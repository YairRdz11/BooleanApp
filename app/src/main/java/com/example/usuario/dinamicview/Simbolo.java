package com.example.usuario.dinamicview;

import java.io.Serializable;

/**

 * @author  José Luis García Guerrero
 * @version 1.0
 * @since   2016-09-13
 */
public class Simbolo implements Serializable, Comparable<Simbolo>
{
    /**
     * Nombre del Simbolo
     */
    private String nombre;
    /**
     * Valor del Simbolo
     */
    private boolean valor;

    private Variable variable;

    /**
     * Construye un nuevo Simbolo
     */
    public Simbolo()
    {
        this.nombre = "";
        this.valor = false;
        this.variable = null;
    }

    /**
     * Obtiene el nombre de este Simbolo
     * @return       el nombre de este Simbolo
     */
    public String getNombre()
    {
        return this.nombre;
    }

    /**
     * Obtiene el valor de este Simbolo
     * @return       el valor de este Simbolo
     */
    public boolean getValor()
    {
        return this.valor;
    }

    /**
     * Establece un nombre para este Simbolo
     * @param    unNombre   es el nombre nuevo que sera establecido
     */
    public void setNombre(String unNombre)
    {
        this.nombre = unNombre;
    }

    /**
     * Establece un valor para este Simbolo
     * @param    unValor    es el nuevo valor que sera establecido
     */
    public void setValor(boolean unValor)
    {
        this.valor = unValor;
    }

    public Variable getVariable()
    {
        return this.variable;
    }

    public void setVariable(Variable variable)
    {
        this.variable = variable;
    }

    @Override
    public int compareTo(Simbolo aComparar)
    {
        return this.nombre.compareTo(aComparar.nombre);
    }
}