package com.example.usuario.dinamicview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;

public class RespuestaActivity extends AppCompatActivity {

    TablaVerdad tablaVerdad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respuesta);
        Button btnIntentKmap;
        boolean tipoEntrada = getIntent().getExtras().getBoolean("TipoEntrada");

        if(tipoEntrada) {
            ArrayList<Arbol> bosque = (ArrayList<Arbol>) getIntent().getSerializableExtra("Bosque");
            TabSim tablaSimbolos = (TabSim) getIntent().getExtras().getSerializable("TablaSimbolos");
            tablaVerdad = new TablaVerdad(RespuestaActivity.this, bosque, tablaSimbolos);
        }
        else {
            int noFunciones = getIntent().getExtras().getInt("NoFunciones");
            int noVariables = getIntent().getExtras().getInt("NoVariables");
            tablaVerdad = new TablaVerdad(RespuestaActivity.this, noFunciones, noVariables);
        }

        try {
            LinearLayout conten = (LinearLayout) findViewById(R.id.layoutRespuesta);
            btnIntentKmap = new Button(RespuestaActivity.this);
            btnIntentKmap.setText("Mapas de Karnaugh");
            btnIntentKmap.setTextColor(getResources().getColor(R.color.colorFondo));
            btnIntentKmap.setBackground(getResources().getDrawable(R.drawable.botonredondo));
            TextView viewMiniterm = new TextView(RespuestaActivity.this);
            viewMiniterm.setGravity(Gravity.CENTER);
            viewMiniterm.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            conten.addView(tablaVerdad);
            conten.addView(viewMiniterm);
            conten.addView(btnIntentKmap);
            btnIntentKmap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<ArrayList<ParEnlazado>> listaEnlaces = tablaVerdad.quineMcCluskey();
                    if(tablaVerdad.getNoVariables() > 1) {
                        Intent intent = new Intent(RespuestaActivity.this, KmapActivity.class);
                        intent.putExtra("nVariables", tablaVerdad.getNoVariables());
                        intent.putExtra("funcionesSalida", tablaVerdad.getFunciones());
                        intent.putExtra("nFunciones", tablaVerdad.getNoFunciones());
                        intent.putExtra("tablaSimbolos",tablaVerdad.getTablaSimbolos());
                        intent.putExtra("lEnlaces", listaEnlaces);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(RespuestaActivity.this, "Una variable, no vale la pena el mapa de karnaugh", Toast.LENGTH_SHORT).show();

                }
            });
        }
        catch(Exception ex)
        {
            String error = ex.getMessage();
        }
    }
}
