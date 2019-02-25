package com.example.usuario.dinamicview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by jgarcia on 29/11/16.
 */

public class Variable {
    private int x;
    private int y;
    private boolean valor;
    private int color;
    private String nombre;

    public Variable()
    {

    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getX()
    {
        return this.x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setValor(boolean valor)
    {
        this.valor = valor;
        if(this.valor == false)
            this.color = Color.BLACK;
        else
            this.color = Color.RED;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void draw(Canvas canvas, int altura)
    {
        Paint p = new Paint();
        p.setColor(this.color);
        p.setStyle(Paint.Style.FILL);
        p.setTextSize(20);
        p.setStrokeWidth(3);
        canvas.drawText(this.nombre, this.x, this.y + 10, p);
        canvas.drawLine(this.x + 5, this.y + 25, this.x + 5, this.y + altura, p);
    }
}
