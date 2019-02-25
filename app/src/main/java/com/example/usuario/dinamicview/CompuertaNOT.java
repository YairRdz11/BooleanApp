package com.example.usuario.dinamicview;

import android.content.Context;
import android.graphics.BitmapFactory;

/**
 * Created by jgarcia on 01/12/16.
 */

public class CompuertaNOT extends Compuerta {

    public CompuertaNOT(Context contexto)
    {
        super();
        super.image = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.nota);
    }

    public void calculaCoordenadas(int numColumnas, int columna, int renglon, int tamCuadroAncho, int tamCuadroAlto)
    {
        this.x = ((numColumnas-columna)+1) * tamCuadroAncho;
        this.y = ((renglon) * tamCuadroAlto) - (tamCuadroAlto/2);
    }
}
