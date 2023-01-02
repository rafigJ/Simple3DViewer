package com.cgvsu.model;

import com.cgvsu.math.Vector2;
import com.cgvsu.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class ModelOnScene {
    private List<Vector3> vertices;
    private List<Vector2> textureVertices;
    private List<Vector3> normals;
    private List<Polygon> polygons;
    private  Vector3 vS;
    private Vector3 vR;
    private Vector3 vT;

    public ModelOnScene(final List<Vector3> vertices, final List<Vector2> textureVertices, final List<Vector3> normals, final List<Polygon> polygons,
                        final Vector3 vS, final Vector3 vR, final Vector3 vT) {
        this.vertices = vertices;
        this.textureVertices = textureVertices;
        this.normals = normals;
        this.polygons = polygons;
        this.vS = vS;
        this.vR = vR;
        this.vT = vT;
    }

    public ModelOnScene() {
        vertices = new ArrayList<>();
        textureVertices = new ArrayList<>();
        normals = new ArrayList<>();
        polygons = new ArrayList<>();
    }

    public List<Vector3> getVertices() {
        return vertices;
    }

    public List<Vector2> getTextureVertices() {
        return textureVertices;
    }

    public List<Vector3> getNormals() {
        return normals;
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }

    public Vector3 getVS() {
        return vS;
    }

    public Vector3 getVR() {
        return vR;
    }

    public Vector3 getVT() {
        return vT;
    }

    public void setVertices(final List<Vector3> vertices) {
        this.vertices = vertices;
    }

    public void setTextureVertices(final List<Vector2> vertices) {
        this.textureVertices = vertices;
    }

    public void setNormals(final List<Vector3> vertices) {
        this.normals = vertices;
    }

    public void setPolygons(final List<Polygon> vertices) {
        this.polygons = vertices;
    }

    public void setVS(final Vector3 vS) {
        this.vS = vS;
    }

    public void setVT(final Vector3 vT) {
        this.vT = vT;
    }

    public void setVR(final Vector3 vR) {
        this.vR = vR;
    }

}
