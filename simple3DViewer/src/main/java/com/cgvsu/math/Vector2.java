package com.cgvsu.math;

public class Vector2 {
    private float x;
    private float y;

    private static final float eps = 1e-7f;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2() {

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void sum(final Vector2 v2) {
        this.x += v2.getX();
        this.y += v2.getY();
    }

    public static Vector2 sum(final Vector2 v1, final Vector2 v2) {
        final float x = v1.getX() + v2.getX();
        final float y = v1.getY() + v2.getY();

        return new Vector2(x,y);
    }

    public void sub(final Vector2 v2) {
        this.x -= v2.getX();
        this.y -= v2.getY();
    }

    public static Vector2 sub(Vector2 v1, Vector2 v2) {
        final float x = v1.getX() - v2.getX();
        final float y = v1.getY() - v2.getY();
        return new Vector2(x, y);
    }

    public void multiply(final float n) {
        this.x *= n;
        this.y *= n;
    }

    public static Vector2 multiply(final Vector2 v1, final float n) {
        final float x = v1.getX() * n;
        final float y = v1.getY() * n;

        return new Vector2(x, y);
    }

    public void divide(final float n) throws Exception {
        if(n - 0 < eps) {
            throw new Exception("Divide by 0");
        }

        this.x = this.x / n;
        this.y = this.y / n;
    }

    public static Vector2 divide(final Vector2 v1,final float n) throws Exception {
        if(n - 0 < eps) {
            throw new Exception("Divide by 0");
        }

        final float x = v1.getX() / n;
        final float y = v1.getY() / n;

        return new Vector2(x, y);
    }

    public float length() {
        return (float) Math.sqrt(this.getX() * this.getX() + this.getY() * this.getY());
    }

    public static float length(final Vector2 v1) {
        return (float) Math.sqrt(v1.getX() * v1.getX() + v1.getY() * v1.getY());
    }

    public float dotProduct(final Vector2 v2) {
        return this.getX() * v2.getX() + this.getY() * v2.getY();
    }

    public static float dotProduct(final Vector2 v1, final Vector2 v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY();
    }

    public void normalization() {
        float invLength = (float) (1 / Math.sqrt(this.getX() * this.getX() + this.getY() * this.getY()));
        this.x *= invLength;
        this.y *= invLength;
    }

    public static Vector2 normalization(final Vector2 v1) {
        float invLength = (float) (1 / (Math.sqrt(v1.getX() * v1.getX() + v1.getY() * v1.getY())));
        final float x = v1.getX() * invLength;
        final float y = v1.getY() * invLength;

        return new Vector2(x, y);
    }

}
