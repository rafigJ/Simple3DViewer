package com.cgvsu.render_engine;

import com.cgvsu.math.*;
import com.cgvsu.model.Model;
import com.cgvsu.model.ModelOnScene;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.cgvsu.render_engine.GraphicConveyor.*;
import static javax.imageio.ImageIO.read;

public class RenderEngine {

    private static BufferedImage img;

    public static void setImg(BufferedImage img) {
        RenderEngine.img = img;
    }

    static {
        try {
            img = read(new File("123.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static float shade(
            Vector3 norm,
            Camera camera) {

        Vector3 v = Vector3.sub(camera.getTarget(), camera.getPosition());
        float cosine = Vector3.dotProduct(Vector3.normalization(norm), Vector3.normalization(v));
        return Math.abs(cosine);
    }

    private static void texture(
            Matrix4 projectionViewModelMatrix,
            Camera camera,
            GraphicsContext graphicsContext,
            ModelOnScene mesh,
            int width,
            int height,
            float[] zBuffer,
            boolean shadow,
            boolean fill) {

        final int nPolygons = mesh.getPolygons().size();
        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {

            ArrayList<Float> pointsZ = new ArrayList<>();
            ArrayList<Vector2> resultPoints = new ArrayList<>();
            ArrayList<Vector2> VT = new ArrayList<>();
            ArrayList<Float> N = new ArrayList<>();
            for (int vertexInPolygonInd = 0; vertexInPolygonInd < 3; ++vertexInPolygonInd) {
                Vector3 vertex = mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(vertexInPolygonInd));
                Vector2 VTVertex = mesh.getTextureVertices().get(mesh.getPolygons().get(polygonInd).getTextureVertexIndices().get(vertexInPolygonInd));
                Vector3 vertexVecmath = new Vector3(vertex.getX(), vertex.getY(), vertex.getZ());
                Vector3 norm = mesh.getNormals().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(vertexInPolygonInd));
                Vector2 resultPoint = vertexToPoint(multiplyMatrix4ByVector3(projectionViewModelMatrix, vertexVecmath), width, height);
                resultPoints.add(resultPoint);
                pointsZ.add(vertex.getZ());
                VT.add(VTVertex);
                N.add(shade(norm, camera));
            }
            MinMaxValue m = new MinMaxValue(resultPoints);
            for (float y = m.getMinY(); y <= m.getMaxY(); y++) {
                for (float x = m.getMinX(); x <= m.getMaxX(); x++) {
                    getColor(x, y, resultPoints, pointsZ, width, graphicsContext, zBuffer, VT, N, shadow, fill);
                }
            }
        }

    }

    private static void mesh(
            Matrix4 projectionViewModelMatrix,
            GraphicsContext graphicsContext,
            ModelOnScene mesh,
            int width,
            int height) {

        final int nPolygons = mesh.getPolygons().size();
        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            final int nVerticesInPolygon = mesh.getPolygons().get(polygonInd).getVertexIndices().size();
            final int nTVerticesInPolygon = mesh.getPolygons().get(polygonInd).getTextureVertexIndices().size();
            ArrayList<Vector2> resultPoints = new ArrayList<>();
            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                Vector3 vertex = mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(vertexInPolygonInd));
                Vector3 vertexVecMath = new Vector3(vertex.getX(), vertex.getY(), vertex.getZ());
                Vector2 resultPoint = vertexToPoint(multiplyMatrix4ByVector3(projectionViewModelMatrix, vertexVecMath), width, height);
                resultPoints.add(resultPoint);
            }
            for (int vertexInPolygonInd = 1; vertexInPolygonInd < nTVerticesInPolygon - 1; vertexInPolygonInd++) {
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
            final ModelOnScene mesh,
            final int width,
            final int height,
            final boolean[] params) {

        float[] zBuffer = new float[width * height];
        Arrays.fill(zBuffer, Float.NEGATIVE_INFINITY);
        Matrix4 modelMatrix = mesh.getModelMatrix();
        Matrix4 viewMatrix = camera.getViewMatrix();
        Matrix4 projectionMatrix = camera.getProjectionMatrix();
        Matrix4 projectionViewModelMatrix = new Matrix4(projectionMatrix.getData());
        projectionViewModelMatrix.multiply(viewMatrix);
        projectionViewModelMatrix.multiply(modelMatrix);

        if (params[0]) {
            texture(projectionViewModelMatrix, camera, graphicsContext, mesh, width, height, zBuffer, params[1], false);
        } else if (params[3]) {
            texture(projectionViewModelMatrix, camera, graphicsContext, mesh, width, height, zBuffer, params[1], true);
        }
        if (params[2]) {
            mesh(projectionViewModelMatrix, graphicsContext, mesh, width, height);
        }
    }

    private static void getColor(
            float x,
            float y,
            ArrayList<Vector2> resultPoints,
            ArrayList<Float> pointsZ,
            int width,
            GraphicsContext graphicsContext,
            float[] zBuffer,
            ArrayList<Vector2> VT,
            ArrayList<Float> N,
            boolean shadow,
            boolean fill) {

        Triangle triangle = new Triangle(resultPoints);
        Barycentric barycentric = new Barycentric(triangle, x, y);
        Characteristics c = new Characteristics(pointsZ, VT, N, barycentric, shadow);

        if (barycentric.isInside()) {
            int zIndex = (int) (y * width + x);
            if (zBuffer[zIndex] < c.getDepth()) {
                zBuffer[zIndex] = c.getDepth();
                int color = fill ? setDefaultColor(c.getShade()) : setTextureColor(c.getShade(), c.getVX(), c.getVY());
                graphicsContext.getPixelWriter().setArgb((int) x, (int) (y), (color));
            }
        }
    }

    private static int setTextureColor(float shade, float x, float y) {
        int color = img.getRGB((int) ((x * ((float) img.getWidth()))), (int) (y * (float) (img.getHeight())));
        int r = (int) (((color >> 16) & 0xff) * shade);
        int g = (int) (((color >> 8) & 0xff) * shade);
        int b = (int) (((color) & 0xff) * shade);
        return new Color(r, g, b).getRGB();
    }

    private static int setDefaultColor(float shade) {
        int color = Color.CYAN.getRGB();
        int r = (int) (((color >> 16) & 0xff) * shade);
        int g = (int) (((color >> 8) & 0xff) * shade);
        int b = (int) (((color) & 0xff) * shade);
        return new Color(r, g, b).getRGB();
    }


}