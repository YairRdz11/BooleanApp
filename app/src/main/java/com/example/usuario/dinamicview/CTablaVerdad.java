package com.example.usuario.dinamicview;

import android.content.Context;
import android.widget.TableLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.math.*;
/**
 * Created by Yair Rodriguez on 21/09/2016.
 */
public class CTablaVerdad
{
    String[] tabla;
    int nVariables;
    int nFunciones;

    public CTablaVerdad(String[] arrTabla, int numVar, int numFunc)
    {
        tabla = arrTabla;
        nVariables = numVar;
        nFunciones = numFunc;
    }

    /**
     * Método que regresa la lista re resultados de la función
     * @param indice que función desea regresar
     * @return
     */
    public ArrayList<String> getListResultado(int indice){
        ArrayList<String> listResultado = new ArrayList<String>();
        int contReng = 0;
        int totReg = nVariables + indice;

        for(int i = nVariables + nFunciones;i < tabla.length;i++)
        {
            if(contReng == (nVariables + nFunciones)){
                contReng = 0;
            }
            if(contReng == totReg -1){
                listResultado.add(tabla[i]);
            }
            contReng ++;
        }

        return listResultado;
    }

    public  ArrayList<ArrayList<String>> getFormatTKarnaugh(ArrayList<String> lResultados,ArrayList<String> columnas,int nRenglones,int nCol){
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
}
