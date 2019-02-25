package com.example.usuario.dinamicview;

import java.util.ArrayList;

/**
 * Clase encarcada de guardar los enlaces para hacer la simulacion en los mapas de Karnaugh
 * Created by Yair on 23/10/2016.
 */

public class CEnlace {
    ArrayList<String> enlace;

    public CEnlace(){
        enlace = new ArrayList<String>();
    }

    public void AgregarEnlace(String cad){
        enlace.add(cad);
    }

    public ArrayList<String> getEnlace(){
        return enlace;
    }
}
