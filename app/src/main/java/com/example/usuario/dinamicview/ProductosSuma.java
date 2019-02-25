package com.example.usuario.dinamicview;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * Funciona como un producto de sumas (algebra booleana), contiene la forma (x + y)(w + zx)
 * version: 1.0
 * since: 2016-11-9
 * author: Hugo Armando Rodríguez Flores
 */
public class ProductosSuma {
    /**
     * Cada elemento de esta coleccion es un producto y entre todos forman las sumas
     */
    private ArrayList<Producto> sumas;

    /**
     * Construye un nuevo productos de suma
     */
    public ProductosSuma(){
        this.sumas = new ArrayList<Producto>();
    }

    /**
     * Agrega un Producto a la coleccion de sumas
     * @param unProducto    un producto que formará parte de las sumas
     */
    public void agregaProducto(Producto unProducto){
        if (!this.contiene(unProducto))
            this.sumas.add(unProducto);
    }

    /**
     * Devuelve la longitud de las sumas
     * @return      un entero con la longitud de las sumas
     */
    public int obtieneTamano(){
        return this.sumas.size();
    }

    /**
     * Devuelve la longitud de cada uno de los productos
     * @return      un entero con la longitud de cada uno de los productos
     */
    public int numeroProductos(){
        int posiciones = 0;

        for(Producto producto : this.sumas)
            posiciones += producto.obtieneTamano();
        return posiciones;
    }

    /**
     * Verifica que exista el elemento especificado en la coleccion de sumas
     * @param unProducto     es el elemento a verificar que exista dentro de la colección
     * @return      true si el elemento esta contenido en las sumas
     */
    public boolean contiene(Producto unProducto){
        Iterator<Producto> itProducto;
        boolean res = false;

        itProducto = this.sumas.iterator();
        while(itProducto.hasNext() && !res)
            res = itProducto.next().equals(unProducto);
        return res;
    }

    /**
     * Encuentra el enlace común entre dos productos de suma
     * @param unOtroProducto    es el elemento con el que se va a buscar un factor común
     * @return      null si entre dos productos no existe un factor común
     */
    public Producto encuentraFactor(ProductosSuma unOtroProducto){
        Producto terminoComun = null;

        for(Iterator<Producto> itFactor = this.sumas.iterator(); itFactor.hasNext() && terminoComun == null; ){
            Producto factor = itFactor.next();
            for(Iterator<Producto> itOtro = unOtroProducto.sumas.iterator(); itOtro.hasNext() && terminoComun == null; )
                if (factor.equals(itOtro.next()))
                    terminoComun = factor;
        }
        return terminoComun;
    }

    /**
     * Factoriza y/o distribuye los terminos de dos productos de suma
     * @param unProductoComun   el producto que es común entre ambos terminos, null para solo aplicar la distribución
     * @param unOtroProducto    el otro producto con el que se va a aplicar la distribución
     * @return      un nuevo ProductosSuma con el resutlado de la factorización y/o distribución
     */
    public ProductosSuma factoriza(Producto unProductoComun, ProductosSuma unOtroProducto){
        ProductosSuma factorizado = new ProductosSuma();

        if (unProductoComun != null){
            factorizado.agregaProducto(unProductoComun);
            this.sumas.remove(unProductoComun);
            unOtroProducto.sumas.remove(unProductoComun);
        }
        for (Producto prodFactor : this.sumas)
            for (Producto prodOtro : unOtroProducto.sumas){
                Producto nuevoProducto = prodFactor.mezclaProducto(prodOtro);
                factorizado.agregaProducto(nuevoProducto);
        }
        return factorizado;
    }

    /**
     * Aplica una serie de reglas para encontrar el producto más optimo
     * @return  el producto más optimo (o el único) perteneciente a este ProductosSuma
     */
    public Producto productoOptima(){
        ProductosSuma optimaAnterior;
        ProductosSuma optimaPresente;
        Producto optimo = null;

        optimaAnterior = this;
        optimaPresente = new ProductosSuma();
        optimaPresente.agregaProducto(optimaAnterior.sumas.get(0));
        for (ListIterator<Producto> itProducto = this.sumas.listIterator(1); itProducto.hasNext(); ){
            Producto sigProducto = itProducto.next();
            Producto productoMenor = optimaPresente.sumas.get(0);
            if (sigProducto.obtieneTamano() == productoMenor.obtieneTamano())
                optimaPresente.agregaProducto(sigProducto);
            else if (sigProducto.obtieneTamano() < productoMenor.obtieneTamano()){
                optimaPresente = new ProductosSuma();
                optimaPresente.agregaProducto(sigProducto);
            }
        }
        optimo = optimaPresente.sumas.get(0);
        if (optimaPresente.sumas.size() > 1) {
            optimaAnterior = optimaPresente;
            optimaPresente = new ProductosSuma();
            optimaPresente.agregaProducto(optimaAnterior.sumas.get(0));
            for (ListIterator<Producto> itProducto = optimaAnterior.sumas.listIterator(1); itProducto.hasNext(); ) {
                Producto sigProducto = itProducto.next();
                Producto productoMayor = optimaPresente.sumas.get(0);
                if (sigProducto.numeroPosiciones() == productoMayor.numeroPosiciones())
                    optimaPresente.agregaProducto(sigProducto);
                else if (sigProducto.numeroPosiciones() > productoMayor.numeroPosiciones()) {
                    optimaPresente = new ProductosSuma();
                    optimaPresente.agregaProducto(sigProducto);
                }
            }
            optimo = optimaPresente.sumas.get(0);
            if (optimaPresente.sumas.size() > 1){
                optimaAnterior = optimaPresente;
                for (ListIterator<Producto> itProducto = optimaAnterior.sumas.listIterator(1); itProducto.hasNext(); ) {
                    Producto sigProducto = itProducto.next();
                    if (optimo.getNumeroPositivos() < sigProducto.getNumeroPositivos())
                        optimo = sigProducto;
                }
            }
        }
        return optimo;
    }

    /**
     * Devuelve la representación interna de este objeto
     * @return      una cadena con la información interna de este objeto
     */
    @Override
    public String toString() {
        Iterator<Producto> itElemento;
        String representacion = "(";

        itElemento = this.sumas.iterator();
        while(itElemento.hasNext()){
            representacion += itElemento.next().toString();
            if (itElemento.hasNext())
                representacion += " + ";
        }
        representacion += ")";
        return representacion;
    }

    /**
     * Verifica que dos productos de suma contengan los mismos elementos
     * @param o     el otro producto de suma con el que sera comparado
     * @return      true si ambos productos de suma tienen los mismos elementos
     */
    @Override
    public boolean equals(Object o) {
        Iterator<Producto> itProducto;
        ProductosSuma otro;
        boolean res = false;

        if (o instanceof ProductosSuma){
            otro = (ProductosSuma)o;
            res = this.sumas.size() == otro.sumas.size() && this.numeroProductos() == otro.numeroProductos();
            itProducto = this.sumas.iterator();
            while(itProducto.hasNext() && res)
                res = otro.contiene(itProducto.next());
        }
        return res;
    }
}
