package com.example.usuario.dinamicview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Stack;

/**
 *
 * @author  José Luis García Guerrero
 * @version 1.0
 * @since   2016-09-20
 */
public class Arbol implements Serializable {
    /**
     * Pila que guarda los nodos que se van creando de abajo hacia arriba del árbol
     */
    private Stack<Nodo> pilaNodos;
    /**
     * Nodo que apunta a la raiz del árbol
     */
    private Nodo raiz;

    private int columnas;
    private int renglones;


    /**
     * Constructor de la clase árbol
     */
    public Arbol()
    {
        this.pilaNodos = new Stack<Nodo>();
        this.raiz = null;
        this.columnas = 0;
        this.renglones = 1;
    }

    /**
     * Se obtiene la pila de nodos del árbol
     * @return          devuelve la pila con los nodos creados hasta el momento
     */
    public Stack<Nodo> getPilaNodos() { return this.pilaNodos; }

    /**
     * Se obtiene la raíz del árbol
     * @return          devuelve el nodo raíz del árbol
     */
    public Nodo getRaiz() { return this.raiz; }

    /**
     * Establece la raíz del árbol
     * @param unaRaiz   Nodo que se asigna como raíz del árbol
     */
    public void setRaiz(Nodo unaRaiz) { this.raiz = unaRaiz; }

    public int getColumnas() {
        return columnas;
    }

    public int getRenglones() {
        return renglones;
    }

    /**
     * Se obtiene el nodo de la cima de la pila y se asigna como raíz del árbol
     */
    public void asignaRaiz()
    {
        if(this.pilaNodos.size() > 0) {
            this.raiz = this.pilaNodos.pop();
        }
    }

    /**
     * Se realiza la manipulación de un nodo con respecto al árbol, dependiendo del tipo de elemento
     * que sea, un ID, un operador binario o un operador unario
     * @param   unNodo  Nodo que va a ser insertado en el árbol
     */
    public void insertaEnArbol(Nodo unNodo) throws Exception
    {
        switch(((Token)unNodo).getTipo())
        {
            case ID:
                this.insertaEnPilaNodos(unNodo);
                break;
            case AND:
            case OR:
                this.construyeArbolBinario(unNodo);
                break;
            case NOT:
                this.construyeArbolUnario(unNodo);
                break;
        }
    }

    /**
     * Se realiza la inserción de un nodo en la pila de nodos del árbol
     * @param   unNodo  Nodo que se inserta en la pila del árbol
     */
    private void insertaEnPilaNodos(Nodo unNodo)
    {
        this.pilaNodos.push(unNodo);
    }

    /**
     * Se realiza la construcción de un árbol con dos hijos, obteniendo los dos elementos mas a la cima de la pila,
     * y se asignan como hijos del nodo nuevo que es insertado en la cima de la pila.
     * @param unNodo
     */
    private void construyeArbolBinario(Nodo unNodo)
    {
        if(!this.pilaNodos.empty()) {
            if(this.pilaNodos.size() > 1) {
                Nodo izq = this.pilaNodos.pop();
                unNodo.sethIzq(izq);
                Nodo der = this.pilaNodos.pop();
                unNodo.sethDer(der);
                this.insertaEnPilaNodos(unNodo);
            }
        }
    }

    /**
     * Se realiza la construcción de un árbol con un hijo, obteniendo el elemento en la cima de la pila, y
     * se asigna al hijo izquierdo del nodo nuevo que es insertado en la cima de la pila.
     * @param unNodo
     */
    private void construyeArbolUnario(Nodo unNodo)
    {
        if(!this.pilaNodos.empty()) {
            if(this.pilaNodos.size() > 0) {
                Nodo hijo = this.pilaNodos.pop();
                unNodo.sethIzq(hijo);
                this.insertaEnPilaNodos(unNodo);
            }
        }
    }

    /**
     * Se inicia el recorrido del árbol a partir de la raíz
     * @param tablaSimbolos     Tabla de simbolos de la que se obtendrán los valores asociados a los simbolos
     * @return                  Devuelve el valor booleano de la evaluación del árbol
     */
    public boolean evaluaArbol(TabSim tablaSimbolos)
    {
        boolean valor = false;

        this.recorreArbol(this.raiz, tablaSimbolos);
        valor = this.raiz.getValor();

        return valor;
    }

    /**
     * Se realiza el recorrido del árbol recursivamente empezando de izquierda a derecha y al regresar
     * de la llamada recursiva asigna el valor booleano del nodo
     * @param   raiz            Nodo que se toma como raíz para recorrer el árbol
     * @param   tablaSimbolos   Tabla de simbolos para tomar el valor asociado al simbolo de la hoja del árbol
     */
    private void recorreArbol(Nodo raiz, TabSim tablaSimbolos)
    {
        if(raiz != null) {
            TipoToken tipo = ((Token) raiz).getTipo();
            String variable = ((Token) raiz).getLexema();
            if (tipo == TipoToken.ID) {
                Simbolo simb = tablaSimbolos.buscaSimbolo(variable);
                raiz.setValor(simb.getValor());
            } else if (tipo == TipoToken.AND) {
                this.recorreArbol(raiz.gethIzq(), tablaSimbolos);
                this.recorreArbol(raiz.gethDer(), tablaSimbolos);
                raiz.setValor(raiz.gethIzq().getValor() && raiz.gethDer().getValor());
            } else if (tipo == TipoToken.OR) {
                this.recorreArbol(raiz.gethIzq(), tablaSimbolos);
                this.recorreArbol(raiz.gethDer(), tablaSimbolos);
                raiz.setValor(raiz.gethIzq().getValor() || raiz.gethDer().getValor());
            } else if (tipo == TipoToken.NOT) {
                this.recorreArbol(raiz.gethIzq(), tablaSimbolos);
                raiz.setValor(!raiz.gethIzq().getValor());
            }
        }
    }

    public void asignaRenglonesColumnas()
    {
        this.asignaRenglonColumna(this.raiz, 0);
    }

    private void asignaRenglonColumna(Nodo raiz, int columnaAnterior)
    {
        TipoToken tipo = ((Token) raiz).getTipo();
        int columna = -1;

        if(tipo == TipoToken.AND || tipo == TipoToken.OR) {
            raiz.setColumna(columnaAnterior + 1);
            columna = raiz.getColumna();
            this.asignaRenglonColumna(raiz.gethIzq(), columna);
            raiz.setRenglon(this.renglones);
            this.renglones++;
            this.asignaRenglonColumna(raiz.gethDer(), columna);
        }
        else if(tipo == TipoToken.NOT) {
            raiz.setColumna(columnaAnterior + 1);
            columna = raiz.getColumna();
            this.asignaRenglonColumna(raiz.gethIzq(), columna);
            if(((Token)raiz.gethIzq()).getTipo() == TipoToken.AND || ((Token)raiz.gethIzq()).getTipo() == TipoToken.OR)
                raiz.setRenglon(this.renglones - 1);
            else
                raiz.setRenglon(this.renglones);
        }
        if(columna > this.columnas)
            this.columnas = columna;
    }

    public void draw(Canvas canvas, Context contexto, int tamCuadroAncho, int tamCuadroAlto)
    {
        this.dibujaNodo(this.raiz, canvas, contexto, tamCuadroAncho, tamCuadroAlto);
    }

    private void dibujaNodo(Nodo raiz, Canvas canvas, Context contexto, int tamCuadroAncho, int tamCuadroAlto)
    {
        TipoToken tipoToken = ((Token)raiz).getTipo();
        if(tipoToken == TipoToken.AND || tipoToken == TipoToken.OR)
        {
            dibujaNodo(raiz.gethIzq(), canvas, contexto, tamCuadroAncho, tamCuadroAlto);
            dibujaNodo(raiz.gethDer(), canvas, contexto, tamCuadroAncho, tamCuadroAlto);
            raiz.draw(canvas, contexto, tamCuadroAncho, tamCuadroAlto, this.columnas);
        }
        else if(tipoToken == TipoToken.NOT)
        {
            dibujaNodo(raiz.gethIzq(), canvas, contexto, tamCuadroAncho, tamCuadroAlto);
            raiz.draw(canvas, contexto, tamCuadroAncho, tamCuadroAlto, this.columnas);
        }
    }

    public void dibujaConectores(Canvas canvas, TabSim tablaSimbolos)
    {
        this.dibujaConector(canvas, this.raiz, tablaSimbolos);
        int xi = this.raiz.getCompuerta().getX() + raiz.getCompuerta().image.getHeight();
        int yi = this.raiz.getCompuerta().getY() + (raiz.getCompuerta().image.getHeight() / 2);
        int xf = xi + 50;
        int yf = yi;
        Conector f = new Conector(xi, yi, xf, yf);
        if(this.raiz.getValor())
            f.setColor(Color.RED);
        else
            f.setColor(Color.BLACK);
        f.draw(canvas);
    }

    private void dibujaConector(Canvas canvas, Nodo raiz, TabSim tablaSimbolos)
    {
        TipoToken tipoToken = ((Token)raiz).getTipo();
        if(tipoToken == TipoToken.AND || tipoToken == TipoToken.OR)
        {
            dibujaConector(canvas, raiz.gethIzq(), tablaSimbolos);
            dibujaConector(canvas, raiz.gethDer(), tablaSimbolos);
            paintConectores(canvas, raiz, tablaSimbolos);
        }
        else if(tipoToken == TipoToken.NOT)
        {
            dibujaConector(canvas, raiz.gethIzq(), tablaSimbolos);
            paintConectores(canvas, raiz, tablaSimbolos);
        }
    }

    private void paintConectores(Canvas canvas, Nodo raiz, TabSim tablaSimbolos)
    {
        TipoToken tipoToken = ((Token)raiz.gethIzq()).getTipo();
        int xi = 0;
        int yi = 0;
        int xf = 0;
        int yf = 0;

        if(tipoToken == TipoToken.ID)
        {
            xi = tablaSimbolos.buscaSimbolo(((Token) raiz.gethIzq()).getLexema()).getVariable().getX() + 3;
            xf = raiz.getCompuerta().getX() + 3;
            if(((Token)raiz).getTipo() == TipoToken.NOT)
            {
                yi = yf = raiz.getCompuerta().getY() + (raiz.getCompuerta().image.getHeight() / 2);
            }
            else {
                yi = yf = raiz.getCompuerta().getY() + (raiz.getCompuerta().image.getHeight() / 4);
            }
        }
        else
        {
            xi = raiz.gethIzq().getCompuerta().getX() + raiz.getCompuerta().image.getHeight();
            yi = raiz.gethIzq().getCompuerta().getY() + (raiz.getCompuerta().image.getHeight()/2);
            xf = raiz.getCompuerta().getX() + 3;
            yf = raiz.getCompuerta().getY() + (raiz.getCompuerta().image.getHeight() / 4);
        }
        Conector con1 = new Conector(xi, yi, xf, yf);
        if(raiz.gethIzq().getValor())
            con1.setColor(Color.RED);
        else
            con1.setColor(Color.BLACK);
        con1.draw(canvas);
        if(((Token)raiz).getTipo() == TipoToken.AND || ((Token)raiz).getTipo() == TipoToken.OR) {
            tipoToken = ((Token) raiz.gethDer()).getTipo();
            if (tipoToken == TipoToken.ID) {
                xi = tablaSimbolos.buscaSimbolo(((Token) raiz.gethDer()).getLexema()).getVariable().getX() + 3;
                yi = (raiz.getCompuerta().getY() + raiz.getCompuerta().image.getHeight()) - (raiz.getCompuerta().image.getHeight() / 4);
                xf = raiz.getCompuerta().getX() + 3;
                yf = (raiz.getCompuerta().getY() + raiz.getCompuerta().image.getHeight()) - (raiz.getCompuerta().image.getHeight() / 4);
            } else {
                xi = raiz.gethDer().getCompuerta().getX() + raiz.getCompuerta().image.getHeight();
                yi = raiz.gethDer().getCompuerta().getY() + (raiz.getCompuerta().image.getHeight() / 2);
                xf = raiz.getCompuerta().getX() + 3;
                yf = (raiz.getCompuerta().getY() + raiz.getCompuerta().image.getHeight()) - (raiz.getCompuerta().image.getHeight() / 4);
            }
            Conector con2 = new Conector(xi, yi, xf, yf);
            if (raiz.gethDer().getValor())
                con2.setColor(Color.RED);
            else
                con2.setColor(Color.BLACK);
            con2.draw(canvas);
        }
    }
}
