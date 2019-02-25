package com.example.usuario.dinamicview;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CircuitoActivity extends AppCompatActivity {

    int noFilas;
    int noColumnas;
    int tamCuadroAncho;
    int tamCuadroAlto;
    TabSim tablaSimbolos;
    int noVariables;
    ArrayList<Arbol> bosqueFunciones;
    int noFunciones;
    TextView nomVariables[];
    Spinner arraySpinner[];
    Spinner circuitoSpinner;
    LinearLayout ll;
    ImageButton btnPlay;
    Switch tipoSimulacion;
    CircuitoView cv;
    Canvas canvas;
    Bitmap bitmap;
    int height;
    int width;
    String[] arregloFunc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circuito);

        bosqueFunciones = (ArrayList<Arbol>) getIntent().getSerializableExtra("bosque");
        Arbol a = bosqueFunciones.get(0);
        arregloFunc = (String[])getIntent().getSerializableExtra("strFunciones");
        Inicializar();

        if(a.getColumnas() > 3)
            tamCuadroAncho = 150;
        else {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            tamCuadroAncho = size.x / (a.getColumnas() + 2);
        }

        if(a.getRenglones() > 2)
            tamCuadroAlto = 100;
        else {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            tamCuadroAlto = size.y / (a.getRenglones() + 1);
        }

        width =  (a.getColumnas() + 2) * this.tamCuadroAncho;
        height = a.getRenglones() * this.tamCuadroAlto;
        ((TextView)findViewById(R.id.txtFuncActual)).setText(arregloFunc[0]);
        cv = new CircuitoView(this, this.tamCuadroAncho, this.tamCuadroAlto, tablaSimbolos);
        cv.setArbol(a);
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        cv.draw(canvas);
        cv.drawVariables(canvas, height);
        cv.drawConectores(canvas);

        ImageView iv = (ImageView) findViewById(R.id.iview);
        iv.setImageBitmap(bitmap);


        //Iniciar la simulacion
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Simbolo[] simbolos = new Simbolo[nomVariables.length];
                int encendido;
                for(int i = 0; i < nomVariables.length; i++){
                    encendido = Integer.parseInt(arraySpinner[i].getItemAtPosition(arraySpinner[i].getSelectedItemPosition()).toString());
                    if(encendido == 1)
                        tablaSimbolos.getTabla().get(i).setValor(true);
                    else
                        tablaSimbolos.getTabla().get(i).setValor(false);
                }
            }
        });
    }

    /***
     * Inicializa la pantalla
     */
    public void Inicializar(){
        tablaSimbolos = (TabSim) getIntent().getExtras().getSerializable("tablaSimbolos");
        tablaSimbolos.sort();
        noVariables = tablaSimbolos.numeroSimbolos();
        noFunciones = (int) getIntent().getExtras().getInt("noFunciones");
        nomVariables = new TextView[noVariables];
        arraySpinner = new Spinner[noVariables];
        ll = (LinearLayout)findViewById(R.id.Llayout);
        circuitoSpinner = (Spinner) findViewById(R.id.spnnCircuito);
        tipoSimulacion = (Switch) findViewById(R.id.idswitch);
        btnPlay = (ImageButton) findViewById(R.id.btnPlaySim);
        //InsertaVariables(ll);
        llenaSpinnerCircuito();
    }

    /***
     * Metodo para llenar el spinner que contendra el numero de circuitos a mostrar
     */
    public void llenaSpinnerCircuito(){
        String[] lCircuito = new String[noFunciones];
        for(int i = 0; i < noFunciones; i++){
            lCircuito[i] = "Circuito " + String.valueOf(i + 1);
        }

        circuitoSpinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, lCircuito));

        circuitoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String nSCircuito = (String) circuitoSpinner.getItemAtPosition(circuitoSpinner.getSelectedItemPosition()).toString();
                int nCircuito = Integer.parseInt(nSCircuito.substring(nSCircuito.length()-1, nSCircuito.length()));
                Arbol a = bosqueFunciones.get(nCircuito - 1);
                if(a.getColumnas() > 3)
                    tamCuadroAncho = 150;
                else {
                    Display display = getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    tamCuadroAncho = size.x / (a.getColumnas() + 2);
                }

                if(a.getRenglones() > 2)
                    tamCuadroAlto = 100;
                else {
                    Display display = getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    tamCuadroAlto = size.y / (a.getRenglones() + 1);
                }

                width =  (a.getColumnas() + 2) * tamCuadroAncho;
                height = a.getRenglones() * tamCuadroAlto;
                ((TextView)findViewById(R.id.txtFuncActual)).setText(arregloFunc[nCircuito - 1]);
                cv.setArbol(a);
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                canvas = new Canvas(bitmap);
                cv.draw(canvas);
                cv.drawVariables(canvas, height);
                cv.drawConectores(canvas);
                ImageView iv = (ImageView) findViewById(R.id.iview);
                iv.setImageBitmap(bitmap);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /***
     * Metodo para intertar las variables con su respectivo spinner y asi cambiar los valores de entrada
     * @param ll layout donde se cargaran los views
     */
    public  void InsertaVariables(LinearLayout ll){
        char car = 'a';
        String[] valores = {"0", "1"};

        for (int i = 0; i < noVariables; i++) {
            arraySpinner[i] = new Spinner(this);
            nomVariables[i] = new TextView(this);
            if(tablaSimbolos != null)
                nomVariables[i].setText(tablaSimbolos.getTabla().get(i).getNombre());
            else {
                nomVariables[i].setText(String.valueOf(car));
                car++;
            }
            arraySpinner[i].setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valores));
            ll.addView(nomVariables[i]);
            ll.addView(arraySpinner[i]);
        }
    }

    public  class CircuitoView extends View{
        private int width;
        private int height;
        private Context contexto;
        private Arbol arbol;
        private int tamCuadroAncho;
        private int tamCuadroAlto;
        private TabSim tablaSimbolos;

        public CircuitoView(Context context, int tamAncho, int tamAlto, TabSim tablaSimbolos){
            super(context);
            this.contexto = context;
            this.tamCuadroAncho = tamAncho;
            this.tamCuadroAlto = tamAlto;
            this.tablaSimbolos = tablaSimbolos;
        }

        public void setArbol(Arbol a)
        {
            this.arbol = a;
        }

        @Override
        protected void onDraw(Canvas canvas){
            super.onDraw(canvas);
            this.width = (this.arbol.getColumnas() + 2) * this.tamCuadroAncho;
            this.height = (this.arbol.getRenglones() + 1) * this.tamCuadroAlto;
            this.arbol.draw(canvas, this.contexto, this.tamCuadroAncho, this.tamCuadroAlto);
        }

        public  void drawVariables(Canvas canvas, int height){
            int desplX = (this.tamCuadroAncho - (this.tablaSimbolos.numeroSimbolos() * 15)) / ((this.tablaSimbolos.numeroSimbolos() * 2) + 1);
            int x = desplX;

            for(Simbolo s : this.tablaSimbolos.getTabla())
            {
                Variable v = new Variable();
                v.setNombre(s.getNombre());
                v.setValor(s.getValor());
                v.setX(x);
                v.setY(10);
                v.draw(canvas, height);
                x += desplX + 15;
                s.setVariable(v);
            }
        }

        public void drawConectores(Canvas canvas)
        {
            this.arbol.dibujaConectores(canvas, this.tablaSimbolos);
            invalidate();
        }
    }
}

