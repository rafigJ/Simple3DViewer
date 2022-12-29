package com.cgvsu.model;

import com.cgvsu.math.Vector2;
import com.cgvsu.math.Vector3;
import com.cgvsu.objreader.ReaderExceptions;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private List<Vector3> vertices;
    private List<Vector2> textureVertices;
    public  List<Vector3> normals;
    private List<Polygon> polygons;

    public Model(final List<Vector3> vertices, final List<Vector2> textureVertices, final List<Vector3> normals, final List<Polygon> polygons) {
        this.vertices = vertices;
        this.textureVertices = textureVertices;
        this.normals = normals;
        this.polygons = polygons;
    }

    public Model() {
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
        //ModelUtils.recalculateNormals(this);
        return normals;
    }

    public List<Polygon> getPolygons() {
        return polygons;
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

    public boolean checkConsistency() {
        for (int i = 0; i < polygons.size(); i++) {
            List<Integer> vertexIndices = polygons.get(i).getVertexIndices();
            List<Integer> textureVertexIndices = polygons.get(i).getTextureVertexIndices();
            List<Integer> normalIndices = polygons.get(i).getNormalIndices();
            if (vertexIndices.size() != textureVertexIndices.size()
                    && vertexIndices.size() != 0 && textureVertexIndices.size() != 0) {
                throw new ReaderExceptions.NotDefinedUniformFormatException(
                        "The unified format for specifying polygon descriptions is not defined.");
            }
            if (vertexIndices.size() != normalIndices.size()
                    && vertexIndices.size() != 0 &&  normalIndices.size() != 0) {
                throw new ReaderExceptions.NotDefinedUniformFormatException(
                        "The unified format for specifying polygon descriptions is not defined.");
            }
            if (normalIndices.size() != textureVertexIndices.size()
                    && normalIndices.size() != 0 && textureVertexIndices.size() != 0) {
                throw new ReaderExceptions.NotDefinedUniformFormatException(
                        "The unified format for specifying polygon descriptions is not defined.");
            }
            for (int j = 0; j < vertexIndices.size(); j++) {
                if (vertexIndices.get(j) >= vertices.size()) {
                    throw new ReaderExceptions.FaceException(
                            "Polygon description is wrong.", i + 1);
                }
            }
            for (int j = 0; j < textureVertexIndices.size(); j++) {
                if (textureVertexIndices.get(j) >= textureVertices.size()) {
                    throw new ReaderExceptions.FaceException(
                            "Polygon description is wrong.", i + 1);
                }
            }
            for (int j = 0; j < normalIndices.size(); j++) {
                if (normalIndices.get(j) >= normals.size()) {
                    throw new ReaderExceptions.FaceException(
                            "Polygon description is wrong.", i + 1);
                }
            }
        }
        return true;
    }
    public void triangulate() {
        List<Polygon> triangulatedPolygons = new ArrayList<>();
        List<Vector2> textureVertices = new ArrayList<>();
        for (Polygon polygon : polygons) {
            List<Integer> vertexIndices = polygon.getVertexIndices();
            List<Integer> TextureVertexIndices = polygon.getTextureVertexIndices();
            if (vertexIndices.size() > 3) {
                for (int i = 2; i < vertexIndices.size(); i++) {
                    Polygon triangle = new Polygon();
                    triangle.getVertexIndices().add(vertexIndices.get(0));
                    triangle.getVertexIndices().add(vertexIndices.get(i - 1));
                    triangle.getVertexIndices().add(vertexIndices.get(i));
                    triangulatedPolygons.add(triangle);
                }
            } else {
                triangulatedPolygons.add(polygon);

            }
        }
        polygons = triangulatedPolygons;

    }
}