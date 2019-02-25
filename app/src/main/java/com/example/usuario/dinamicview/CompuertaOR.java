package com.example.usuario.dinamicview;

import android.app.Application;
import android.content.Context;
import android.graphics.BitmapFactory;

/**
 * Created by jgarcia on 28/11/16.
 */

public class CompuertaOR extends Compuerta {
    public CompuertaOR(Context contexto)
    {
        super();
        this.image = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.or);
    }

    public void calculaCoordenadas(int numColumnas, int columna, int renglon, int tamCuadroAncho, int tamCuadroAlto)
    {
        this.x = ((numColumnas-columna)+1) * tamCuadroAncho;
        this.y = ((renglon) * tamCuadroAlto) - 25;
    }
}
