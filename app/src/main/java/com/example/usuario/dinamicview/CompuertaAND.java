package com.example.usuario.dinamicview;

import android.content.Context;
import android.graphics.BitmapFactory;

/**
 * Created by jgarcia on 28/11/16.
 */

public class CompuertaAND extends Compuerta {
    public CompuertaAND(Context contexto)
    {
        super();
        super.image = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.and);
    }

    public void calculaCoordenadas(int numColumnas, int columna, int renglon, int tamCuadroAncho, int tamCuadroAlto)
    {
        this.x = ((numColumnas-columna)+1) * tamCuadroAncho;
        this.y = ((renglon) * tamCuadroAlto) - 25;
    }
}
