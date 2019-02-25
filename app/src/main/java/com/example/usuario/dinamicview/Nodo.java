package com.example.usuario.dinamicview;

import android.app.Application;
import android.content.Context;
import android.graphics.Canvas;

import java.io.Serializable;

/**
     *
     * @author  José Luis García Guerrero
     * @version 1.0
     * @since   2016-09-20
     */
public class Nodo implements Serializable {
    /**
     * Valor booleano que va a tomar el nodo al ser evaluado
      */
    private boolean valor;
    /**
     * Hijo derecho del nodo del árbol
     */
    private Nodo hDer;
    /**
     * Hijo izquierdo del nodo del árbol
     */
    private Nodo hIzq;
    /**
     * Número de columna en la que debe dibujarse el nodo en el circuito
     */
    private int columna;
    /**
     * Número de renglon en el que debe dibujarse el nodo en el circuito
     */
    private int renglon;

    private Compuerta compuerta;

    private Conector conIzq;

    private Conector conDer;

    /**
     * Constructor de la clase Nodo
     */
    public Nodo()
    {
        this.hDer = null;
        this.hIzq = null;
        this.columna = -1;
        this.renglon = -1;
        this.compuerta = null;
        this.conIzq = null;
        this.conDer = null;
    }

    /**
     * Establece el valor del nodo del árbol
     * @param   unValor     el nuevo valor que va a aser asignado al nodo
     */
    public void setValor(boolean unValor) { this.valor = unValor; }

    /**
     * Establece el hijo derecho del nodo del árbol
     * @param   unhDer      el nuevo hijo derecho del nodo
     */
    public void sethDer(Nodo unhDer) { this.hDer = unhDer; }

    /**
     * Establece el hijo izquierdo del nodo del árbol
     * @param   unhIzq      el nuevo hijo izquierdo del nodo
     */
    public void sethIzq(Nodo unhIzq) { this.hIzq = unhIzq; }

    /**
     * Establece la columna en la que se posiciona el nodo
     * @param noColumna     el nuevo valor de la columna del nodo
     */
    public void setColumna(int noColumna) { this.columna = noColumna; }

    /**
     * Establece el renglon en el que se posiciona el nodo
     * @param noRenglon     el nuevo valor del renglon del nodo
     */
    public void setRenglon(int noRenglon) { this.renglon = noRenglon; }

    /**
     * Obtiene el valor del nodo del árbol
     * @return              devuelve el valor del nodo
     */
    public boolean getValor() { return this.valor; }

    /**
     * Obtiene el hijo derecho del nodo del árbol
     * @return              devuelve el nodo hijo de la derecha de este nodo
     */
    public Nodo gethDer() { return this.hDer; }

    /**
     * Obtiene el hijo izquierdo del nodo del árbol
     * @return              devuelve el nodo hijo de la izquierda de este nodo
     */
    public Nodo gethIzq() { return this.hIzq; }

    /**
     * Obtiene el valor de la columna en la que se posiciona el nodo
     * @return              devuelve el valor de la columna en que se posiciona este nodo
     */
    public int getColumna() { return this.columna; }

    /**
     * Obtiene el valor del renglon en el que se posiciona el nodo
     * @return              devuelve el valor del renglon en que se posiciona este nodo
     */
    public int getRenglon() { return this.renglon; }

    public Compuerta getCompuerta(){ return this.compuerta; }

    public void setCompuerta(Compuerta unaCompuerta) { this.compuerta = unaCompuerta; }

    public void draw(Canvas canvas, Context contexto, int tamCuadroAncho, int tamCuadroAlto, int numColumnas)
    {
        Compuerta compuerta = null;
        if(((Token)this).getTipo() == TipoToken.AND)
        {
            compuerta = new CompuertaAND(contexto);
            compuerta.calculaCoordenadas(numColumnas, this.columna, this.renglon, tamCuadroAncho, tamCuadroAlto);
        }
        else if(((Token)this).getTipo() == TipoToken.OR)
        {
            compuerta = new CompuertaOR(contexto);
            compuerta.calculaCoordenadas(numColumnas, this.columna, this.renglon, tamCuadroAncho, tamCuadroAlto);
        }
        else if(((Token)this).getTipo() == TipoToken.NOT)
        {
            compuerta = new CompuertaNOT(contexto);
            compuerta.calculaCoordenadas(numColumnas, this.columna, this.renglon, tamCuadroAncho, tamCuadroAlto);
        }
        if(compuerta !=  null)
        {
            compuerta.draw(canvas);
            this.compuerta = compuerta;
        }
    }
}
