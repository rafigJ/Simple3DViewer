package com.cgvsu.Writer;


import com.cgvsu.math.Vector2;
import com.cgvsu.math.Vector3;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObjWriter {

    public static void write(final String name, final Model model) throws IOException {
        final List<String> vertices = verticesToString(model.getVertices());
        final List<String> textureVertices = textureVerticesToString(model.getTextureVertices());
        final List<String> normals = normalsToString(model.getNormals());
        final List<String> poly = polygonsToString(model.getPolygons());

        FileManager.createFileWithText(name, vertices, textureVertices, normals, poly);
    }

    public static List<String> verticesToString(final List<Vector3> array) {
        List<String> l = new ArrayList<>();

        for (int vertexInd = 0; vertexInd < array.size(); vertexInd++) {
            l.add("v " + array.get(vertexInd).getX() + " " + array.get(vertexInd).getY() + " " + array.get(vertexInd).getZ());
        }

        return l;
    }

    public static List<String> textureVerticesToString(final List<Vector2> array) {
        List<String> l = new ArrayList<>();

        for (int textureVertices = 0; textureVertices < array.size(); textureVertices++) {
            l.add("vt " + array.get(textureVertices).getX() + " " + array.get(textureVertices).getY());
        }

        return l;
    }

    public static List<String> normalsToString(final List<Vector3> array) {
        List<String> l = new ArrayList<>();

        for (Vector3 vector3 : array) {
            l.add("vn " + vector3.getX() + " " + vector3.getY() + " " + vector3.getZ());
        }

        return l;
    }

    public static List<String> polygonsToString(final List<Polygon> polygons) {
        List<String> l = new ArrayList<>();
        String s;
        Polygon polygon;
        List<Integer> vertex;
        List<Integer> textureVertex;
        List<Integer> normal;

        for (int poly = 0; poly < polygons.size(); poly++) {
            s = "f";
            polygon = polygons.get(poly);
            vertex = polygon.getVertexIndices();
            textureVertex = polygon.getTextureVertexIndices();
            normal = polygon.getNormalIndices();

            for (int v = 0; v < vertex.size(); v++) {
                s += " ";
                s += vertex.get(v) + 1;

                if (textureVertex.size() != 0) {
                    s += "/";
                    s += textureVertex.get(v) + 1;
                }

                if (normal.size() != 0) {
                    s += "/";
                    s += normal.get(v) + 1;
                }
            }

            l.add(s);
        }

        return l;
    }

}
