package com.cgvsu.math;

import java.util.ArrayList;

public class CalculationMinAndMaxValue {
    private final int minX;
    private final int maxX;
    private final int minY;
    private final int maxY;

    public CalculationMinAndMaxValue(ArrayList<Vector2> resultPoints, int width, int height) {
        this.minX = (int) Math.max(0,Math.min(resultPoints.get(0).getX(), Math.min(resultPoints.get(1).getX(), resultPoints.get(2).getX())));
        this.maxX = (int) Math.min(width - 1,Math.max(resultPoints.get(0).getX(), Math.max(resultPoints.get(1).getX(), resultPoints.get(2).getX())));
        this.minY = (int) Math.max(0,Math.min(resultPoints.get(0).getY(), Math.min(resultPoints.get(1).getY(), resultPoints.get(2).getY())));
        this.maxY = (int) Math.min(height - 1,Math.max(resultPoints.get(0).getY(), Math.max(resultPoints.get(1).getY(), resultPoints.get(2).getY())));
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