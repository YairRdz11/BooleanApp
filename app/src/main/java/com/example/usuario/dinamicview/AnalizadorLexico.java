package com.example.usuario.dinamicview;

import android.content.Context;
import android.content.res.Resources;

import java.util.concurrent.ExecutionException;

/**
 *
 * @author  José Luis García Guerrero
 * @version 1.0
 * @since   2016-09-13
 */
public class AnalizadorLexico
{
    /**
     * Contiene un listado de Simbolos
     */
    private TabSim tablaSimbolos;
    /**
     * Referencia al contexto desde donde fue instanciada esta clase
     */
    private Context contexto;

    /**
     * Crea un nuevo AnalizadorLexico
     */
    public AnalizadorLexico(Context unContexto)
    {
        this.tablaSimbolos = new TabSim();
        this.contexto = unContexto;
    }

    /**
     * Entrega un token por un caracter evaluado
     * @param    c   es un caracter que sera evaluado
     * @return       un nuevo Token como resultado de la evaluación
     */
    public Token evalua(char c) throws Exception
    {
        Token token = null;
        boolean insertSimb = false;

        switch(c)
        {
            case '+':
                token = new Token(TipoToken.OR, Character.toString(c));
                break;
            case '.':
            case '\u2022':
                token = new Token(TipoToken.AND, "\u2022");
                break;
            case '\'':
                token = new Token(TipoToken.NOT, Character.toString(c));
                break;
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
                if(this.tablaSimbolos.getTipoConjunto() == TipoConjunto.NULO || this.tablaSimbolos.getTipoConjunto() == TipoConjunto.ABCDE) {
                    token = new Token(TipoToken.ID, Character.toString(c));
                    this.tablaSimbolos.setTipoConjunto(TipoConjunto.ABCDE);
                    if(this.tablaSimbolos.buscaSimbolo(Character.toString(c)) == null)
                        insertSimb = true;
                }
                else
                    throw new Exception(String.format(this.contexto.getString(R.string.errConjuntoVWXYZ), Character.toString(c)));
                break;
            case 'v':
            case 'w':
            case 'x':
            case 'y':
            case 'z':
                if(this.tablaSimbolos.getTipoConjunto() == TipoConjunto.NULO || this.tablaSimbolos.getTipoConjunto() == TipoConjunto.VWXYZ) {
                    token = new Token(TipoToken.ID, Character.toString(c));
                    this.tablaSimbolos.setTipoConjunto(TipoConjunto.VWXYZ);
                    if(this.tablaSimbolos.buscaSimbolo(Character.toString(c)) == null)
                        insertSimb = true;
                }
                else
                    throw new Exception(String.format(this.contexto.getString(R.string.errConjuntoABCDE), Character.toString(c)));
                break;
            case '(':
                token = new Token(TipoToken.PARIZQ, Character.toString(c));
                break;
            case ')':
                token = new Token(TipoToken.PARDER, Character.toString(c));
                break;
            case '$':
                token = new Token(TipoToken.PESOS, Character.toString(c));
                break;
            default:
                throw new Exception(this.contexto.getString(R.string.errLexico) + c);
        }
        if(insertSimb) {
            if (this.tablaSimbolos.numeroSimbolos() < 5)
                this.tablaSimbolos.agregaSimbolo(Character.toString(c), false);
            else
                throw new Exception(this.contexto.getString(R.string.errSematicaVariables));
        }
        return token;
    }

    /**
     * Obtiene la tabla de simbolos
     * @return       devuelve la tabla de simbolos
     */
    public TabSim getTabSim()
    {
        return this.tablaSimbolos;
    }
}