package com.example.usuario.dinamicview;

import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.content.Context;

/**
 *
 * @author  Hugo Armando Rodríguez Flores
 * @version 1.0
 * @since   2016-09-12
 */
public class AnalizadorSintactico{
    /**
     * Referencia al AnalizadorLexico
     */
    private AnalizadorLexico lexico;
    /**
     * Contiene las reglas para una tabla del análisis sintáctico por precedencia de operadores
     */
    private TablaPrecedencias tabla;
    /**
     * Referencia al contexto desde donde fue instanciada esta clase
     */
    private Context contexto;
    /**
     * Colección que funciona para ayudar a detectar errores de tipo 2 (errores de reducción)
     */
    private ArrayList<String> arbolReducciones;

    /**
     * Crea un nuevo AnalizadorSintactico
     */
    public AnalizadorSintactico(Context unContexto){
        this.contexto = unContexto;
        this.arbolReducciones = null;
        this.tabla = new TablaPrecedencias();
        this.tabla.construyeTabla();
        this.lexico = new AnalizadorLexico(unContexto);
    }

    /**
     * Acondiciona una cadena de entrada al poner explicitamente el caracter que representa a la operación lógica AND
     * @param   unaEntrada  la cadena de entrada que sera examinada
     * @return              una cadena con la operación AND anexada de manera explícita
     */
    private String normalizaEntrada(String unaEntrada){
        Pattern patAux;
        Pattern patSig;
        String entradaLimpia = unaEntrada.replaceAll("\\s+", "");
        String expr = "";

        patAux = Pattern.compile("[^+\u2022.(]");
        patSig = Pattern.compile("[^+\u2022.')]");
        for (int idxAux = 0, idxSig = idxAux + 1, tam = entradaLimpia.length(); idxAux < tam; idxAux++, idxSig++){
            String aux = Character.toString(entradaLimpia.charAt(idxAux));
            expr += aux;
            if (idxSig < tam){
                String sig = Character.toString(entradaLimpia.charAt(idxSig));
                Matcher matchAux = patAux.matcher(aux);
                Matcher matchSig = patSig.matcher(sig);
                if (matchAux.matches() && matchSig.matches())
                    expr += '\u2022';
            }
        }
        return expr;
    }

    /**
     * Punto de entrada para el análisis de varias funciones lógicas
     * @param   unasEntradas    es un arreglo de funciones lógicas
     * @return                  una colección de arboles de funciones lógicas analizadas
     */
    public ArrayList<Arbol> ingresaEntradas(String[] unasEntradas) throws Exception{
        ArrayList<Arbol> bosque = new ArrayList<Arbol>();

        for (int i = 0; i < unasEntradas.length; i++)
            try{
                bosque.add(this.ingresaEntrada(unasEntradas[i]));
            }
            catch (Exception e) {
                bosque.clear();
                throw new Exception(this.contexto.getString(R.string.errSintaxisFuncion) + (i + 1) + ": " + e.getMessage());
            }
        return bosque;
    }

    /**
     * Punto de entrada al análisis de la sintaxis de una función lógica
     * @param   unaEntrada  es la función lógica que sera analizada
     * @return              un árbol sintáctico que sera construido por el analizador
     */
    public Arbol ingresaEntrada(String unaEntrada) throws Exception{
        String entradaNormalizada = this.normalizaEntrada(unaEntrada);
        Arbol aux = this.analiza(entradaNormalizada + '$');
        aux.asignaRaiz();
        return aux;
    }

    /**
     * Algoritmo del análisis sintáctico por precedencia de operadores para una función lógica
     * @param   unaEntrada  es la función lógica que sera analizada
     * @return              un árbol abstracto referente a la expresion analizada
     */
    private Arbol analiza(String unaEntrada) throws Exception{
        Arbol arbol = new Arbol();
        Stack<Token> pila = new Stack<Token>();
        Token pesos = new Token(TipoToken.PESOS, "$");
        int ae = 0;

        this.arbolReducciones = new ArrayList<String>();
        pila.push(pesos);
        while(!(pila.peek() == pesos && unaEntrada.charAt(ae) == '$') && ae < unaEntrada.length()){
            Token cima = pila.peek();
            Token aeTok = this.lexico.evalua(unaEntrada.charAt(ae));
            Precedencia prec = this.tabla.get(cima.getTipo(), aeTok.getTipo());
            switch(prec) {
                case MENOR:
                case IGUAL: //desplazamiento
                    this.arbolReducciones.add("<");
                    this.arbolReducciones.add(aeTok.getTipo().toString());
                    pila.push(aeTok);
                    ae++;
                    break;
                case MAYOR: //reducción
                    do {
                        cima = pila.pop();
                        prec = this.verificaProduccion();
                        if (prec == Precedencia.IGUAL && cima.getTipo() != TipoToken.PARIZQ && cima.getTipo() != TipoToken.PARDER) {
                            arbol.insertaEnArbol(cima);
                        }
                    }
                    while (prec == Precedencia.IGUAL && this.tabla.get(pila.peek().getTipo(), cima.getTipo()) != Precedencia.MENOR);
                    break;
            }
            char caracterPrevio = unaEntrada.charAt(ae <= 0 ? 0 : ae - 1);
            switch (prec) {
                case ERR_FALTA_OPERADOR:    //inutilizado por la corrección echa con la normalización de la entrada
                    throw new Exception(this.contexto.getString(R.string.errSintaxisOperador) + caracterPrevio);
                case ERR_FALTA_OPERANDO:
                    throw new Exception(this.contexto.getString(R.string.errSintaxisOperando) + caracterPrevio);
                case ERR_FALTA_EXPRESION:   //inutilizado por la tabla de prec, pero el error 2 lo usa para parentesis sin contenido
                    throw new Exception(this.contexto.getString(R.string.errSintaxisExpresionVacia) + caracterPrevio);
                case ERR_PAREN_DESEQUILIBRADOS: //inutilizado por que se detectan los errores individuales de los parentesis antes
                    throw new Exception(this.contexto.getString(R.string.errSintaxisParentesisDesequilibrados) + caracterPrevio);
                case ERR_FALTA_PARENDER:
                    throw new Exception(this.contexto.getString(R.string.errSintaxisParentesisDerecho) + caracterPrevio);
                case ERR_FALTA_PARENIZQ:
                    throw new Exception(this.contexto.getString(R.string.errSintaxisParentesisIzquierdo) + caracterPrevio);
            }
        }
        return arbol;
    }

    public TabSim regresaTablaSimbolos() { return this.lexico.getTabSim(); }

    /**
     * Verifica que las reducciones se esten dando de acuerdo a la gramática de operadores
     * @return              un valor Precedencia.IGUAL en caso de que las reducciones cumplan con la gramática de operadores
     */
    private Precedencia verificaProduccion(){
        String porcionProduccion;
        int indiceInicioProduccion;
        int indiceUltimaReduccion;
        Precedencia res = Precedencia.IGUAL;

        indiceInicioProduccion = this.arbolReducciones.lastIndexOf("<");    //busca el inicio del ultimo terminal
        indiceUltimaReduccion = this.arbolReducciones.lastIndexOf("B");
        this.arbolReducciones.remove(indiceInicioProduccion);   //remueve el separador de inicio de produccion ("<")
        porcionProduccion = this.arbolReducciones.remove(indiceInicioProduccion);   //remueve el terminal a ser analizado
        switch (porcionProduccion){
            case "ID":
                this.arbolReducciones.add("B");
                break;
            case "NOT":
                if (indiceInicioProduccion > 0 && this.arbolReducciones.get(indiceInicioProduccion - 1) != "B")   //error de sintaxis sin B a la izquierda
                    res = Precedencia.ERR_FALTA_OPERANDO;
                break;
            case "AND":
            case "OR":
                if (indiceUltimaReduccion > -1 && indiceInicioProduccion < indiceUltimaReduccion) {
                    if (indiceInicioProduccion > 0 && this.arbolReducciones.get(indiceInicioProduccion - 1) == "B")
                        this.arbolReducciones.remove(indiceInicioProduccion - 1);
                    else    //error de sintaxis sin B a la izquierda
                        res = Precedencia.ERR_FALTA_OPERANDO;
                }
                else    //error de sintaxis sin B a la derecha
                    res = Precedencia.ERR_FALTA_OPERANDO;
                break;
            case "PARDER":
                if (indiceInicioProduccion > 0 && this.arbolReducciones.get(indiceInicioProduccion - 1) != "B")   //error de sintaxis en B a la izq de PARDER
                    res = Precedencia.ERR_FALTA_EXPRESION;
                break;
        }
        return res;
    }
}