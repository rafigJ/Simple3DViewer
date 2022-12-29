package com.cgvsu.math;

public class Vector3 {
    private float x;
    private float y;
    private float z;
    final static private float eps = 1e-7f;

    public Vector3() {

    }

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public  float getZ() {
        return z;
    }

    public void sum(final Vector3 v2) {
        this.x += v2.getX();
        this.y += v2.getY();
        this.z += v2.getZ();
    }

    public static Vector3 sum(final Vector3 v1, final Vector3 v2) {
        final float x = v1.getX() + v2.getX();
        final float y = v1.getY() + v2.getY();
        final float z = v1.getZ() + v2.getZ();

        return new Vector3(x, y, z);
    }

    public void sub(final Vector3 v2) {
        this.x -= v2.getX();
        this.y -= v2.getY();
        this.z -= v2.getZ();
    }

    public static Vector3 sub(final Vector3 v1, final Vector3 v2) {
        final float x = v1.getX() - v2.getX();
        final float y = v1.getY() - v2.getY();
        final float z = v1.getZ() - v2.getZ();

        return new Vector3(x, y, z);
    }

    public void multiply(final float n) {
        this.x *= n;
        this.y *= n;
        this.z *= n;
    }

    public static Vector3 multiply(final Vector3 v1,final float n) {
        final float x = v1.getX() * n;
        final float y = v1.getY() * n;
        final float z = v1.getZ() * n;

        return new Vector3(x, y, z);
    }

    public void divide(final float n) throws Exception{
        if(n - 0 < eps) {
            throw new Exception("На 0 делить нельзя");
        }

        this.x = this.x / n;
        this.y = this.y / n;
        this.z = this.z / n;
    }

    public static Vector3 divide(Vector3 v1, float n) throws Exception {
        if(n - 0 < eps) {
            throw new Exception("На 0 делить нельзя");
        }
        final float x = v1.getX() / n;
        final float y = v1.getY() / n;
        final float z = v1.getZ() / n;

        return new Vector3(x, y, z);
    }

    public float length() {
        return (float) Math.sqrt(this.getX() * this.getX() + this.getY() * this.getY() + this.getZ() * this.getZ());
    }

    public static float length(final Vector3 v1) {
        return (float) Math.sqrt(v1.getX() * v1.getX() + v1.getY() * v1.getY() + v1.getZ() * v1.getZ());
    }

    public float dotProduct(final Vector3 v2) {
        return this.getX() * v2.getX() + this.getY() * v2.getY() + this.getZ() * v2.getZ();
    }

    public static float dotProduct(final Vector3 v1,final Vector3 v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();
    }

    public void normalization() {
        final float invLength = (float) (1 / Math.sqrt(this.getX() * this.getX() + this.getY() * this.getY() + this.getZ() * this.getZ()));
        this.x *= invLength;
        this.y *= invLength;
        this.z *= invLength;
    }

    public static Vector3 normalization(final Vector3 v1) {
        final float invLength = (float) (1 / Math.sqrt(v1.getX() * v1.getX() + v1.getY() * v1.getY() + v1.getZ() * v1.getZ()));

        final float x = v1.getX() * invLength;
        final float y = v1.getY() * invLength;
        final float z = v1.getZ() * invLength;

        return new Vector3(x, y, z);
    }

    public static Vector3 crossProduct(final Vector3 v1, final Vector3 v2) {
        final float z = v1.getX() * v2.getY() - v1.getY() * v2.getX();
        final float x = v1.getY() * v2.getZ() - v1.getZ() * v2.getY();
        final float y = v2.getX() * v1.getZ() - v2.getZ() * v1.getX();

        return new Vector3(x, y, z);
    }
}
