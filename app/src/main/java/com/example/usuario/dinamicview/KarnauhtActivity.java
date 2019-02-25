package com.example.usuario.dinamicview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.Toast;

import java.util.ArrayList;


public class KarnauhtActivity extends AppCompatActivity {

    TableLayout[] arrTablaLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karnauht);
        CTablaVerdad tablaVerdad;
        ArrayList<String> resultadosTVerdad;
        String[] tabla;
        String[] cabecera = null;
        ArrayList<String> tResultados = new ArrayList<String>();
        ArrayList<ArrayList<String>> tvKarnaugh = new ArrayList<ArrayList<String>>();
        ArrayList<String> lColumnas = new ArrayList<String>();
        int nVariables;
        int nFunciones;
        Bundle bundle = getIntent().getExtras();
        tabla = bundle.getStringArray("tVerdad");
        nVariables = bundle.getInt("variables");
        nFunciones = bundle.getInt("funciones");
        tablaVerdad = new CTablaVerdad(tabla,nVariables,nFunciones);

        arrTablaLayout = new TableLayout[nFunciones];

        switch (nVariables)
        {
            case 2:
                Toast.makeText(KarnauhtActivity.this, "2", Toast.LENGTH_SHORT).show();
                cabecera = new String[]{"a/b","0","1"};
                break;
            case 3:
                Toast.makeText(KarnauhtActivity.this, "3", Toast.LENGTH_SHORT).show();
                cabecera = new String[]{"a/bc","00","01","11","10"};
                break;
            case 4:
                Toast.makeText  (KarnauhtActivity.this, "4", Toast.LENGTH_SHORT).show();
                cabecera = new String[]{"ab/cd","00","01","11","10"};
                break;

        }
        //CTKarnaugh tKarnaugh = new CTKarnaugh(this, (TableLayout) findViewById(R.id.tablaKarnaugh),nVariables,cabecera);
        //CTKarnaugh tKarnaugh = new CTKarnaugh(this, arrTablaLayout ,nVariables,cabecera);
        for(int i = 0; i < nFunciones; i++) {
           // tKarnaugh.agregarCabecera(i);
            resultadosTVerdad = tablaVerdad.getListResultado(i + 1);
            switch (nVariables)
            {
                case 2:
                    lColumnas.add("0");
                    lColumnas.add("1");
                    tvKarnaugh = tablaVerdad.getFormatTKarnaugh(resultadosTVerdad,lColumnas,2,2);
                    for(int k = 0; k < 2;k++){
             //           tKarnaugh.agregarFilaTabla(tvKarnaugh.get(k),i);
                    }
                    break;
                case 3:
                    lColumnas.add("0");
                    lColumnas.add("1");
                    tvKarnaugh = tablaVerdad.getFormatTKarnaugh(resultadosTVerdad,lColumnas,2,4);
                    tvKarnaugh = tablaVerdad.CambiarValoresTres(tvKarnaugh);
                    for(int k = 0; k < 2;k++){
                        //tKarnaugh.agregarFilaTabla(tvKarnaugh.get(k),i);
                    }
                    break;
                case 4:
                    lColumnas.add("00");
                    lColumnas.add("01");
                    lColumnas.add("11");
                    lColumnas.add("10");
                    tvKarnaugh = tablaVerdad.getFormatTKarnaugh(resultadosTVerdad,lColumnas,4,4);
                    tvKarnaugh = tablaVerdad.CambiarValoresCuatro(tvKarnaugh);
                    for(int k = 0; k < 4;k++){
                        //tKarnaugh.agregarFilaTabla(tvKarnaugh.get(k),i);
                    }
                    break;
            }
        }
    }

    public ArrayList<String> getTablaResultado(String[] tablaFull,int indiceFuncion,int variables){
        ArrayList<String> tResultado = new ArrayList<>();
        int contVariable = 0;

        for(int i = 0; i < tablaFull.length;i++)
        {
            if(contVariable == (variables + indiceFuncion) - 1) {
                tResultado.add(tablaFull[i]);
                contVariable = 0;
            }
            contVariable++;
        }
        return  tResultado;
    }
}
