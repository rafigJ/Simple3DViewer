package com.cgvsu.render_engine;

import java.util.ArrayList;

import com.cgvsu.GuiController;
import com.cgvsu.math.Matrix4;
import com.cgvsu.math.Vector2;
import com.cgvsu.math.Vector3;
import com.cgvsu.model.ModelOnScene;
import javafx.scene.canvas.GraphicsContext;
import com.cgvsu.model.Model;

import static com.cgvsu.render_engine.GraphicConveyor.*;

public class RenderEngine {

    public static void render(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final Model mesh,
            final ModelOnScene modelOnScene,
            final int width,
            final int height)
    {
        Matrix4 modelMatrix;
        if(modelOnScene == null) {
            modelMatrix = rotateScaleTranslate();
        } else {
            modelMatrix = rotateScaleTranslate(modelOnScene.getVS(), modelOnScene.getVR(), modelOnScene.getVT());
        }
        Matrix4 viewMatrix = camera.getViewMatrix();
        Matrix4 projectionMatrix = camera.getProjectionMatrix();

        Matrix4 projectionViewModelMatrix = new Matrix4(projectionMatrix.getData());
        projectionViewModelMatrix.multiply(viewMatrix);
        projectionViewModelMatrix.multiply(modelMatrix);

        final int nPolygons = mesh.getPolygons().size();
        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            final int nVerticesInPolygon = mesh.getPolygons().get(polygonInd).getVertexIndices().size();

            ArrayList<Vector2> resultPoints = new ArrayList<>();
            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                Vector3 vertex = mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(vertexInPolygonInd));

                Vector3 vertexVecmath = new Vector3(vertex.getX(), vertex.getY(), vertex.getZ());   

                Vector2 resultPoint = vertexToPoint(multiplyMatrix4ByVector3(projectionViewModelMatrix, vertexVecmath), width, height);
                resultPoints.add(resultPoint);
            }

            for (int vertexInPolygonInd = 1; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
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