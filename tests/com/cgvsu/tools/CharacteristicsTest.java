package com.cgvsu.tools;

import com.cgvsu.math.Barycentric;
import com.cgvsu.math.Vector2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class CharacteristicsTest {
    Vector2 v1 = new Vector2(1, 2);
    Vector2 v2 = new Vector2(3, 4);
    Vector2 v3 = new Vector2(12, 12);
    Vector2 vT1 = new Vector2(0.5F, 1F);
    Vector2 vT2 = new Vector2(0.5F, 0.5F);
    Vector2 vT3 = new Vector2(0.5F, 0.1F);
    ArrayList<Vector2> resultPoints = new ArrayList<>();
    final ArrayList<Float> zCoordinates = new ArrayList<>();
    final ArrayList<Vector2> textureCoordinates = new ArrayList<>();
    final ArrayList<Float> shadows = new ArrayList<>();
    final float EPS = 1e-7f;


    @Test
    void getDepth() {
        resultPoints.add(v1);
        resultPoints.add(v2);
        resultPoints.add(v3);
        zCoordinates.add(1F);
        zCoordinates.add(2F);
        zCoordinates.add(3F);
        textureCoordinates.add(vT1);
        textureCoordinates.add(vT2);
        textureCoordinates.add(vT3);
        shadows.add(0.1f);
        shadows.add(0.1f);
        shadows.add(0.1f);
        Triangle triangle = new Triangle(resultPoints);
        Barycentric barycentric = new Barycentric(triangle, 10, 10);
        Characteristics characteristics = new Characteristics(zCoordinates, textureCoordinates, shadows, barycentric, true);
        assert 2 - characteristics.getDepth() < EPS;
    }

    @Test
    void getShade() {
        resultPoints.add(v1);
        resultPoints.add(v2);
        resultPoints.add(v3);
        zCoordinates.add(1F);
        zCoordinates.add(2F);
        zCoordinates.add(3F);
        textureCoordinates.add(vT1);
        textureCoordinates.add(vT2);
        textureCoordinates.add(vT3);
        shadows.add(0.1f);
        shadows.add(0.1f);
        shadows.add(0.1f);
        Triangle triangle = new Triangle(resultPoints);
        Barycentric barycentric = new Barycentric(triangle, 10, 10);
        Characteristics characteristics = new Characteristics(zCoordinates, textureCoordinates, shadows, barycentric, true);
        assert 0.1 - characteristics.getShade() < EPS;
    }

    @Test
    void getVX() {
        resultPoints.add(v1);
        resultPoints.add(v2);
        resultPoints.add(v3);
        zCoordinates.add(1F);
        zCoordinates.add(2F);
        zCoordinates.add(3F);
        textureCoordinates.add(vT1);
        textureCoordinates.add(vT2);
        textureCoordinates.add(vT3);
        shadows.add(0.1f);
        shadows.add(0.1f);
        shadows.add(0.1f);
        Triangle triangle = new Triangle(resultPoints);
        Barycentric barycentric = new Barycentric(triangle, 10, 10);
        Characteristics characteristics = new Characteristics(zCoordinates, textureCoordinates, shadows, barycentric, true);
        assert 0.5 - characteristics.getVX() < EPS;

    }

    @Test
    void getVY() {
        resultPoints.add(v1);
        resultPoints.add(v2);
        resultPoints.add(v3);
        zCoordinates.add(1F);
        zCoordinates.add(2F);
        zCoordinates.add(3F);
        textureCoordinates.add(vT1);
        textureCoordinates.add(vT2);
        textureCoordinates.add(vT3);
        shadows.add(0.1f);
        shadows.add(0.1f);
        shadows.add(0.1f);
        Triangle triangle = new Triangle(resultPoints);
        Barycentric barycentric = new Barycentric(triangle, 10, 10);
        Characteristics characteristics = new Characteristics(zCoordinates, textureCoordinates, shadows, barycentric, true);
        assert 0.4 - characteristics.getVY() < EPS;
    }
}