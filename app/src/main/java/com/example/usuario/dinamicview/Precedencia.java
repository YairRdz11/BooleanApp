package com.example.usuario.dinamicview;

/**
 * Indica los posibles estados de la Tabla de Precedencias
 */
public enum Precedencia{
    MENOR,
    IGUAL,
    MAYOR,
    ERR_FALTA_OPERADOR,
    ERR_FALTA_OPERANDO,
    ERR_FALTA_EXPRESION,
    ERR_PAREN_DESEQUILIBRADOS,
    ERR_FALTA_PARENDER,
    ERR_FALTA_PARENIZQ
}