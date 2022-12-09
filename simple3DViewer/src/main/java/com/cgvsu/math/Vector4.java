package com.cgvsu.math;
public class Vector4 {
    private float x;
    private float y;
    private float z;
    private float m;

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

    public void sum(Vector4 v2) {
        this.x += v2.getX();
        this.y += v2.getY();
        this.z += v2.getZ();
        this.m += v2.getM();
    }

    public void sum(Vector4 v1, Vector4 v2) {
        this.x = v1.getX() + v2.getX();
        this.y = v1.getY() + v2.getY();
        this.z = v1.getZ() + v2.getZ();
        this.m = v1.getM() + v2.getM();
    }

    public void sub(Vector4 v2) {
        this.x -= v2.getX();
        this.y -= v2.getY();
        this.z -= v2.getZ();
        this.m -= v2.getM();
    }

    public void sub(Vector4 v1, Vector4 v2) {
        this.x = v1.getX() - v2.getX();
        this.y = v1.getY() - v2.getY();
        this.z = v1.getZ() - v2.getZ();
        this.m = v1.getM() - v2.getM();
    }

    public void increaseOnNumber(float n) {
        this.x *= n;
        this.y *= n;
        this.z *= n;
        this.m *= n;
    }

    public void increaseOnNumber(Vector4 v1, float n) {
        this.x = v1.getX() * n;
        this.y = v1.getY() * n;
        this.z = v1.getZ() * n;
        this.m = v1.getM() * n;
    }

    public void divisionOnNumber(float n) {
        if(n == 0) {
            System.out.println("На 0 делить нельзя");
            return;
        }

        this.x = this.x / n;
        this.y = this.y / n;
        this.z = this.z / n;
        this.m = this.m / n;
    }

    public void divisionOnNumber(Vector4 v1, float n) {
        if(n == 0) {
            System.out.println("На 0 делить нельзя");
            return;
        }
        this.x = v1.getX() / n;
        this.y = v1.getY() / n;
        this.z = v1.getZ() / n;
        this.m = v1.getM() / n;
    }
    public float length() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.m * this.m);
    }

    public float length(Vector4 v1) {
        return (float) Math.sqrt(v1.getX() * v1.getX() + v1.getY() * v1.getY() + v1.getZ() * v1.getZ() + v1.getM() * v1.getM());
    }

    public float scalarProduct(Vector4 v2) {
        return this.x * v2.getX() + this.y * v2.getY() + this.z * v2.getZ() + this.m * v2.getM();
    }

    public float scalarProduct(Vector4 v1, Vector4 v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ() + v1.getM() * v2.getM();
    }

    public void normalization() {
        float invLength = (float) (1 / Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.m * this.m));
        this.x *= invLength;
        this.y *= invLength;
        this.z *= invLength;
        this.m *= invLength;
    }

    public void normalization(Vector4 v1) {
        float invLength = (float) (1 / Math.sqrt(v1.getX() * v1.getX() + v1.getY() * v1.getY() + v1.getZ() * v1.getZ() + v1.getM() * v1.getM()));

        this.x = v1.getX() * invLength;
        this.y = v1.getY() * invLength;
        this.z = v1.getZ() * invLength;
        this.m = v1.getM() * invLength;
    }

}
