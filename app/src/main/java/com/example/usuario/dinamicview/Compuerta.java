package com.example.usuario.dinamicview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by jgarcia on 28/11/16.
 */

public abstract class Compuerta {
    protected int x;
    protected int y;
    protected Bitmap image;
    protected int color;
    protected Nodo nodo;

    public Compuerta()
    {
        this.x = 0;
        this.y = 0;
        this.image = null;
        this.color = Color.WHITE;
        this.nodo = null;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Nodo getNodo() {
        return nodo;
    }

    public void setNodo(Nodo nodo) {
        this.nodo = nodo;
    }

    public void draw(Canvas canvas)
    {
        Paint p = new Paint();
        p.setColor(this.color);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawBitmap(this.image, this.x, this.y, p);
    }

    public abstract void calculaCoordenadas(int numColumnas, int columna, int renglon, int tamCuadroAncho, int tamCuadroAlto);
}
