package com.cgvsu.math;

import org.junit.jupiter.api.Test;

class Vector3Test {
    private final Vector3 v1 = new Vector3(1,1,0.5f);
    private final Vector3 v2 = new Vector3(3.4f, 1, 2);
    private final float EPS = 1e-7f;

    @Test
    void sum() {
        Vector3 v = new Vector3(1,2,3);
        v.sum(v1);

        Vector3 vRes = new Vector3(2,3, 3.5f);

        assert (v.getX() - vRes.getX() < EPS);
        assert (v.getY() - vRes.getY() < EPS);
        assert (v.getZ() - vRes.getZ() < EPS);
    }

    @Test
    void sum1() {
        Vector3 v = Vector3.sum(v1, v2);
        Vector3 vRes = new Vector3(4.4f, 2, 2.5f);

        assert (v.getX() - vRes.getX() < EPS);
        assert (v.getY() - vRes.getY() < EPS);
        assert (v.getZ() - vRes.getZ() < EPS);
    }

    @Test
    void sub() {
        Vector3 v = new Vector3(1,2,3);
        v.sub(v1);

        Vector3 vRes = new Vector3(0,1, 2.5f);

        assert (v.getX() - vRes.getX() < EPS);
        assert (v.getY() - vRes.getY() < EPS);
        assert (v.getZ() - vRes.getZ() < EPS);
    }

    @Test
    void dotProduct() {
        Vector3 v = new Vector3(1,2,3);
        float dp = v.dotProduct(v2);

        float dpRes = 11.4f;

        assert (dp - dpRes < EPS);

    }

    @Test
    void dotProduct1() {
        float dp = Vector3.dotProduct(v1, v2);
        float dpRes = 5.4f;

        assert (dp - dpRes < EPS);
    }

    @Test
    void normalization() {
        Vector3 v = new Vector3(3, 4, 1);
        v.normalization();

        Vector3 vRes = new Vector3(0.6f, 0.8f, 0.2f);

        assert (v.getX() - vRes.getX() < EPS);
        assert (v.getY() - vRes.getY() < EPS);
        assert (v.getZ() - vRes.getZ() < EPS);

    }

    @Test
    void normalization1() {
        Vector3 v = Vector3.normalization(v1);
        Vector3 vRes = new Vector3(2.0f/3.0f * 1, 2.0f/3.0f * 1, 2.0f/3.0f * 0.5f);

        assert (v.getX() - vRes.getX() < EPS);
        assert (v.getY() - vRes.getY() < EPS);
        assert (v.getZ() - vRes.getZ() < EPS);
    }

    @Test
    void crossProduct() {
        Vector3 v = Vector3.crossProduct(v1,v2);
        Vector3 vRes = new Vector3(1.5f, -0.3f, -2.4f);

        assert (v.getX() - vRes.getX() < EPS);
        assert (v.getY() - vRes.getY() < EPS);
        assert (v.getZ() - vRes.getZ() < EPS);
    }
}