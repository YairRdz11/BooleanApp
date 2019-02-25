package com.example.usuario.dinamicview;

/**
 *
 * @author  José Luis García Guerrero
 * @version 1.0
 * @since   2016-09-13
 */
public class Token extends Nodo
{
    /**
     * Indica que tipo de token se esta utilizando
     */
    private TipoToken tipo;
    /**
     * Lexema para el token utilizado
     */
    private String lexema;

    /**
     * Construye un nuevo Token
     */
    public Token()
    {
        super();
        this.tipo = TipoToken.PESOS;
        this.lexema = "";
    }

    /**
     * Construye un nuevo Token
     * @param   unTipo      tipo del token
     * @param   unLexema    valor del token
     */
    public Token(TipoToken unTipo, String unLexema)
    {
        super();
        this.tipo = unTipo;
        this.lexema = unLexema;
    }

    /**
     * Obtiene el tipo de este Token
     * @return              devuelve el tipo correspondiente a este Token
     */
    public TipoToken getTipo()
    {
        return this.tipo;
    }

    /**
     * Obtiene el lexema de este Token
     * @return              devuelve el valor del lexema
     */
    public String getLexema()
    {
        return this.lexema;
    }

    /**
     * Establece el tipo de este Token
     * @param   unTipo      el nuevo tipo de este Token
     */
    public void setTipo(TipoToken unTipo)
    {
        this.tipo = unTipo;
    }

    /**
     * Establece un nuevo lexema a este Token
     * @param   unLexema    el nuevo lexema de este Token
     */
    public void setLexema(String unLexema)
    {
        this.lexema = unLexema;
    }

    /**
     * Entrega la representación interna en texto de este objeto
     * @return              devuelve la cadena con el contenido del objeto
     */
    @Override
    public String toString()
    {
        String cad = "";

        cad += "Tipo: " + this.tipo.name() + " Lexema: " + this.lexema;
        return cad;
    }
}