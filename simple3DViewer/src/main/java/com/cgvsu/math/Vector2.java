package com.cgvsu.math;

public class Vector2 {
    private float x;
    private float y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2() {

    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public void sum(Vector2 v2) {
        this.x += v2.getX();
        this.y += v2.getY();
    }

    public void sum(Vector2 v1, Vector2 v2) {
        this.x = v1.getX() + v2.getX();
        this.y = v1.getY() + v2.getY();
    }

    public void sub(Vector2 v2) {
        this.x -= v2.getX();
        this.y -= v2.getY();
    }

    public void sub(Vector2 v1, Vector2 v2) {
        this.x = v1.getX() - v2.getX();
        this.y = v1.getY() - v2.getY();
    }

    public void increaseOnNumber(float n) {
        this.x *= n;
        this.y *= n;
    }

    public void increaseOnNumber(Vector2 v1, float n) {
        this.x = v1.getX() * n;
        this.y = v1.getY() * n;
    }

    public void divisionOnNumber(float n) {
        if(n == 0) {
            System.out.println("На 0 делить нельзя");
            return;
        }

        this.x = this.x / n;
        this.y = this.y / n;
    }

    public void divisionOnNumber(Vector2 v1, float n) {
        if(n == 0) {
            System.out.println("На 0 делить нельзя");
            return;
        }
        this.x = v1.getX() / n;
        this.y = v1.getY() / n;
    }

    public float length() {
        return (float) Math.sqrt(this.getX() * this.getX() + this.getY() * this.getY());
    }

    public float length(Vector2 v1) {
        return (float) Math.sqrt(v1.getX() * v1.getX() + v1.getY() * v1.getY());
    }

    public float scalarProduct(Vector2 v2) {
        return this.getX() * v2.getX() + this.getY() * v2.getY();
    }

    public float scalarProduct(Vector2 v1, Vector2 v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY();
    }

    public void normalization() {
        float invLength = (float) (1 / Math.sqrt(this.getX() * this.getX() + this.getY() * this.getY()));
        this.x *= invLength;
        this.y *= invLength;
    }

    public void normalization(Vector2 v1) {
        float invLength = (float) (1 / (Math.sqrt(v1.getX() * v1.getX() + v1.getY() * v1.getY())));
        this.x = v1.getX() * invLength;
        this.y = v1.getY() * invLength;
    }

}
