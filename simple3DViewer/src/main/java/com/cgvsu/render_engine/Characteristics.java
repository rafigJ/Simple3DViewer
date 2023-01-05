package com.cgvsu.render_engine;

import com.cgvsu.math.Barycentric;
import com.cgvsu.math.Vector2;

import java.util.ArrayList;

public class Characteristics {
    private final float depth;
    private final float shade;
    private final float VX;
    private final float VY;

    public Characteristics(ArrayList<Float> pointsZ, ArrayList<Vector2> VT, ArrayList<Float> N, Barycentric barycentric, boolean shadow) {
        this.depth = barycentric.getL1() * pointsZ.get(0) + barycentric.getL2() * pointsZ.get(1) + barycentric.getL3() * pointsZ.get(2);
        this.shade = shadow ? barycentric.getL1() * N.get(0) + barycentric.getL2() * N.get(1) + barycentric.getL3() * N.get(2) : 1;
        this.VX = barycentric.getL1() * VT.get(0).getX() + barycentric.getL2() * VT.get(1).getX() + barycentric.getL3() * VT.get(2).getX();
        this.VY = 1 - (barycentric.getL1() * VT.get(0).getY() + barycentric.getL2() * VT.get(1).getY() + barycentric.getL3() * VT.get(2).getY());
    }

    public float getDepth() {
        return depth;
    }

    public float getShade() {
        return shade;
    }

    public float getVX() {
        return VX;
    }

    public float getVY() {
        return VY;
    }
}