package com.cgvsu.math;

public class Vector3 {
    private float x;
    private float y;
    private float z;

    public Vector3() {

    }

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public void sum(Vector3 v2) {
        this.x += v2.getX();
        this.y += v2.getY();
        this.z += v2.getZ();
    }

    public void sum(Vector3 v1, Vector3 v2) {
        this.x = v1.getX() + v2.getX();
        this.y = v1.getY() + v2.getY();
        this.z = v1.getZ() + v2.getZ();
    }

    public void sub(Vector3 v2) {
        this.x -= v2.getX();
        this.y -= v2.getY();
        this.z -= v2.getZ();
    }

    public void sub(Vector3 v1, Vector3 v2) {
        this.x = v1.getX() - v2.getX();
        this.y = v1.getY() - v2.getY();
        this.z = v1.getZ() - v2.getZ();
    }

    public void increaseOnNumber(float n) {
        this.x *= n;
        this.y *= n;
        this.z *= n;
    }

    public void increaseOnNumber(Vector3 v1, float n) {
        this.x = v1.getX() * n;
        this.y = v1.getY() * n;
        this.z = v1.getZ() * n;
    }

    public void divisionOnNumber(float n) {
        if(n == 0) {
            System.out.println("На 0 делить нельзя");
            return;
        }

        this.x = this.x / n;
        this.y = this.y / n;
        this.z = this.z / n;
    }

    public void divisionOnNumber(Vector3 v1, float n) {
        if(n == 0) {
            System.out.println("На 0 делить нельзя");
            return;
        }
        this.x = v1.getX() / n;
        this.y = v1.getY() / n;
        this.z = v1.getZ() / n;
    }

    public float length() {
        return (float) Math.sqrt(this.getX() * this.getX() + this.getY() * this.getY() + this.getZ() * this.getZ());
    }

    public float length(Vector3 v1) {
        return (float) Math.sqrt(v1.getX() * v1.getX() + v1.getY() * v1.getY() + v1.getZ() * v1.getZ());
    }

    public float scalarProduct(Vector3 v2) {
        return this.getX() * v2.getX() + this.getY() * v2.getY() + this.getZ() * v2.getZ();
    }

    public float scalarProduct(Vector3 v1, Vector3 v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();
    }

    public void normalization() {
        float invLength = (float) (1 / Math.sqrt(this.getX() * this.getX() + this.getY() * this.getY() + this.getZ() * this.getZ()));
        this.x *= invLength;
        this.y *= invLength;
        this.z *= invLength;
    }

    public void normalization(Vector3 v1) {
        float invLength = (float) (1 / Math.sqrt(v1.getX() * v1.getX() + v1.getY() * v1.getY() + v1.getZ() * v1.getZ()));

        this.x = v1.getX() * invLength;
        this.y = v1.getY() * invLength;
        this.z = v1.getZ() * invLength;
    }

    public void vectorProduct(Vector3 v1, Vector3 v2) {
        this.z = v1.getX() * v2.getY() - v1.getY() * v2.getX();
        this.x = v1.getY() * v2.getZ() - v1.getZ() * v2.getY();
        this.y = v2.getX() * v1.getZ() - v2.getZ() * v1.getX();
    }
}
