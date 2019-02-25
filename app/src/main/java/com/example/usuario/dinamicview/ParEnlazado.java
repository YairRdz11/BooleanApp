package com.example.usuario.dinamicview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * Integra las combinaciones posibles de posiciones en un mapa de Karnaugh
 * Created by hugo on 10/23/16.
 * version: 1.0
 * since: 2016-10-23
 * author: Hugo Armando Rodríguez Flores
 */
public class ParEnlazado implements Serializable{
    /**
     * Sirve para poder aplicar la operación lógica XOR posteriormente a solo bits validos
     */
    private Integer mascaraBitsValidos;
    /**
     * Coleccion que guarda todos los miniterminos que podrian formar un enlace de un mapa de Karnaugh
     */
    private ArrayList<Integer> enlace;
    /**
     * Funciona para marcar a este par en caso de que sea implicante
     */
    private boolean esImplicante;
    /**
     * Referencia a la tabla de simbolos usada en el conocimiento de la ecuacion resultante
     */
    private TabSim tablaSimbolos;

    /**
     * Construye un nuevo par enlazado
     */
    public ParEnlazado(){
        this.mascaraBitsValidos = 0x3F;
        this.enlace = new ArrayList<Integer>();
        this.esImplicante = true;
        this.tablaSimbolos = null;
    }

    /**
     * Construye un nuevo par enlazado que recibe el primer elemento del par
     * @param unElemento    el primer el elemento entero del enlace
     */
    public ParEnlazado(Integer unElemento){
        this.mascaraBitsValidos = 0x3F;
        this.enlace = new ArrayList<Integer>();
        this.enlace.add(unElemento);
        this.esImplicante = true;
        this.tablaSimbolos = null;
    }

    /**
     * Agregar una posicion ordenada nueva a este par
     * @param unElemento     es la posición que sera marcada como posible adyacencia en un mapa de Karnaugh
     */
    public void agregaPosicion(Integer unElemento){
        int i = this.enlace.size();

        for (; i > 0 && unElemento < this.enlace.get(i - 1); i--);
        this.enlace.add(i, unElemento);
    }

    /**
     * Conjunta otro par con este
     * @param otro  el otro ParEnlazado con el que quiere hacerse una union
     */
    public void agregaParEnlazado(ParEnlazado otro){
        for(Integer minitermino : otro.enlace)
            this.agregaPosicion(minitermino);
    }

    /**
     * Obtiene la colección de miniterminos que podrian marcar un enlace en el mapa de Karnaugh
     * @return  un ArrayList<Integer> que contiene posiciones de un mapa de Karnaugh
     */
    public ArrayList<Integer> getEnlace(){
        return this.enlace;
    }

    /**
     * Obtiene el primer elemento de este ParEnlazado
     * @return  un entero con la primera posición dentro del enlace
     */
    public Integer getPrimerElemento(){
        return !this.enlace.isEmpty() ? this.enlace.get(0) : null;
    }

    /**
     *
     * @param unaTablaSimbolos
     */
    public void setTablaSimbolos(TabSim unaTablaSimbolos){
        this.tablaSimbolos = unaTablaSimbolos;
    }

    /**
     * Establece la mascara de bits usada para determinar este enlace
     * @param unaMascara    un entero resultado de una operacion XNOR
     */
    public void setMascaraBitsValidos(Integer unaMascara){
        this.mascaraBitsValidos = unaMascara;
    }

    /**
     * Obtiene la mascara de bits que hace unico a este enlace
     * @return  un entero con la mascara de bits validos
     */
    public Integer getMascaraBitsValidos() {
        return this.mascaraBitsValidos;
    }

    /**
     * Marca a este ParEnlazado como implicante o no
     * @param implicante    un booleano que indica si este par es implicante o no
     */
    public void setEsImplicante(boolean implicante){
        this.esImplicante = implicante;
    }

    /**
     * Obtiene la bandera que indica si este ParEnlazado es implicante o no
     * @return      un booleano que indica si este par es implicante o no
     */
    public boolean getEsImplicante(){
        return this.esImplicante;
    }

    /**
     * Devuelve un producto de literales validas
     * @return  una cadena con la ecuación de literales validos
     */
    public String getEcuacionLiteral(){
        int numSimbolos = this.tablaSimbolos.numeroSimbolos();
        int primerElemento = this.enlace.get(0);
        String ecuacion = "";

        for(int i = numSimbolos - 1; i >= 0; i--)
            if (((this.mascaraBitsValidos >> i) & 0x01) == 0x01){
                ecuacion += this.tablaSimbolos.obtenieneSimbolo(numSimbolos - 1 - i).getNombre();
                if ((((primerElemento & this.mascaraBitsValidos) >> i) & 0x01) != 0x01)
                    ecuacion += "'";
            }
        return ecuacion;
    }

    /**
     * Devuelve la cantidad de bits positivos
     * @return  un entero con el numero de bits en 1 que son validos
     */
    public int getNumeroPositivos(){
        int primerElemento = this.enlace.get(0);
        int positivos = 0;

        for(int i = this.tablaSimbolos.numeroSimbolos() - 1; i >= 0; i--)
            if (((this.mascaraBitsValidos >> i) & 0x01) == 0x01)
                if ((((primerElemento & this.mascaraBitsValidos) >> i) & 0x01) == 0x01)
                    positivos++;
        return positivos;
    }

    /**
     * Verifica si este enlace contiene la posición buscada
     * @param unElemento    el elemento que va a ser buscando dentro de este enlace
     * @return      true si el elemento esta presente en el enlace
     */
    public boolean contiene(Integer unElemento){
        return this.enlace.contains(unElemento);
    }

    /**
     * Obtiene la representación interna de este enlace
     * @return  la cadena con la información interna de este enlace
     */
    @Override
    public String toString(){
        //////Valores usados para mostrar en debug
        //String numBinario = Integer.toBinaryString(this.mascaraBitsValidos & 0x1F).replace(' ', '0');
        //String representacion = String.format("Mascara: %5s Enlace: {", numBinario);
        String representacion = "{";
        for (int i = 0; i < this.enlace.size(); i++){
            representacion += this.enlace.get(i).toString();
            if (i < this.enlace.size() - 1)
                representacion += ", ";
        }
        representacion += '}';
        return representacion;
    }

    /**
     * Compara dos pares para verificar que contengan los mismos elementos
     * @param o     el otro ParEnlazado que sera comparado contra este
     * @return      true si los contenidos de los enlaces son iguales
     */
    @Override
    public boolean equals(Object o) {
        ParEnlazado otro;
        Iterator<Integer> itElemento;
        Iterator<Integer> itElementoOtro;
        boolean res = false;

        if (o instanceof ParEnlazado){
            otro = (ParEnlazado)o;
            res = true;
            itElemento = this.enlace.iterator();
            itElementoOtro = otro.enlace.iterator();
            while(itElemento.hasNext() && res)      //se asume que la comparacion sera realizada en enlaces del mismo tamaño
                res = itElemento.next().intValue() == itElementoOtro.next().intValue();
        }
        return res;
    }
}
