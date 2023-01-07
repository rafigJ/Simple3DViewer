package com.cgvsu.math;

import java.util.ArrayList;

public class MinMaxValue {
    private final int minX;
    private final int maxX;
    private final int minY;
    private final int maxY;

    public MinMaxValue(ArrayList<Vector2> resultPoints) {
        this.minX = (int) Math.ceil(Math.min(resultPoints.get(0).getX(), Math.min(resultPoints.get(1).getX(), resultPoints.get(2).getX())));
        this.maxX = (int) (Math.floor(Math.max(resultPoints.get(0).getX(), Math.max(resultPoints.get(1).getX(), resultPoints.get(2).getX()))));
        this.minY = (int) (Math.ceil(Math.min(resultPoints.get(0).getY(), Math.min(resultPoints.get(1).getY(), resultPoints.get(2).getY()))));
        this.maxY = (int) (Math.floor(Math.max(resultPoints.get(0).getY(), Math.max(resultPoints.get(1).getY(), resultPoints.get(2).getY()))));
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

}