package com.example.usuario.dinamicview;

import java.util.HashMap;

/**
 *
 * @author  Hugo Armando Rodríguez Flores
 * @version 1.0
 * @since   2016-09-13
 */
public class TablaPrecedencias{
    /**
     * Arreglo de llaves con los terminales de la gramática de operadores
     */
    private TipoToken[] terminales;
    /**
     * Hash de Hashes utilizada para guardar la tabla de precedencias
     */
    private HashMap<TipoToken, HashMap<TipoToken, Precedencia>> tabla;

    /**
     * Inicializa una nueva TablaPrecedencias
     */
    public TablaPrecedencias(){
        this.terminales = TipoToken.values();
        this.tabla = new HashMap<TipoToken, HashMap<TipoToken, Precedencia>>();
    }

    /**
     * Construye la tabla de precedencias específica (no generica)
     */
    public void construyeTabla(){
        Precedencia[][] tablaPrec = {
                //ID                                AND                 OR                  NOT                 PARIZQ                                  PARDER                          PESOS
                {Precedencia.ERR_FALTA_OPERADOR,    Precedencia.MAYOR,  Precedencia.MAYOR,  Precedencia.MAYOR,  Precedencia.ERR_FALTA_OPERANDO,         Precedencia.MAYOR,              Precedencia.MAYOR},
                {Precedencia.MENOR,                 Precedencia.MAYOR,  Precedencia.MAYOR,  Precedencia.MENOR,  Precedencia.MENOR,                      Precedencia.MAYOR,              Precedencia.MAYOR},
                {Precedencia.MENOR,                 Precedencia.MENOR,  Precedencia.MAYOR,  Precedencia.MENOR,  Precedencia.MENOR,                      Precedencia.MAYOR,              Precedencia.MAYOR},
                {Precedencia.MENOR,                 Precedencia.MAYOR,  Precedencia.MAYOR,  Precedencia.MAYOR,  Precedencia.MENOR,                      Precedencia.MAYOR,              Precedencia.MAYOR},
                {Precedencia.MENOR,                 Precedencia.MENOR,  Precedencia.MENOR,  Precedencia.MENOR,  Precedencia.MENOR,                      Precedencia.IGUAL,              Precedencia.ERR_FALTA_PARENDER},
                {Precedencia.ERR_FALTA_OPERADOR,    Precedencia.MAYOR,  Precedencia.MAYOR,  Precedencia.MAYOR,  Precedencia.ERR_PAREN_DESEQUILIBRADOS,  Precedencia.MAYOR,              Precedencia.MAYOR},
                {Precedencia.MENOR,                 Precedencia.MENOR,  Precedencia.MENOR,  Precedencia.MENOR,  Precedencia.MENOR,                      Precedencia.ERR_FALTA_PARENIZQ, Precedencia.ERR_FALTA_EXPRESION}
        };

        this.tabla = new HashMap<TipoToken, HashMap<TipoToken, Precedencia>>();
        //Puede usarse foreach pero conviene mejor usar indices para indizar terminales y tablaPrec
        for(int ren = 0; ren < this.terminales.length; ren++){
            HashMap<TipoToken, Precedencia> renglon = new HashMap<TipoToken, Precedencia>();
            for(int col = 0; col < this.terminales.length; col++)
                renglon.put(this.terminales[col], tablaPrec[ren][col]);
            this.tabla.put(this.terminales[ren], renglon);
            renglon = null;
        }
    }

    /**
     * Recupera un valor de la tabla de precedencias
     * @param    unRenglon  llave para los renglones de la tabla
     * @param    unaColumna llave para las columnas de la tabla
     * @return              el valor que ha sido indizado
     */
    public Precedencia get(TipoToken unRenglon, TipoToken unaColumna){
        HashMap<TipoToken, Precedencia> renglon;
        Precedencia val = null;

        renglon = this.tabla.get(unRenglon);
        if (renglon != null)
            val = renglon.get(unaColumna);
        return val;
    }

    /**
     * Entrega la representación en cadena de esta tabla
     * @return              la cadena con la representación interna de este objeto
     */
    @Override
    public String toString(){
        String representacion = "\t";

        for (int ren = 0; ren < this.terminales.length; ren++){
            representacion += this.tipoTokenMapeo(this.terminales[ren]);
            if (ren + 1 < this.terminales.length)
                representacion += '\t';
        }
        representacion += '\n';
        for (int ren = 0; ren < this.terminales.length; ren++){
            String renglon = "";
            renglon += this.tipoTokenMapeo(this.terminales[ren]) + '\t';
            for (int col = 0; col < this.terminales.length; col++){
                Precedencia tok = this.get(this.terminales[ren], this.terminales[col]);
                if (tok != null)
                    renglon += precedenciaMapeo(tok);
                if (ren < this.terminales.length)
                    renglon += '\t';
            }
            representacion += renglon + '\n';
        }
        return representacion;
    }

    /**
     * Mapea la enumeracion Precedencia a simbolos legibles
     * @param   unaPrec     un atributo de la enumeración Precedencia
     * @return              la equivalencia del atributo a un simbolo legible
     */
    private String precedenciaMapeo(Precedencia unaPrec){
        String res = "";

        switch(unaPrec){
            case MENOR:
                res = "<";
                break;
            case IGUAL:
                res = "=";
                break;
            case MAYOR:
                res = ">";
                break;
        }
        return res;
    }

    /**
     * Mapea la enumeracion TipoToken a simbolos legibles
     * @param   unTipoTok   un atributo de la enumeración TipoToken
     * @return              la equivalencia del atributo a un simbolo legible
     */
    private String tipoTokenMapeo(TipoToken unTipoTok){
        String res = "";

        switch(unTipoTok){
            case ID:
                res = "ID";
                break;
            case AND:
                res = "\u2022";
                break;
            case OR:
                res = "+";
                break;
            case NOT:
                res = "~";
                break;
            case PARIZQ:
                res = "(";
                break;
            case PARDER:
                res = ")";
                break;
            case PESOS:
                res = "$";
                break;
        }
        return res;
    }
}