package com.cgvsu.Writer;


import com.cgvsu.math.Vector2;
import com.cgvsu.math.Vector3;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.utils.FileManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObjWriter {

    public static void write(final String name, final Model model) throws IOException {
        final List<String> vertices = verticesToString(model.getVertices());
        final List<String> textureVertices = textureVerticesToString(model.getTextureVertices());
        final List<String> normals = normalsToString(model.normals);
        final List<String> poly = polygonsToString((ArrayList<Polygon>) model.getPolygons());

        FileManager.createFileWithText(name, vertices, textureVertices, normals, poly);
    }

    public static ArrayList<String> verticesToString(final List<Vector3> array) {
        ArrayList<String> l = new ArrayList<String>();

        for (int vertexInd = 0; vertexInd < array.size(); vertexInd++) {
            l.add("v " + array.get(vertexInd).getX() + " " + array.get(vertexInd).getY() + " " + array.get(vertexInd).getZ());
        }

        return l;
    }

    public static ArrayList<String> textureVerticesToString(final List<Vector2> array) {
        ArrayList<String> l = new ArrayList<String>();

        for (int textureVertices = 0; textureVertices < array.size(); textureVertices++) {
            l.add("vt " + array.get(textureVertices).getX() + " " + array.get(textureVertices).getY());
        }

        return l;
    }

    public static ArrayList<String> normalsToString(final List<Vector3> array) {
        ArrayList<String> l = new ArrayList<String>();

        for (Vector3 vector3f : array) {
            l.add("vn " + vector3f.getX() + " " + vector3f.getY() + " " + vector3f.getZ());
        }

        return l;
    }

    public static ArrayList<String> polygonsToString(final ArrayList<Polygon> polygons) {
        ArrayList<String> l = new ArrayList<String>();
        StringBuilder s;
        Polygon polygon = new Polygon();
        ArrayList<Integer> vertex = new ArrayList<Integer>();
        ArrayList<Integer> textureVertex = new ArrayList<Integer>();
        ArrayList<Integer> normal = new ArrayList<Integer>();

        for (int poly = 0; poly < polygons.size(); poly++) {
            s = new StringBuilder("f");
            polygon = polygons.get(poly);
            vertex = (ArrayList<Integer>) polygon.getVertexIndices();
            textureVertex = (ArrayList<Integer>) polygon.getTextureVertexIndices();
            normal = (ArrayList<Integer>) polygon.getNormalIndices();

            for (int v = 0; v < vertex.size(); v++) {
                s.append(" ");
                s.append(vertex.get(v) + 1);

                if (textureVertex.size() != 0) {
                    s.append("/");
                    s.append(textureVertex.get(v) + 1);
                }

                if (normal.size() != 0) {
                    s.append("/");
                    s.append(normal.get(v) + 1);
                }
            }

            l.add(s.toString());
        }

        return l;
    }

}

