package com.example.usuario.dinamicview;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Funciona como un producto (en el algebra booleana). Indica cuales enlaces son los que estan funcionando como elementos de una multiplicación
 * version: 1.0
 * since: 2016-11-7
 * author: Hugo Armando Rodríguez Flores
 */
public class Producto {
    /**
     * Guarda las referencias de multiples enlaces para convertirlas en un producto
     */
    private ArrayList<ParEnlazado> productosEnlaces;

    /**
     * Construye un nuevo producto vacio
     */
    public Producto(){
        this.productosEnlaces = new ArrayList<ParEnlazado>();
    }

    /**
     * Construye un nuevo producto y agrega el primer elemento
     * @param unEnlace
     */
    public Producto(ParEnlazado unEnlace){
        this.productosEnlaces = new ArrayList<ParEnlazado>();
        this.productosEnlaces.add(unEnlace);
    }

    /**
     * Agrega un nuevo elemento al producto
     * @param unEnlace  es el elemento a ser agregado
     */
    public void agregaEnlace(ParEnlazado unEnlace){
        this.productosEnlaces.add(unEnlace);
    }

    /**
     * Mezcla dos productos para formar uno nuevo (Ley distributiva del álgebra booleana)
     * @param unProducto    el otro producto con el que sera mezclado este
     * @return      un nuevo producto con la mezcla de este y el elemento dado
     */
    public Producto mezclaProducto(Producto unProducto){
        Producto prod;

        prod = new Producto();
        for (ParEnlazado enlace : this.productosEnlaces)
            prod.agregaEnlace(enlace);
        for (ParEnlazado enlace : unProducto.productosEnlaces)
            if (!prod.contiene(enlace))
                prod.agregaEnlace(enlace);
        return prod;
    }

    /**
     * Obtiene la lista de elementos del producto
     * @return      una lista con todos los elementos contenidos en el producto
     */
    public ArrayList<ParEnlazado> getProductosEnlaces(){
        return this.productosEnlaces;
    }

    /**
     * Obtiene el primer elemento de los productos
     * @return  un ParEnlazado que es el primer producto del producto
     */
    public ParEnlazado getPrimerElemento(){
        return !this.getProductosEnlaces().isEmpty() ? this.productosEnlaces.get(0) : null;
    }

    /**
     * Devuelve la longitud del producto
     * @return      un entero con el valor de la longitud de elementos en el producto
     */
    public int obtieneTamano(){
        return this.productosEnlaces.size();
    }

    /**
     * Devuelve el total de posiciones entre todos los enlaces del producto
     * @return      un entero con el total de posiciones
     */
    public int numeroPosiciones(){
        int posiciones = 0;

        for(ParEnlazado enlace : this.productosEnlaces)
            posiciones += enlace.getEnlace().size();
        return posiciones;
    }

    /**
     * Devuelve el total de bits en 1 validos de este Producto
     * @return  un entero con el total de bits validos resultado de la suma de enlaces
     */
    public int getNumeroPositivos(){
        int positivos = 0;

        for(ParEnlazado enlace : this.productosEnlaces)
            positivos += enlace.getNumeroPositivos();
        return positivos;
    }

    /**
     * Verifica que un elemento dado este dentro del producto
     * @param unEnlace  un elemento a ser buscado
     * @return      true si el elemento se encuentra en el producto
     */
    public boolean contiene(ParEnlazado unEnlace){
        Iterator<ParEnlazado> itEnlace;
        boolean res = false;

        itEnlace = this.productosEnlaces.iterator();
        while(itEnlace.hasNext() && !res)
            res = itEnlace.next().equals(unEnlace);
        return res;
    }

    /**
     * Obtiene la representación interna de este producto
     * @return  la cadena con la información interna de este producto
     */
    @Override
    public String toString() {
        String representacion = "";

        for(ParEnlazado enlace : this.productosEnlaces)
            representacion += enlace.toString();
        return representacion;
    }


    /**
     * Compara dos Productos para verificar que se contengan los mismos elementos
     * @param o     el otro Producto que sera comparado contra este
     * @return      true si ambos productos son contienen los mismos elementos
     */
    @Override
    public boolean equals(Object o) {
        Producto otro;
        Iterator<ParEnlazado> itEnlace;
        boolean res = false;

        if (o instanceof Producto){
            otro = (Producto)o;
            res = this.productosEnlaces.size() == otro.productosEnlaces.size() && this.numeroPosiciones() == otro.numeroPosiciones();
            itEnlace = this.productosEnlaces.iterator();
            while(itEnlace.hasNext() && res)
                res = otro.contiene(itEnlace.next());
        }
        return res;
    }
}
