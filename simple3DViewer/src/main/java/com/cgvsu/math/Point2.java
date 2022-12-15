package com.cgvsu.math;

public class Point2 {
    private float x;
    private float y;

    public Point2 (float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point2() {

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float distanceTo(Point2 p2) {
        float dx = this.x - p2.getX();
        float dy = this.y - p2.getY();
        return (float) Math.sqrt((double) dx * dx + dy * dy);
    }
}
