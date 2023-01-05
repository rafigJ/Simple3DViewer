package com.cgvsu.math;

import com.cgvsu.render_engine.Triangle;

public class Barycentric {
    private final float l1;
    private final float l2;
    private final float l3;

    public float getL1() {
        return l1;
    }

    public float getL2() {
        return l2;
    }

    public float getL3() {
        return l3;
    }

    public Barycentric(Triangle t, float x, float y) {
        this.l1 = ((t.getY2() - t.getY3()) * (x - t.getX3()) + (t.getX3() - t.getX2()) * (y - t.getY3())) /
                ((t.getY2() - t.getY3()) * (t.getX1() - t.getX3()) + (t.getX3() - t.getX2()) * (t.getY1() - t.getY3()));
        this.l2 = ((t.getY3() - t.getY1()) * (x - t.getX3()) + (t.getX1() - t.getX3()) * (y - t.getY3())) /
                ((t.getY2() - t.getY3()) * (t.getX1() - t.getX3()) + (t.getX3() - t.getX2()) * (t.getY1() - t.getY3()));
        this.l3 = 1 - l1 - l2;
    }

    public boolean isInside(){
        return (l1 >= 0 && l1 <= 1 && l2 >= 0 && l2 <= 1 && l3 >= 0 && l3 <= 1);
    }
}