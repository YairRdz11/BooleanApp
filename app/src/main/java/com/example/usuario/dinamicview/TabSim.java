package com.example.usuario.dinamicview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 *
 * @author  José Luis García Guerrero
 * @version 1.0
 * @since   2016-09-13
 */
public class TabSim implements Serializable
{
    private ArrayList<Simbolo> tabla;
    private TipoConjunto tipoConjunto;

    /**
     * Construye una nueva Tabla de Simbolos
     */
    public TabSim()
    {
        this.tabla = new ArrayList<Simbolo>();
        this.tipoConjunto = TipoConjunto.NULO;
    }

    public void setTipoConjunto(TipoConjunto tipo)
    {
        this.tipoConjunto = tipo;
    }

    public  ArrayList<Simbolo> getTabla(){return this.tabla;}

    public TipoConjunto getTipoConjunto()
    {
        return this.tipoConjunto;
    }

    /**
     * Agrega un nuevo Simbolo a la tabla de Simbolos
     * @param	nombre	es el nuevo nombre del Simbolo a agregar
     * @param	valor	es el nuevo valor del Simbolo a agregar
     * @return			true si el Simbolo fue agregado correctamente
     */
    public boolean agregaSimbolo(String nombre, boolean valor)
    {
        boolean bandAgregado = false;

        if(this.buscaSimbolo(nombre) == null)
        {
            Simbolo nuevo = new Simbolo();
            nuevo.setNombre(nombre);
            nuevo.setValor(valor);
            this.tabla.add(nuevo);
            bandAgregado = true;
        }
        return bandAgregado;
    }

    /**
     * Agrega un nuevo Simbolo a la tabla de de Simbolos
     * @param	sAgregar	es el Simbolo nuevo a agregar
     * @return				true si el Simbolo fue agregado correctamente
     */
    public boolean agregaSimbolo(Simbolo sAgregar)
    {
        boolean bandAgregado = false;

        if(this.buscaSimbolo(sAgregar.getNombre()) == null)
        {
            this.tabla.add(sAgregar);
            bandAgregado = true;
        }
        return bandAgregado;
    }

    /**
     * Busca un Simbolo dentro de la tabla de Simbolos
     * @param	nombre	indica el nombre del simbolo que sera buscado
     * @return			null si el nombre no corresponde a ningun Simbolo
     */
    public Simbolo buscaSimbolo(String nombre)
    {
        Iterator<Simbolo> buscador = this.tabla.iterator();
        Simbolo encontrado = null;
        boolean band = false;

        while(buscador.hasNext())
        {
            encontrado = buscador.next();
            if(encontrado.getNombre().equals(nombre))
            {
                band = true;
                break;
            }
        }
        if(!band)
            encontrado = null;
        return encontrado;
    }

    /**
     * Elimina un Simbolo de la tabla de Simbolos
     * @param	nombre	es el nombre del Simbolo que sera eliminado de la tabla de Simbolos
     * @return			true si el Simbolo buscado fue encontrado y eliminado
     */
    public boolean eliminaSimbolo(String nombre)
    {
        boolean bandEliminado = false;
        Simbolo sEliminar = this.buscaSimbolo(nombre);

        if(sEliminar != null)
        {
            this.tabla.remove(sEliminar);
            bandEliminado = true;
        }
        return bandEliminado;
    }

    /**
     * Elimina un Simbolo de la tabla de Simbolos
     * @param    sEliminar	es Simbolo que sera eliminado de la tabla de Simbolos
     * @return				true si el Simbolo buscado fue encontrado y eliminado
     */
    public boolean eliminaSimbolo(Simbolo sEliminar)
    {
        boolean bandEliminado = false;

        if(this.tabla.contains(sEliminar))
        {
            this.tabla.remove(sEliminar);
            bandEliminado = true;
        }
        return bandEliminado;
    }

    /**
     * Cambia el valor de un Simbolo
     * @param	nombre	es el nombre del Simbolo a cambiar
     * @param	valor	es el valor que sera asignado al Simbolo
     * @return			true si el valor fue cambiado correctamente
     */
    public boolean asignaValorSimbolo(String nombre, boolean valor)
    {
        Simbolo s = buscaSimbolo(nombre);
        if(s != null)
        {
            s.setValor(valor);
        }
        return false;
    }

    /**
     * Entrega el número de símbolos que contiene la tabla
     * @return          un entero que indica la cantidad de símbolos insertados
     */
    public int numeroSimbolos(){
        return this.tabla.size();
    }

    /**
     * Entrega la representación de este Simbolo en una cadena
     * @return			una cadena con la representación interna de este Simbolo
     */
    @Override
    public String toString()
    {
        Iterator<Simbolo> buscador = this.tabla.iterator();
        Simbolo encontrado = null;
        String tablaString = "";

        while(buscador.hasNext())
        {
            encontrado = buscador.next();
            tablaString += encontrado.getNombre() + "\t" + encontrado.getValor() + "\n";
        }
        return tablaString;
    }

    public void asignaValoresSimbolos(String valores)
    {
        if(this.tipoConjunto == TipoConjunto.ABCDE)
            this.asignaValoresConjuntoABCDE(valores);
        else if(this.tipoConjunto == TipoConjunto.VWXYZ)
            this.asignaValoresConjuntoVWXYZ(valores);

    }

    private void asignaValoresConjuntoABCDE(String valores)
    {
        int index = 0;

        index = this.insertaValorEnTabla("a", index, valores);
        index = this.insertaValorEnTabla("b", index, valores);
        index = this.insertaValorEnTabla("c", index, valores);
        index = this.insertaValorEnTabla("d", index, valores);
        index = this.insertaValorEnTabla("e", index, valores);
    }

    private void asignaValoresConjuntoVWXYZ(String valores)
    {
        int index = 0;

        index = this.insertaValorEnTabla("v", index, valores);
        index = this.insertaValorEnTabla("w", index, valores);
        index = this.insertaValorEnTabla("x", index, valores);
        index = this.insertaValorEnTabla("y", index, valores);
        index = this.insertaValorEnTabla("z", index, valores);
    }

    private int insertaValorEnTabla(String simbolo, int index, String valores)
    {
        if(this.buscaSimbolo(simbolo) != null){
            this.asignaValorSimbolo(simbolo, valores.charAt(index) == '0' ? false : true );
            index++;
        }

        return index;
    }

    public void generaSimbolos(int noVariables)
    {
        for(int i = 0; i < noVariables; i++)
        {
            Simbolo nuevo = new Simbolo();
            switch(i)
            {
                case 0:
                    nuevo.setNombre("a");
                    break;
                case 1:
                    nuevo.setNombre("b");
                    break;
                case 2:
                    nuevo.setNombre("c");
                    break;
                case 3:
                    nuevo.setNombre("d");
                    break;
                case 4:
                    nuevo.setNombre("e");
                    break;
            }
            this.tabla.add(nuevo);
        }
    }

    public String obtenSimbolos()
    {
        String aux = "";

        for(Simbolo s : this.tabla)
        {
            aux += s.getNombre();
        }
        char[] simbs = aux.toCharArray();
        Arrays.sort(simbs);
        return new String(simbs);
    }


    /**
     * Devuelve un simbolo de la posición especificada
     * @param indice    es una posición de la tabla
     * @return      null si el simbolo no fue encontrado
     */
    public Simbolo obtenieneSimbolo(int indice){
        return !this.tabla.isEmpty() || indice >= 0 && indice < this.tabla.size() ? this.tabla.get(indice) : null;
    }

    public void sort()
    {
        Collections.sort(this.tabla);
    }
}