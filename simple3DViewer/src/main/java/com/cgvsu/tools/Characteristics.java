package com.cgvsu.tools;

import com.cgvsu.math.Barycentric;
import com.cgvsu.math.Vector2;

import java.util.ArrayList;

public class Characteristics {
    private final float depth;
    private final float shade;
    private final float VX;
    private final float VY;


    public Characteristics(ArrayList<Float> zCoordinates, ArrayList<Vector2> textureVertexes, ArrayList<Float> shadows, Barycentric barycentric, boolean shadow) {
        this.depth = barycentric.getL1() * zCoordinates.get(0) + barycentric.getL2() * zCoordinates.get(1) + barycentric.getL3() * zCoordinates.get(2);
        this.shade = shadow ? barycentric.getL1() * shadows.get(0) + barycentric.getL2() * shadows.get(1) + barycentric.getL3() * shadows.get(2) : 1;
        this.VX = barycentric.getL1() * textureVertexes.get(0).getX() + barycentric.getL2() * textureVertexes.get(1).getX() + barycentric.getL3() * textureVertexes.get(2).getX();
        this.VY = 1 - (barycentric.getL1() * textureVertexes.get(0).getY() + barycentric.getL2() * textureVertexes.get(1).getY() + barycentric.getL3() * textureVertexes.get(2).getY());
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
