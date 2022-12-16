package com.cgvsu.render_engine;

import java.util.ArrayList;

import com.cgvsu.math.Matrix4;
import com.cgvsu.math.Vector3;
import javafx.scene.canvas.GraphicsContext;
import com.cgvsu.model.Model;

import static com.cgvsu.render_engine.GraphicConveyor.*;

public class RenderEngine {

    public static void render(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final Model mesh,
            final int width,
            final int height)
    {
        Matrix4 modelMatrix = rotateScaleTranslate(); // переделать кусок
        Matrix4 viewMatrix = camera.getViewMatrix();
        Matrix4 projectionMatrix = camera.getProjectionMatrix();

//        projectionMatrix.increaseMatrix(viewMatrix);
//        projectionMatrix.increaseMatrix(modelMatrix);

        Matrix4 modelViewProjectionMatrix = new Matrix4(modelMatrix.getData());
        modelViewProjectionMatrix.multiply(viewMatrix);
        modelViewProjectionMatrix.multiply(projectionMatrix); // конец

        final int nPolygons = mesh.getPolygons().size();
        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            final int nVerticesInPolygon = mesh.getPolygons().get(polygonInd).getVertexIndices().size();

            ArrayList<Point2> resultPoints = new ArrayList<>();
            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                Vector3 vertex = mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(vertexInPolygonInd));

                Vector3 vertexVecmath = new Vector3(vertex.getX(), vertex.getY(), vertex.getZ());

                Point2 resultPoint = vertexToPoint(multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertexVecmath), width, height); // переделать
                resultPoints.add(resultPoint);
            }

            for (int vertexInPolygonInd = 2; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                graphicsContext.strokeLine(
                        resultPoints.get(vertexInPolygonInd - 1).getX(),
                        resultPoints.get(vertexInPolygonInd - 1).getY(),
                        resultPoints.get(vertexInPolygonInd).getX(),
                        resultPoints.get(vertexInPolygonInd).getY());
            }

            if (nVerticesInPolygon > 0) {
                graphicsContext.strokeLine(
                        resultPoints.get(nVerticesInPolygon - 1).getX(),
                        resultPoints.get(nVerticesInPolygon - 1).getY(),
                        resultPoints.get(0).getX(),
                        resultPoints.get(0).getY());
            }
        }
    }
}