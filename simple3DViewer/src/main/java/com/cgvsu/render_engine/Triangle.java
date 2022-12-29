package com.cgvsu.render_engine;



    import java.awt.*;

public class Triangle {
    private static float X1;
    private static float Y1;
    private static float X2;
    private static float Y2;
    private static float X3;
    private static float Y3;
    private   static Color  A;
    private   static Color  B;
    private   static Color  C;

    public float getX1() {
        return X1;
    }

    public  float getY1() {
        return Y1;
    }

    public  float getX2() {
        return X2;
    }

    public  float getY2() {
        return Y2;
    }

    public  float getX3() {
        return X3;
    }

    public  float getY3() {
        return Y3;
    }

    public  Color getA() {
        return A;
    }

    public  Color getC() { return C;  }

    public  Color getB() {
        return B;
    }


    public Triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        X1 = x1;
        Y1 = y1;
        X2 = x2;
        Y2 = y2;
        X3 = x3;
        Y3 = y3;


    }

    public Color colorRGB(int r,int g,int b){
        return new Color(r ,g ,b);
    }
}

