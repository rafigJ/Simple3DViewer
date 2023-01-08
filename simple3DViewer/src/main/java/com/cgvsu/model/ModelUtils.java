package com.cgvsu.model;

import com.cgvsu.math.Vector3;

import java.util.ArrayList;
import java.util.List;

import static com.cgvsu.math.Vector3f.*;

public class ModelUtils {

    public static void recalculateNormals(Model model) {
        model.getNormals().clear();
        for (int i = 0; i < model.getVertices().size(); i++) {
            try {
                model.getNormals().add(calculateNormalForVertexInModel(model, i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected static Vector3 calculateNormalForPolygon(final Polygon polygon, final Model model){

        List<Integer> vertexIndices = polygon.getVertexIndices();
        int verticesCount = vertexIndices.size();

        Vector3 vector1 = fromTwoPoints(model.getVertices().get(vertexIndices.get(0)), model.getVertices().get(vertexIndices.get(1)));
        Vector3 vector2 = fromTwoPoints(model.getVertices().get(vertexIndices.get(0)), model.getVertices().get(vertexIndices.get(verticesCount - 1)));

        return Vector3.crossProduct(vector1, vector2);
    }

    protected static Vector3 calculateNormalForVertexInModel(final Model model, final int vertexIndex) throws Exception {
        ArrayList<Vector3> saved = new ArrayList<>();
        for (Polygon polygon : model.getPolygons()) {
            if (polygon.getVertexIndices().contains(vertexIndex)) {
                saved.add(calculateNormalForPolygon(polygon, model));
            }
        }
        return sum(saved).divide(saved.size());
    }
}