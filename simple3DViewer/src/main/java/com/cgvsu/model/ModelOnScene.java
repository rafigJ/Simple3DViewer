package com.cgvsu.model;
import com.cgvsu.math.Matrix4;
import com.cgvsu.math.Vector2;
import com.cgvsu.math.Vector3;
import com.cgvsu.render_engine.GraphicConveyor;

import java.util.List;

public class ModelOnScene extends Model {
    private Model mesh;
    private final Model initialMesh;
    private Vector3 vS = new Vector3(1, 1,1);
    private Vector3 vR = new Vector3(0,0,0);
    private Vector3 vT = new Vector3(0,0,0);

    public ModelOnScene(final Model mesh, final Vector3 vS, final Vector3 vR, final Vector3 vT) {
        this.mesh = mesh;
        this.initialMesh = mesh;
        this.vS = vS;
        this.vR = vR;
        this.vT = vT;
    }

    public ModelOnScene() {
        mesh = new Model();
        this.initialMesh = mesh;
    }

    public List<Vector3> getVertices() {
        return mesh.getVertices();
    }

    public List<Vector2> getTextureVertices() {
        return mesh.getTextureVertices();
    }

    public List<Vector3> getNormals() {
        return mesh.getNormals();
    }

    public List<Polygon> getPolygons() {
        return mesh.getPolygons();
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

    public void setVectors(final Vector3 vS, final Vector3 vR, final Vector3 vT) {
        this.vS = vS;
        this.vR = vR;
        this.vT = vT;
    }

    public void setVertices(final List<Vector3> vertices) {
        mesh.setVertices(vertices);
    }

    public void setTextureVertices(final List<Vector2> vertices) {
        mesh.setTextureVertices(vertices);
    }

    public void setNormals(final List<Vector3> vertices) {
        mesh.setNormals(vertices);
    }

    public void setPolygons(final List<Polygon> vertices) {
        mesh.setPolygons(vertices);
    }

    public void setMesh(final Model mesh) {
        this.mesh = mesh;
    }

    public Model getMesh() {
        return mesh;
    }

    public Model getInitialMesh() {
        return initialMesh;
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

    public Matrix4 getModelMatrix() {
        return GraphicConveyor.translateRotateScale(vS, vR, vT);
    }
}