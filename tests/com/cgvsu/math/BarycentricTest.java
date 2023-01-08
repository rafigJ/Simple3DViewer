package com.cgvsu.math;

import com.cgvsu.tools.Triangle;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BarycentricTest {
    final float EPS = 1e-7f;
    Vector2 v1 = new Vector2(1,2);
    Vector2 v2 = new Vector2(3,4);
    Vector2 v3 = new Vector2(12,12);
    ArrayList<Vector2> resultPoints = new ArrayList<>();

    @Test
    void getL1() {
        resultPoints.add(v1);
        resultPoints.add(v2);
        resultPoints.add(v3);
        Triangle triangle = new Triangle(resultPoints);
        Barycentric barycentric = new Barycentric(triangle, 10, 10);
        assert 1 - barycentric.getL1() < EPS ;

    }
    @Test
    void getL2() {
        resultPoints.add(v1);
        resultPoints.add(v2);
        resultPoints.add(v3);
        Triangle triangle = new Triangle(resultPoints);
        Barycentric barycentric = new Barycentric(triangle, 10, 10);
        assert -1 - barycentric.getL2() < EPS;
    }

    @Test
    void getL3() {
        resultPoints.add(v1);
        resultPoints.add(v2);
        resultPoints.add(v3);
        Triangle triangle = new Triangle(resultPoints);
        Barycentric barycentric = new Barycentric(triangle, 10, 10);
        assert 1 - barycentric.getL3() < EPS ;
    }
}