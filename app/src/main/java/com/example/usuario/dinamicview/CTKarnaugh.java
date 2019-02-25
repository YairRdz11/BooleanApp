package com.example.usuario.dinamicview;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.app.INotificationSideChannel;
import android.test.suitebuilder.TestMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.net.PortUnreachableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static android.widget.Toast.makeText;

/**
 * Created Yair Rodriguez on 21/09/2016.
 */
public class CTKarnaugh{
    private TableLayout tabla;          // Layout donde se pintará la tabla
    private TableLayout tabla2;         // Layout donde se pintará la tabla A positivo si son 5 variables
    private ArrayList<TableRow> filas;  // Array de las filas de la tabla
    private Activity actividad;         //Activirty donde pintaremos la tabla
    private Resources rs;
    private int FILAS, COLUMNAS;        // Filas y columnas de nuestra mapa
    String[] arraycabecera;             //Arreglo de cabecera del mapa
    ArrayList<String> funciones;        //Arreglo de caracteres que contendra la funcion segun sea el caso
    ArrayList<Integer> secuenciaMapa;   //secuencia auxuliar para poderles poner tag a las celdas del mapa
    int posCelda;                       //Intereo para saber en que posicion estoy de la tabla
    TextView tvMas;
    TextView tvMenos;
    TabSim tabSim;

    /***
     *
     * @param actividad
     * @param tabla1
     * @param nVariables
     * @param arrayFunciones
     */
    public  CTKarnaugh(Activity actividad, TableLayout tabla1,TableLayout t2,int nVariables,ArrayList<String>arrayFunciones,TabSim tablaSimbolos)
    {
        this.actividad = actividad;
        this.tabla = tabla1;
        this.tabla2 = t2;
        rs = this.actividad.getResources();
        FILAS = 0;
        posCelda = 0;
        COLUMNAS = nVariables;
        filas = new ArrayList<TableRow>();
        funciones = arrayFunciones;
        tabSim = tablaSimbolos;
        InicializarTag();
        configuraTabla();
    }

    /***
     *Creamos el mapa de Karnaugh apartir de un numero de funcion
     * @param noFuncion numero de funcion a mostrar
     */
    public void CrearMapaKarnaugh(int noFuncion){
        ArrayList<String> funcionSalida = new ArrayList<>();
        ArrayList<ArrayList<String>> tvKarnaugh = new ArrayList<ArrayList<String>>();


        posCelda = 0;
        ObtenCabecera();
        if(COLUMNAS <= 4) {
            agregarCabecera();
        }
        else {
            tvMas = (TextView) actividad.findViewById(R.id.tvAmas);
            tvMenos = (TextView) actividad.findViewById(R.id.tvAmenos);
            tvMas.setText((tabSim.getTabla().get(0).getNombre()).toUpperCase());
            tvMenos.setText((tabSim.getTabla().get(0).getNombre()).toUpperCase()+"'");
            agregarCabecera(tabla);
            agregarCabecera(tabla2);
        }
        funcionSalida = obtenFuncionSalida(noFuncion);
        formatKarnaugh(COLUMNAS,tvKarnaugh,funcionSalida);

    }

    private void configuraTabla() {
        tabla.setLayoutParams(new TableLayout.LayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, (TableLayout.LayoutParams.MATCH_PARENT) - 50)));
        tabla.setStretchAllColumns(true);
        tabla2.setLayoutParams(new TableLayout.LayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, (TableLayout.LayoutParams.MATCH_PARENT) - 50)));
        tabla2.setStretchAllColumns(true);
    }

    private void formatKarnaugh(int nVariables, ArrayList<ArrayList<String>> tvKarnaugh,ArrayList<String> funcionSalida){
        ArrayList<String> lColumnas = new ArrayList<String>();

        switch (nVariables)
        {
            case 2:
                lColumnas.add("0");
                lColumnas.add("1");
                tvKarnaugh = getFormatTKarnaugh(funcionSalida,lColumnas,2,2);
                for(int k = 0; k < 2;k++){
                    agregarFilaTabla(tvKarnaugh.get(k));
                }
                break;
            case 3:
                lColumnas.add("0");
                lColumnas.add("1");
                tvKarnaugh = getFormatTKarnaugh(funcionSalida,lColumnas,2,4);
                tvKarnaugh = CambiarValoresTres(tvKarnaugh);
                for(int k = 0; k < 2;k++){
                    agregarFilaTabla(tvKarnaugh.get(k));
                }
                break;
            case 4:
                lColumnas.add("00");
                lColumnas.add("01");
                lColumnas.add("11");
                lColumnas.add("10");
                tvKarnaugh = getFormatTKarnaugh(funcionSalida,lColumnas,4,4);
                tvKarnaugh = CambiarValoresCuatro(tvKarnaugh);
                for(int k = 0; k < 4;k++){
                    agregarFilaTabla(tvKarnaugh.get(k));
                }
                break;
            case 5:
                ArrayList<String> funcionS1Part;
                ArrayList<String> funcionS2Part;
                lColumnas.add("00");
                lColumnas.add("01");
                lColumnas.add("11");
                lColumnas.add("10");
                funcionS1Part = DividirFuncionSalida(funcionSalida,1);
                tvKarnaugh = getFormatTKarnaugh(funcionS1Part,lColumnas,4,4);
                tvKarnaugh = CambiarValoresCuatro(tvKarnaugh);
                for(int k = 0; k < 4;k++){
                    agregarFilaTabla(tvKarnaugh.get(k));
                }
                funcionS2Part = DividirFuncionSalida(funcionSalida,2);
                tvKarnaugh = getFormatTKarnaugh(funcionS2Part,lColumnas,4,4);
                tvKarnaugh = CambiarValoresCuatro(tvKarnaugh);
                posCelda = 0;
                for(int k = 0; k < 4;k++){
                    agregarFilaTabla(tvKarnaugh.get(k),posCelda);
                }
                break;
        }
    }

    private ArrayList<String> DividirFuncionSalida(ArrayList<String> funcionSalida, int num){
        ArrayList<String> lista = new ArrayList<String>();
        int mitad = (int)(Math.pow(2,COLUMNAS))/2;
        int terminar = 0;
        int i = 0;
        if(num == 1) {
            i = 0;
            terminar = mitad;
        }
        if(num == 2) {
            i = mitad;
            terminar = funcionSalida.size();
        }
        for(; i < terminar; i++){
            lista.add(funcionSalida.get(i));
        }

        return  lista;
    }


    private void ObtenCabecera(){
        String cab = "";

        for(int i = 0; i < tabSim.getTabla().size();i++){
            if(tabSim.getTabla().size() != 5)
            {
                if((tabSim.getTabla().size() == 2 || tabSim.getTabla().size() == 3) && i == 1)
                    cab += "/";
                else if(tabSim.getTabla().size() == 4 && i == 2)
                    cab += "/";
                cab += (tabSim.getTabla().get(i).getNombre()).toUpperCase();
            }
            else
            {
                if(i != 0)
                    cab += (tabSim.getTabla().get(i).getNombre()).toUpperCase();
                if(i == 2)
                    cab += "/";
            }

        }
        switch (COLUMNAS)
        {
            case 2:
                arraycabecera = new String[]{cab,"0","1"};
                break;
            case 3:
                arraycabecera = new String[]{cab,"00","01","11","10"};
                break;
            case 4:
                arraycabecera = new String[]{cab,"00","01","11","10"};
                break;
            case 5:
                arraycabecera = new String[]{cab,"00","01","11","10"};
                break;
        }
    }

    private ArrayList<String> obtenFuncionSalida(int noFuncion){
        ArrayList<String> funcionSalida = new ArrayList<>();
        String cad;

        for(int i = 0; i < funciones.size();i++){
            cad = (funciones.get(i).substring(noFuncion - 1,noFuncion)).toString();
            funcionSalida.add(cad);
        }

        return funcionSalida;
    }

    /**
     * Agrega la cabecera al table loyout
     * @param
     */
    public void agregarCabecera()
    {
        TableRow.LayoutParams layoutCelda;
        TableRow fila = new TableRow(actividad);
        TableRow fila2 = new TableRow(actividad);
        TableRow.LayoutParams layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        fila.setLayoutParams(layoutFila);
        fila2.setLayoutParams(layoutFila);

        for (int i = 0; i < arraycabecera.length; i++) {
            TextView texto = new TextView(actividad);
            layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(arraycabecera[i]), TableRow.LayoutParams.WRAP_CONTENT);
            layoutCelda.height = 30;
            texto.setText(arraycabecera[i]);
            texto.setGravity(Gravity.CENTER_HORIZONTAL);
            texto.setLayoutParams(layoutCelda);
            fila.addView(texto);
        }
        tabla.addView(fila);
        filas.add(fila);
        FILAS++;
    }

    public void agregarCabecera(TableLayout tabla)
    {
        TableRow.LayoutParams layoutCelda;
        TableRow fila = new TableRow(actividad);
        TableRow fila2 = new TableRow(actividad);
        TableRow.LayoutParams layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        fila.setLayoutParams(layoutFila);
        fila2.setLayoutParams(layoutFila);

        for (int i = 0; i < arraycabecera.length; i++) {
            TextView texto = new TextView(actividad);
            layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(arraycabecera[i]), TableRow.LayoutParams.WRAP_CONTENT);
            layoutCelda.height = 30;
            texto.setText(arraycabecera[i]);
            texto.setGravity(Gravity.CENTER_HORIZONTAL);
            texto.setLayoutParams(layoutCelda);
            fila.addView(texto);
        }
        tabla.addView(fila);
        filas.add(fila);
        FILAS++;
    }

    /**
     * Agrega una fila a la tabla
     * @param elementos Elementos de la fila
     */
    public void agregarFilaTabla(ArrayList<String> elementos)
    {
        TextView texto = new TextView(actividad);

        TableRow.LayoutParams layoutCelda;
        TableRow.LayoutParams layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        TableRow fila = new TableRow(actividad);
        fila.setLayoutParams(layoutFila);

        for(int i = 0; i< elementos.size(); i++)
        {
            texto = new TextView(actividad);
            if(i != 0) {
                texto.setTag(secuenciaMapa.get(posCelda));
                posCelda++;
            }
            texto.setText(String.valueOf(elementos.get(i)));
            texto.setGravity(Gravity.CENTER_HORIZONTAL);
            layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(texto.getText().toString()), TableRow.LayoutParams.WRAP_CONTENT);
            texto.setLayoutParams(layoutCelda);
            fila.addView(texto);
        }
        tabla.addView(fila);
        filas.add(fila);
        FILAS++;
    }

    public void agregarFilaTabla(ArrayList<String> elementos,int num)
    {
        TableRow.LayoutParams layoutCelda;
        TableRow.LayoutParams layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        TableRow fila = new TableRow(actividad);
        fila.setLayoutParams(layoutFila);

        for(int i = 0; i< elementos.size(); i++)
        {
            TextView texto = new TextView(actividad);
            if(i != 0) {
                texto.setTag(secuenciaMapa.get(posCelda));
                posCelda++;
            }
            texto.setText(String.valueOf(elementos.get(i)));
            texto.setGravity(Gravity.CENTER_HORIZONTAL);
            layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(texto.getText().toString()), TableRow.LayoutParams.WRAP_CONTENT);
            texto.setLayoutParams(layoutCelda);
            fila.addView(texto);
        }
        tabla2.addView(fila);
        filas.add(fila);
        FILAS++;
    }

    /**
     * Obtiene el ancho en píxeles de un texto en un String
     * @param texto Texto
     * @return Ancho en píxeles del texto
     */
    private int obtenerAnchoPixelesTexto(String texto)
    {
        Paint p = new Paint();
        Rect bounds = new Rect();
        p.setTextSize(50);

        p.getTextBounds(texto, 0, texto.length(), bounds);
        return bounds.width();
    }

    public  ArrayList<ArrayList<String>> getFormatTKarnaugh(ArrayList<String> lResultados, ArrayList<String> columnas,int nRenglones,int nCol){
        ArrayList<ArrayList<String>> tKarnaugh = new ArrayList<ArrayList<String>>();
        ArrayList<String> listAux = new ArrayList<String>();
        int contAux = 0;

        for(int i = 0;i < nRenglones;i++){
            for(int j = 0; j < nCol; j++){
                if(j == 0)
                    listAux.add(columnas.get(i));
                listAux.add(lResultados.get(contAux));
                contAux++;
            }
            tKarnaugh.add(listAux);
            listAux = new ArrayList<String>();
        }

        return  tKarnaugh;
    }

    public ArrayList<ArrayList<String>> CambiarValoresTres(ArrayList<ArrayList<String>> tKarnaugh){
        ArrayList<ArrayList<String>> nueva = new ArrayList<ArrayList<String>>();
        String cadCambio = null;

        nueva = tKarnaugh;
        for(int i = 0; i < tKarnaugh.size();i++){
            for(int j = 1; j < tKarnaugh.get(i).size();j++){
                if(j == 3){
                    cadCambio = tKarnaugh.get(i).get(j);
                }
                if(j == 4){
                    nueva.get(i).set(3,tKarnaugh.get(i).get(j));
                    nueva.get(i).set(4,cadCambio);
                }
            }
        }
        return nueva;
    }

    public ArrayList<ArrayList<String>> CambiarValoresCuatro(ArrayList<ArrayList<String>> tKarnaugh){
        ArrayList<ArrayList<String>> nueva = new ArrayList<ArrayList<String>>();
        String cadCambio = null;
        nueva = tKarnaugh;

        for(int i = 0; i < tKarnaugh.size();i++){
            for(int j = 1; j < tKarnaugh.get(i).size();j++){
                if(j == 3){
                    cadCambio = tKarnaugh.get(i).get(j);
                }
                if(j == 4){
                    nueva.get(i).set(3,tKarnaugh.get(i).get(j));
                    nueva.get(i).set(4,cadCambio);
                }
            }
        }

        for(int i = 1; i < tKarnaugh.get(0).size();i++){
            for(int j = 0; j < tKarnaugh.size();j++){
                if(j == 2){
                    cadCambio = tKarnaugh.get(j).get(i);
                }
                if(j == 3){
                    nueva.get(j - 1).set(i,nueva.get(j).get(i));
                    nueva.get(j).set(i,cadCambio);
                }
            }
        }
        return nueva;
    }

    public void configuraCeldaAccion(ArrayList<Integer> enlaceTemp,int contEnlace, boolean bandAvance)
    {
        Integer varCinco = 0;
        int R = (int)(Math.random()*256);
        int G = (int)(Math.random()*256);
        int B = (int)(Math.random()*256);

        TextView celda;

        for (int i = 0; i < enlaceTemp.size(); i++) {
            if(enlaceTemp.get(i) < 16) {
                celda = (TextView) tabla.findViewWithTag(enlaceTemp.get(i));
            }
            else {
                varCinco = enlaceTemp.get(i) - 16;
                celda = RegresaCeldaMapaDos(varCinco);//(TextView) tabla2.findViewWithTag(varCinco);// .findViewWithTag(varCinco);
            }
            if (bandAvance)
                celda.setBackgroundColor(Color.rgb(R, G, B));
            else
                celda.setBackgroundColor(Color.WHITE);

        }
    }


    public  void InicializarTag()
    {

        secuenciaMapa = new ArrayList<Integer>();
        if(tabSim.getTabla().size() != 2) {
            secuenciaMapa.add(0);
            secuenciaMapa.add(1);
            secuenciaMapa.add(3);
            secuenciaMapa.add(2);
            secuenciaMapa.add(4);
            secuenciaMapa.add(5);
            secuenciaMapa.add(7);
            secuenciaMapa.add(6);
            secuenciaMapa.add(12);
            secuenciaMapa.add(13);
            secuenciaMapa.add(15);
            secuenciaMapa.add(14);
            secuenciaMapa.add(8);
            secuenciaMapa.add(9);
            secuenciaMapa.add(11);
            secuenciaMapa.add(10);
        }
        else {
            secuenciaMapa.add(0);
            secuenciaMapa.add(1);
            secuenciaMapa.add(2);
            secuenciaMapa.add(3);
        }
    }
    public void ReiniciarMapa(){
        TextView celda;
        int tam;
        if(COLUMNAS <= 4) {
            tam = (int) Math.pow(2, COLUMNAS);
            for(int i = 0; i < tam ;i++){
                celda =  (TextView) tabla.findViewWithTag(i);
                celda.setBackgroundColor(Color.WHITE);
            }
        }
        else {
            tam = 16;
            for(int i = 0; i < tam ;i++){
                celda =  (TextView) tabla2.findViewWithTag(i);
                celda.setBackgroundColor(Color.WHITE);
            }
        }
    }

    public TextView RegresaCeldaMapaDos(int i){
        TextView celda;

        celda =  (TextView) tabla2.findViewWithTag(i);

        return  celda;
    }

}
