package com.example.usuario.dinamicview;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by jgarcia on 23/11/16.
 */

public class Conector {
    private int xi;
    private int yi;
    private int xf;
    private int yf;
    private int color;

    public Conector(int xi, int yi, int xf, int yf)
    {
        this.xi = xi;
        this.yi = yi;
        this.xf = xf;
        this.yf = yf;
    }

    public void setColor(int color)
    {
        this.color = color;
    }

    public void draw(Canvas canvas)
    {
        int xm = 0;
        Paint p = new Paint();
        p.setColor(this.color);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(3);
        if(yi == yf)
        {
            canvas.drawLine(xi, yi, xf, yf, p);
        }
        else
        {
            xm = xi + ((xf - xi) / 2);
            canvas.drawLine(xi, yi, xm, yi, p);
            canvas.drawLine(xm, yi, xm, yf, p);
            canvas.drawLine(xm, yf, xf, yf, p);
        }
    }
}
