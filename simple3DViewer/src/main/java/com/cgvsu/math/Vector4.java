package com.cgvsu.math;
public class Vector4 {
    private float x;
    private float y;
    private float z;
    private float m;
    private final static float eps = 1e-7f;

    public Vector4(float x, float y, float z, float m) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.m = m;
    }

    public Vector4() {

    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public  float getZ() {
        return z;
    }

    public float getM() {
        return m;
    }

    public void sum(final Vector4 v2) {
        this.x += v2.getX();
        this.y += v2.getY();
        this.z += v2.getZ();
        this.m += v2.getM();
    }

    public static Vector4 sum(final Vector4 v1, final Vector4 v2) {
        final float x = v1.getX() + v2.getX();
        final float y = v1.getY() + v2.getY();
        final float z = v1.getZ() + v2.getZ();
        final float m = v1.getM() + v2.getM();

        return new Vector4(x, y, z, m);
    }

    public void sub(final Vector4 v2) {
        this.x -= v2.getX();
        this.y -= v2.getY();
        this.z -= v2.getZ();
        this.m -= v2.getM();
    }

    public static Vector4 sub(final Vector4 v1, final Vector4 v2) {
        final float x = v1.getX() - v2.getX();
        final float y = v1.getY() - v2.getY();
        final float z = v1.getZ() - v2.getZ();
        final float m = v1.getM() - v2.getM();

        return new Vector4(x, y, z, m);
    }

    public void multiply(final float n) {
        this.x *= n;
        this.y *= n;
        this.z *= n;
        this.m *= n;
    }

    public static Vector4 multiply(final Vector4 v1, final float n) {
        final float x = v1.getX() * n;
        final float y = v1.getY() * n;
        final float z = v1.getZ() * n;
        final float m = v1.getM() * n;

        return new Vector4(x, y, z, m);
    }

    public void divide(final float n) throws Exception {
        if(n - 0 < eps) {
            throw new Exception("На 0 делить нельзя");
        }

        this.x = this.x / n;
        this.y = this.y / n;
        this.z = this.z / n;
        this.m = this.m / n;
    }

    public static Vector4 divide(final Vector4 v1, final float n) throws Exception {
        if(n - 0 < eps) {
            throw new Exception("На 0 делить нельзя");
        }
        final float x = v1.getX() / n;
        final float y = v1.getY() / n;
        final float z = v1.getZ() / n;
        final float m = v1.getM() / n;

        return new Vector4(x, y, z, m);
    }
    public float length() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.m * this.m);
    }

    public static float length(final Vector4 v1) {
        return (float) Math.sqrt(v1.getX() * v1.getX() + v1.getY() * v1.getY() + v1.getZ() * v1.getZ() + v1.getM() * v1.getM());
    }

    public float dotProduct(final Vector4 v2) {
        return this.x * v2.getX() + this.y * v2.getY() + this.z * v2.getZ() + this.m * v2.getM();
    }

    public static float dotProduct(final Vector4 v1, final Vector4 v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ() + v1.getM() * v2.getM();
    }

    public void normalization() {
        float invLength = (float) (1 / Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.m * this.m));
        this.x *= invLength;
        this.y *= invLength;
        this.z *= invLength;
        this.m *= invLength;
    }

    public static Vector4 normalization(final Vector4 v1) {
        float invLength = (float) (1 / Math.sqrt(v1.getX() * v1.getX() + v1.getY() * v1.getY() + v1.getZ() * v1.getZ() + v1.getM() * v1.getM()));

        final float x = v1.getX() * invLength;
        final float y = v1.getY() * invLength;
        final float z = v1.getZ() * invLength;
        final float m = v1.getM() * invLength;

        return new Vector4(x, y, z, m);
    }

}
