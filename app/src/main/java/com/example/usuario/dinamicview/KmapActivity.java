package com.example.usuario.dinamicview;

import android.content.Intent;
import android.content.ServiceConnection;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static java.lang.Math.pow;

public class KmapActivity extends AppCompatActivity {

    LinearLayout ll;
    ArrayList<ParEnlazado> enlaces;
    ArrayList<String> funciones;
    ArrayList<ArrayList<ParEnlazado>> listaEnlaces;
    int noVariables;
    int noFuncionesIntent;
    TabSim tablaSimbolos;
    Spinner spinnerFunciones;
    TableLayout tlMapK;
    TableLayout tlMapK2;
    TextView tvFuncion;
    TextView tvAmas;
    TextView tvAmenos;
    Button btnCircuito;
    String funcionAct;
    int noFuncion;
    int contEnlace;
    private ArrayList<String> funcionesMin;
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kmap);
        final CTKarnaugh mapaKarnaugh;

        Inicializa();

        ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
        ImageButton btnNext = (ImageButton) findViewById(R.id.btnNext);
        ImageButton btnAde  = (ImageButton) findViewById(R.id.btnAdela);
        ImageButton btnAtras= (ImageButton) findViewById(R.id.btnAtras);
        ImageButton btnPlay = (ImageButton) findViewById(R.id.btnPlay);

        mapaKarnaugh = new CTKarnaugh(KmapActivity.this,tlMapK,tlMapK2,noVariables,funciones,tablaSimbolos);
        tvFuncion.setText("Función 1");

        LlenarSpinner();
        /**
         * Evento para seleccionar un mapa de karnaugh apartir del spinner
         */
        spinnerFunciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tlMapK.removeAllViews();
                tlMapK2.removeAllViews();
                funcionAct = (String) spinnerFunciones.getItemAtPosition(spinnerFunciones.getSelectedItemPosition()).toString();
                tvFuncion.setText(funcionAct);
                noFuncion = Integer.parseInt(funcionAct.substring(funcionAct.length()-1, funcionAct.length()));
                mapaKarnaugh.CrearMapaKarnaugh(noFuncion);
                contEnlace = 0;
                ((TextView) findViewById(R.id.txtFuncMin)).setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        /**
         * Evento que muestra las mapas de Karnaugh apartir del boton
         */
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timer != null)
                    timer.cancel();
                tlMapK.removeAllViews();
                tlMapK2.removeAllViews();
                noFuncion--;
                if(noFuncion <= 0)
                    noFuncion = noFuncionesIntent;
                tvFuncion.setText("Función " + String.valueOf(noFuncion) );
                mapaKarnaugh.CrearMapaKarnaugh(noFuncion);
                contEnlace = 0;
                ((TextView) findViewById(R.id.txtFuncMin)).setText("");
            }
        });
        /**
         * Evento que muestra las mapas de Karnaugh apartir del boton
         */
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timer != null)
                    timer.cancel();
                tlMapK.removeAllViews();
                tlMapK2.removeAllViews();
                noFuncion++;
                if(noFuncion > noFuncionesIntent)
                    noFuncion = 1;
                tvFuncion.setText("Función " + String.valueOf(noFuncion) );
                mapaKarnaugh.CrearMapaKarnaugh(noFuncion);
                contEnlace = 0;
                ((TextView) findViewById(R.id.txtFuncMin)).setText("");
            }
        });
        btnAde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timer != null)
                    timer.cancel();
                enlaces = listaEnlaces.get(noFuncion - 1);
                if(enlaces.size() != 0) {
                    if (contEnlace < enlaces.size()) {
                        ArrayList<Integer> enlaceTemp = enlaces.get(contEnlace).getEnlace();
                        mapaKarnaugh.configuraCeldaAccion(enlaceTemp, contEnlace, true);
                        if(enlaces.size() == 1 && enlaceTemp.size() == pow(2, noVariables))
                            ((TextView)findViewById(R.id.txtFuncMin)).setText("Vcc");
                        else {
                            String funcMin = ((TextView) findViewById(R.id.txtFuncMin)).getText().toString();
                            if (contEnlace > 0)
                                funcMin += "+";
                            funcMin += enlaces.get(contEnlace).getEcuacionLiteral();
                            ((TextView) findViewById(R.id.txtFuncMin)).setText(funcMin);
                        }
                        contEnlace++;
                    } else {
                        Toast.makeText(KmapActivity.this, "Fin de obtención de función", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(KmapActivity.this, "No hay enlaces para mostrar en el mapa de karnaugh", Toast.LENGTH_SHORT).show();
                    ((TextView)findViewById(R.id.txtFuncMin)).setText("Gnd");
                }
            }
        });
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timer != null)
                    timer.cancel();
                enlaces = listaEnlaces.get(noFuncion - 1);
                if(enlaces.size() != 0) {
                    ArrayList<Integer> enlaceTemp;
                    ((TextView) findViewById(R.id.txtFuncMin)).setText("");
                    if (contEnlace > 0) {
                        contEnlace--;
                        enlaceTemp = enlaces.get(contEnlace).getEnlace();
                        mapaKarnaugh.configuraCeldaAccion(enlaceTemp, contEnlace, false);
                        String funcMin = "";
                        for (int i = 0; i < contEnlace; i++) {
                            enlaceTemp = enlaces.get(i).getEnlace();
                            if(i > 0)
                                funcMin += "+";
                            funcMin += enlaces.get(i).getEcuacionLiteral();
                            mapaKarnaugh.configuraCeldaAccion(enlaceTemp, contEnlace, true);
                            ((TextView) findViewById(R.id.txtFuncMin)).setText(funcMin);
                        }
                    }
                }
                else
                    Toast.makeText(KmapActivity.this, "No hay enlaces para mostrar en el mapa de karnaugh", Toast.LENGTH_SHORT).show();
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(timer != null)
                    timer.cancel();
                int milim = 1200;
                contEnlace = 0;
                ((TextView) findViewById(R.id.txtFuncMin)).setText("");
                mapaKarnaugh.ReiniciarMapa();
                enlaces = listaEnlaces.get(noFuncion - 1);
                if(enlaces.size() != 0) {
                    timer = new CountDownTimer((milim) * enlaces.size(), milim) {
                                ArrayList<Integer> enlaceTemp;
                                String funcMin = ((TextView) findViewById(R.id.txtFuncMin)).getText().toString();

                                public void onTick(long millisUntilFinished) {
                                    enlaceTemp = enlaces.get(contEnlace).getEnlace();
                                    mapaKarnaugh.configuraCeldaAccion(enlaceTemp, contEnlace, true);
                                    if(enlaces.size() == 1 && enlaceTemp.size() == pow(2, noVariables))
                                    {
                                        ((TextView) findViewById(R.id.txtFuncMin)).setText("Vcc");
                                    }
                                    else {
                                        if (contEnlace > 0)
                                            funcMin += "+";
                                        funcMin += enlaces.get(contEnlace).getEcuacionLiteral();
                                        ((TextView) findViewById(R.id.txtFuncMin)).setText(funcMin);
                                    }
                                    contEnlace++;
                                }

                                public void onFinish() {
                                    enlaceTemp = enlaces.get(contEnlace).getEnlace();
                                    mapaKarnaugh.configuraCeldaAccion(enlaceTemp, contEnlace, true);
                                    if(enlaces.size() == 1 && enlaceTemp.size() == pow(2, noVariables))
                                    {
                                        ((TextView) findViewById(R.id.txtFuncMin)).setText("Vcc");
                                    }
                                    else {
                                        if (contEnlace > 0)
                                            funcMin += "+";
                                        funcMin += enlaces.get(contEnlace).getEcuacionLiteral();
                                        ((TextView) findViewById(R.id.txtFuncMin)).setText(funcMin);
                                    }
                                    Toast.makeText(KmapActivity.this, "Fin de obtención de función", Toast.LENGTH_SHORT).show();
                                    contEnlace++;
                                }
                            };
                    timer.start();
                }
                else {
                    Toast.makeText(KmapActivity.this, "No hay enlaces para mostrar en el mapa de karnaugh", Toast.LENGTH_SHORT).show();
                    ((TextView)findViewById(R.id.txtFuncMin)).setText("Gnd");
                }
            }
        });

        btnCircuito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timer != null)
                    timer.cancel();
                if(listaEnlaces.size() > 0) {
                    if (listaEnlaces.size() >= 1 && listaEnlaces.get(0).size() > 0) {
                        if(listaEnlaces.get(0).size() > 0 && listaEnlaces.get(0).get(0).getEnlace().size() < pow(2, noVariables)) {
                            funcionesMin = new ArrayList<String>();

                            for (ArrayList<ParEnlazado> enlace : listaEnlaces) {
                                String funcMin = "";
                                for (ParEnlazado termino : enlace) {
                                    funcMin += termino.getEcuacionLiteral();
                                    if (enlace.indexOf(termino) < enlace.size() - 1)
                                        funcMin += "+";
                                }
                                funcionesMin.add(funcMin);
                            }
                            try {
                                ArrayList<Arbol> listaArboles = null;
                                AnalizadorSintactico parser = new AnalizadorSintactico(KmapActivity.this);
                                String[] arregloFunc = new String[funcionesMin.size()];
                                arregloFunc = funcionesMin.toArray(arregloFunc);
                                listaArboles = parser.ingresaEntradas(arregloFunc);
                                for (Arbol a : listaArboles) {
                                    a.asignaRenglonesColumnas();
                                }
                                Intent intent = new Intent(KmapActivity.this, CircuitoActivity.class);
                                intent.putExtra("tablaSimbolos", parser.regresaTablaSimbolos());
                                intent.putExtra("noVariables", noVariables);
                                intent.putExtra("lEnlaces", listaEnlaces);
                                intent.putExtra("bosque", listaArboles);
                                intent.putExtra("noFunciones", noFuncionesIntent);
                                intent.putExtra("strFunciones", arregloFunc);
                                startActivity(intent);
                            } catch (Exception ex) {
                                Toast.makeText(KmapActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(KmapActivity.this, "Existe un solo enlace en el mapa de karnaugh y engloba todos los campos", Toast.LENGTH_SHORT).show();
                            ((TextView)findViewById(R.id.txtFuncMin)).setText("Vcc");
                        }
                    }
                    else
                    {
                        Toast.makeText(KmapActivity.this, "No hay enlaces para mostrar en el mapa de karnaugh", Toast.LENGTH_SHORT).show();
                        ((TextView)findViewById(R.id.txtFuncMin)).setText("Gnd");
                    }
                }
                else
                {
                    Toast.makeText(KmapActivity.this, "No hay enlaces para mostrar en el mapa de karnaugh", Toast.LENGTH_SHORT).show();
                    ((TextView)findViewById(R.id.txtFuncMin)).setText("Gnd");
                }
            }
        });
    }

    private void LlenarSpinner(){
        String[] valoresSpinner = new String[noFuncionesIntent];

        for(int i = 0; i < noFuncionesIntent;i++){
            valoresSpinner[i] = "Función " + String.valueOf(i + 1);
        }

        spinnerFunciones.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valoresSpinner));
    }

    private void Inicializa()
    {
        funciones           = getIntent().getExtras().getStringArrayList("funcionesSalida");
        noVariables         = getIntent().getExtras().getInt("nVariables");
        noFuncionesIntent   = getIntent().getExtras().getInt("nFunciones");
        tablaSimbolos       = (TabSim) getIntent().getExtras().getSerializable("tablaSimbolos");
        listaEnlaces        = (ArrayList<ArrayList<ParEnlazado>>) getIntent().getSerializableExtra("lEnlaces");
        ll                  = (LinearLayout) findViewById(R.id.llBotones);
        spinnerFunciones    = (Spinner) findViewById(R.id.spNoKmap);
        tlMapK              = (TableLayout) findViewById(R.id.tlKmap);
        tlMapK2             = (TableLayout) findViewById(R.id.tlKmap2);
        tvFuncion           = (TextView) findViewById(R.id.tvFuncion);
        tvAmas              = (TextView) findViewById(R.id.tvAmas);
        tvAmenos            = (TextView) findViewById(R.id.tvAmenos);
        btnCircuito         = (Button) findViewById(R.id.btnCircuito);
        ((TextView) findViewById(R.id.txtFuncMin)).setText("");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(timer != null)
            timer.cancel();
    }
    private void ConfiguraCeldas(TableLayout tabla,ArrayList<String> enlace){

    }

}
