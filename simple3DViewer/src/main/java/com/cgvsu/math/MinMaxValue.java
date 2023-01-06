package com.cgvsu.math;

import java.util.ArrayList;

public class MinMaxValue {
    private final float minX;
    private final float maxX;
    private final float minY;
    private final float maxY;

    public MinMaxValue(ArrayList<Vector2> resultPoints) {
        this.minX = (float) (Math.ceil(Math.min(resultPoints.get(0).getX(), Math.min(resultPoints.get(1).getX(), resultPoints.get(2).getX()))));
        this.maxX = (float) (Math.floor(Math.max(resultPoints.get(0).getX(), Math.max(resultPoints.get(1).getX(), resultPoints.get(2).getX()))));
        this.minY = (float) (Math.ceil(Math.min(resultPoints.get(0).getY(), Math.min(resultPoints.get(1).getY(), resultPoints.get(2).getY()))));
        this.maxY = (float) (Math.floor(Math.max(resultPoints.get(0).getY(), Math.max(resultPoints.get(1).getY(), resultPoints.get(2).getY()))));
    }

    public float getMinX() {
        return minX;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMinY() {
        return minY;
    }

    public float getMaxY() {
        return maxY;
    }

}