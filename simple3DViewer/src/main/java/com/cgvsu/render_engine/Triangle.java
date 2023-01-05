package com.cgvsu.render_engine;

import com.cgvsu.math.Vector2;

import java.util.ArrayList;

public class Triangle {
    private static float X1;
    private static float Y1;
    private static float X2;
    private static float Y2;
    private static float X3;
    private static float Y3;

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

    public Triangle(ArrayList<Vector2> resultPoints) {
        X1 = resultPoints.get(0).getX();
        Y1 = resultPoints.get(0).getY();
        X2 = resultPoints.get(1).getX();
        Y2 = resultPoints.get(1).getY();
        X3 = resultPoints.get(2).getX();
        Y3 = resultPoints.get(2).getY();
    }
}