package com.example.usuario.dinamicview;

import android.content.Intent;
import android.net.Uri;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.math.*;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    int noFunciones;
    String[] arregloFunciones;
    LinearLayout ll1;
    LinearLayout llf;
    LinearLayout llf1_1;
    LinearLayout llf1_2;
    LinearLayout llt;
    LinearLayout llt1_1;
    LinearLayout llt1_2;
    LinearLayout llt1_3;
    ScrollView sv;
    Spinner spinner;
    Spinner spinerVariables;
    GridView tabla;
    EditText[] arrayEditText;
    Button bIniciar;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ll1     = (LinearLayout) findViewById(R.id.linearLayaout1);
        llf     = (LinearLayout) findViewById(R.id.linerLayoutFuncion);
        llf1_1  = (LinearLayout) findViewById(R.id.LinearLayoutFuncion1_1);
        llf1_2  = (LinearLayout) findViewById(R.id.linerLayoutFuncion1_2);
        llt     = (LinearLayout) findViewById(R.id.linearLayoutTabla);
        llt1_1  = (LinearLayout) findViewById(R.id.linearLayoutTabla1_1);
        llt1_2  = (LinearLayout) findViewById(R.id.linearLayoutTabla1_2);
        llt1_3  = (LinearLayout) findViewById(R.id.linearLayoutTabla1_3);
        client  = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * evento en el cual se activa en el momento de elegir en los radiobutons si se quiere empezar por tabla o
     * por funciones manda a llamar a las interfacez
     *
     * @param v
     */
    public void eventoOnClick(View v) {
        llf1_1.removeAllViews();
        llf1_2.removeAllViews();
        llt1_1.removeAllViews();
        llt1_2.removeAllViews();
        llt1_3.removeAllViews();
        switch (v.getId()) {
            case R.id.rbFunciones:
                llf1_1.removeAllViews();
                llf1_2.removeAllViews();
                CrearInterfazFunciones(v);
                break;
            case R.id.rbTabla:
                llt1_1.removeAllViews();
                llt1_2.removeAllViews();
                llt1_3.removeAllViews();
                CrearInterfazTabla(v);
                break;
        }
    }

    /**
     * Metodo que crea el interfaz si se quiere empezar por funciones mostrando todos los views
     *
     * @param v
     */
    public void CrearInterfazFunciones(View v) {
        noFunciones = 0;
        String[] valores = {"1", "2", "3", "4", "5", "6", "7"};

        TextView tv1 = new TextView(v.getContext());
        spinner = new Spinner(v.getContext());
        Button b = new Button(v.getContext());
        bIniciar = new Button(v.getContext());

        tv1.setText(R.string.nFunciones);
        tv1.setTextColor(getResources().getColor(R.color.colorFondo));
        llf1_1.addView(tv1);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valores));
        llf1_1.addView(spinner);
        b.setText("Crear");
        b.setTextColor(getResources().getColor(R.color.colorFondo));
        b.setBackground(getResources().getDrawable(R.drawable.botonredondo));
        llf1_1.addView(b);

        //Evento si se presiona el boton de crear
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView[] etiqueta;

                llf1_2.removeAllViews();
                noFunciones = Integer.parseInt(spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString());
                arrayEditText = new EditText[noFunciones];
                etiqueta = new TextView[noFunciones];

                for (int i = 0; i < noFunciones; i++) {
                    etiqueta[i] = new TextView(v.getContext());
                    etiqueta[i].setText("Función " + String.valueOf(i + 1));
                    etiqueta[i].setTextColor(getResources().getColor(R.color.colorFondo));
                    arrayEditText[i] = new EditText(v.getContext());
                    arrayEditText[i].setTextColor(getResources().getColor(R.color.colorFondo));
                    arrayEditText[i].setMinLines(1);
                    arrayEditText[i].setMaxLines(1);
                    llf1_2.addView(etiqueta[i]);
                    llf1_2.addView(arrayEditText[i]);
                }
                bIniciar.setText("Iniciar");
                bIniciar.setBackground(getResources().getDrawable(R.drawable.botonredondo));
                bIniciar.setTextColor(getResources().getColor(R.color.colorFondo));
                llf1_2.addView(bIniciar);
            }
        });
        //Evento si se presiona el método de iniciar para mandar las funciones a la siguiente activity
        bIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean funcOK = true;
                AnalizadorSintactico parser = null;
                ArrayList<Arbol> listaArboles = null;
                arregloFunciones = new String[noFunciones];

                for (int i = 0; i < noFunciones; i++) {
                    if (!arrayEditText[i].getText().toString().trim().isEmpty())
                        arregloFunciones[i] = arrayEditText[i].getText().toString();
                    else {
                        funcOK = false;
                        Toast.makeText(MainActivity.this, "Falta introducir la función " + (i + 1), Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                if (funcOK) {
                    parser = new AnalizadorSintactico(MainActivity.this);
                    try {
                        listaArboles = parser.ingresaEntradas(arregloFunciones);
                        goTablaVerdadActivity(listaArboles, parser.regresaTablaSimbolos());
                        //Pasar a la siguiente actividad para generar la tabla de verdad;
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    private void goTablaVerdadActivity(ArrayList<Arbol> bosque, TabSim tablaSimbolos) {
        Intent intent = new Intent(this, RespuestaActivity.class);
        intent.putExtra("TipoEntrada", true);
        intent.putExtra("Bosque", bosque);
        intent.putExtra("TablaSimbolos", tablaSimbolos);
        startActivity(intent);
    }

    public void goTablaVerdadActivity(int noFunciones, int noVariables){
        Intent intent = new Intent(this, RespuestaActivity.class);
        intent.putExtra("TipoEntrada", false);
        intent.putExtra("NoFunciones", noFunciones);
        intent.putExtra("NoVariables", noVariables);
        startActivity(intent);
    }

    /**
     * Crea la interfaz por si se quiere empezar por la tabla de verdad
     *
     * @param v
     */
    public void CrearInterfazTabla(View v) {
        String[] valoresFunciones = {"1", "2", "3", "4", "5", "6", "7"};
        String[] valoresVariables = {"1", "2", "3", "4", "5"};
        TextView tv1 = new TextView(v.getContext());
        TextView tv2 = new TextView(v.getContext());

        spinner = new Spinner(v.getContext());
        spinerVariables = new Spinner(v.getContext());
        Button b = new Button(v.getContext());

        tv1.setText(R.string.nFunciones);
        tv1.setTextColor(getResources().getColor(R.color.colorFondo));
        tv2.setText(R.string.nVariables);
        tv2.setTextColor(getResources().getColor(R.color.colorFondo));
        llt1_1.addView(tv2);

        spinerVariables.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valoresVariables));
        llt1_1.addView(spinerVariables);
        llt1_1.addView(tv1);

        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valoresFunciones));
        llt1_1.addView(spinner);

        b.setText("Crear");
        b.setBackground(getResources().getDrawable(R.drawable.botonredondo));
        b.setTextColor(getResources().getColor(R.color.colorFondo));
        llt1_1.addView(b);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int noVariables = Integer.parseInt(spinerVariables.getItemAtPosition(spinerVariables.getSelectedItemPosition()).toString());
                final int noFunciones = Integer.parseInt(spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString());
                goTablaVerdadActivity(noFunciones, noVariables);
            }
        });
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
