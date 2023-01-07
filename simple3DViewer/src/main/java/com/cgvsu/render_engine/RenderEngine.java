package com.cgvsu.render_engine;

import com.cgvsu.math.*;
import com.cgvsu.model.ModelOnScene;
import com.cgvsu.tools.Characteristics;
import com.cgvsu.tools.TextureSettings;
import com.cgvsu.tools.Triangle;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static com.cgvsu.render_engine.GraphicConveyor.*;

public class RenderEngine {

    private static float shade(
            Vector3 normal,
            Camera camera) {

        Vector3 v = Vector3.sub(camera.getTarget(), camera.getPosition());
        float cosine = Vector3.dotProduct(Vector3.normalization(normal), Vector3.normalization(v));
        return Float.max(0.8f, Math.abs(cosine));
    }

    private static void texture(
            final Matrix4 projectionViewModelMatrix,
            final Camera camera,
            final GraphicsContext graphicsContext,
            final ModelOnScene model,
            final int width,
            final int height,
            final float[][] zBuffer,
            final boolean shadow,
            final boolean fill,
            BufferedImage img) {

        final int nPolygons = model.getPolygons().size();
        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {

            final ArrayList<Float> zCoordinates = new ArrayList<>();
            final ArrayList<Vector2> resultPoints = new ArrayList<>();
            final ArrayList<Vector2> textureCoordinates = new ArrayList<>();
            final ArrayList<Float> shadows = new ArrayList<>();

            for (int vertexInPolygonInd = 0; vertexInPolygonInd < 3; ++vertexInPolygonInd) {
                Vector3 vertex = model.getVertices().get(model.getPolygons().get(polygonInd).getVertexIndices().get(vertexInPolygonInd));
                Vector2 textureVertex = model.getTextureVertices().get(model.getPolygons().get(polygonInd).getTextureVertexIndices().get(vertexInPolygonInd));
                Vector3 vertexVecMath = new Vector3(vertex.getX(), vertex.getY(), vertex.getZ());
                Vector3 normal = model.getNormals().get(model.getPolygons().get(polygonInd).getVertexIndices().get(vertexInPolygonInd));
                Vector2 resultPoint = vertexToPoint(multiplyMatrix4ByVector3(projectionViewModelMatrix, vertexVecMath), width, height);
                resultPoints.add(resultPoint);
                zCoordinates.add(vertex.getZ());
                textureCoordinates.add(textureVertex);
                shadows.add(shade(normal, camera));
            }
            Triangle triangle = new Triangle(resultPoints);
            MinMaxValue m = new MinMaxValue(resultPoints, width, height);
            for (int y = m.getMinY(); y <= m.getMaxY(); y++) {
                for (int x = m.getMinX(); x <= m.getMaxX(); x++) {
                    textureMapping(x, y, triangle, zCoordinates, graphicsContext, zBuffer, textureCoordinates, shadows, shadow, fill, img);
                }
            }
        }
    }

    private static void mesh(
            final Matrix4 projectionViewModelMatrix,
            final GraphicsContext graphicsContext,
            final ModelOnScene model,
            final int width,
            final int height) {

        final int nPolygons = model.getPolygons().size();
        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            final int nVerticesInPolygon = model.getPolygons().get(polygonInd).getVertexIndices().size();
            ArrayList<Vector2> resultPoints = new ArrayList<>();
            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                Vector3 vertex = model.getVertices().get(model.getPolygons().get(polygonInd).getVertexIndices().get(vertexInPolygonInd));
                Vector3 vertexVecMath = new Vector3(vertex.getX(), vertex.getY(), vertex.getZ());
                Vector2 resultPoint = vertexToPoint(multiplyMatrix4ByVector3(projectionViewModelMatrix, vertexVecMath), width, height);
                resultPoints.add(resultPoint);
            }
            for (int vertexInPolygonInd = 1; vertexInPolygonInd < nVerticesInPolygon; vertexInPolygonInd++) {
                graphicsContext.strokeLine(
                        resultPoints.get(vertexInPolygonInd - 1).getX(),
                        resultPoints.get(vertexInPolygonInd - 1).getY(),
                        resultPoints.get(vertexInPolygonInd).getX(),
                        resultPoints.get(vertexInPolygonInd).getY());
            }
            if (nVerticesInPolygon > 0)
                graphicsContext.strokeLine(
                        resultPoints.get(nVerticesInPolygon - 1).getX(),
                        resultPoints.get(nVerticesInPolygon - 1).getY(),
                        resultPoints.get(0).getX(),
                        resultPoints.get(0).getY());
        }
    }

    public static void render(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final ModelOnScene model,
            final int width,
            final int height,
            BufferedImage img,
            final TextureSettings settings) {

        float[][] zBuffer = new float[width][height];
        for (int i = 0; i < zBuffer.length; i++) {
            for (int j = 0; j < zBuffer[0].length; j++) {
                zBuffer[i][j] = Float.NEGATIVE_INFINITY;
            }
        }

        Matrix4 modelMatrix = model.getModelMatrix();
        Matrix4 viewMatrix = camera.getViewMatrix();
        Matrix4 projectionMatrix = camera.getProjectionMatrix();
        Matrix4 projectionViewModelMatrix = new Matrix4(projectionMatrix.getData());
        projectionViewModelMatrix.multiply(viewMatrix);
        projectionViewModelMatrix.multiply(modelMatrix);

        if (settings.texture && img != null) {
            texture(projectionViewModelMatrix, camera, graphicsContext, model, width, height, zBuffer, settings.shadow, false, img);
        } else if (settings.fill) {
            texture(projectionViewModelMatrix, camera, graphicsContext, model, width, height, zBuffer, settings.shadow, true, img);
        }
        if (settings.mesh) {
            mesh(projectionViewModelMatrix, graphicsContext, model, width, height);
        }
    }

    private static void textureMapping(
            final int x,
            final int y,
            final Triangle triangle,
            final ArrayList<Float> zCoordinates,
            final GraphicsContext graphicsContext,
            final float[][] zBuffer,
            final ArrayList<Vector2> textureVertexes,
            final ArrayList<Float> shadows,
            final boolean shadow,
            final boolean fill,
            final BufferedImage img) {


        Barycentric barycentric = new Barycentric(triangle, x, y);
        Characteristics c = new Characteristics(zCoordinates, textureVertexes, shadows, barycentric, shadow);

        if (barycentric.isInside() ) {
            if (zBuffer[x][y] < c.getDepth()) {
                zBuffer[x][y] = c.getDepth();
                int color = fill ? setDefaultColor(c.getShade()) : setTexture(c.getShade(), c.getVX(), c.getVY(), img);
                graphicsContext.getPixelWriter().setArgb(x, y, color);
            }
        }
    }

    private static int setTexture(
            final float shade,
            final float x,
            final float y,
            BufferedImage img) {
        int color = img.getRGB((int) ((x * ((float) img.getWidth()))), (int) (y * (float) (img.getHeight())));
        int r = (int) (((color >> 16) & 0xff) * shade);
        int g = (int) (((color >> 8) & 0xff) * shade);
        int b = (int) (((color) & 0xff) * shade);
        return new Color(r, g, b).getRGB();
    }

    private static int setDefaultColor(final float shade) {
        int color = Color.CYAN.getRGB();
        int r = (int) (((color >> 16) & 0xff) * shade);
        int g = (int) (((color >> 8) & 0xff) * shade);
        int b = (int) (((color) & 0xff) * shade);
        return new Color(r, g, b).getRGB();
    }


}