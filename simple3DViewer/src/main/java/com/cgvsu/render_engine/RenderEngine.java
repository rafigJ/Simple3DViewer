package com.cgvsu.render_engine;

import com.cgvsu.math.Barycentric;
import com.cgvsu.math.Matrix4;
import com.cgvsu.math.Vector2;
import com.cgvsu.math.Vector3;
import com.cgvsu.model.Model;
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


    static BufferedImage img;

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
            Model mesh,
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
            float minX = (float) Math.max(0, Math.ceil(Math.min(resultPoints.get(0).getX(), Math.min(resultPoints.get(1).getX(), resultPoints.get(2).getX()))));
            float maxX = (float) Math.min(width - 1, Math.floor(Math.max(resultPoints.get(0).getX(), Math.max(resultPoints.get(1).getX(), resultPoints.get(2).getX()))));
            float minY = (float) Math.max(0, Math.ceil(Math.min(resultPoints.get(0).getY(), Math.min(resultPoints.get(1).getY(), resultPoints.get(2).getY()))));
            float maxY = (float) Math.min(height - 1, Math.floor(Math.max(resultPoints.get(0).getY(), Math.max(resultPoints.get(1).getY(), resultPoints.get(2).getY()))));
            for (float y = minY; y <= maxY; y++) {
                for (float x = minX; x <= maxX; x++) {
                    getColor(x, y, resultPoints.get(0).getX(), resultPoints.get(0).getY(), resultPoints.get(1).getX(), resultPoints.get(1).getY(),
                            resultPoints.get(2).getX(), resultPoints.get(2).getY(), pointsZ.get(0), pointsZ.get(1), pointsZ.get(2), width, graphicsContext, zBuffer, VT, N, shadow, fill);
                }
            }
        }

    }

    private static void mesh(
            Matrix4 projectionViewModelMatrix,
            GraphicsContext graphicsContext,
            Model mesh,
            int width,
            int height) {

        final int nPolygons = mesh.getPolygons().size();
        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            final int nVerticesInPolygon = mesh.getPolygons().get(polygonInd).getVertexIndices().size();
            final int nTVerticesInPolygon = mesh.getPolygons().get(polygonInd).getTextureVertexIndices().size();
            ArrayList<Vector2> resultPoints = new ArrayList<>();
            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                Vector3 vertex = mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(vertexInPolygonInd));
                Vector3 vertexVecmath = new Vector3(vertex.getX(), vertex.getY(), vertex.getZ());
                Vector2 resultPoint = vertexToPoint(multiplyMatrix4ByVector3(projectionViewModelMatrix, vertexVecmath), width, height);
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
            final Model mesh,
            final int width,
            final int height,
            final boolean[] params) {

        float[] zBuffer = new float[width * height];
        Arrays.fill(zBuffer, Float.NEGATIVE_INFINITY);
        Matrix4 modelMatrix = rotateScaleTranslate();
        Matrix4 viewMatrix = camera.getViewMatrix();
        Matrix4 projectionMatrix = camera.getProjectionMatrix();
        Matrix4 projectionViewModelMatrix = new Matrix4(projectionMatrix.getData());
        projectionViewModelMatrix.multiply(viewMatrix);
        projectionViewModelMatrix.multiply(modelMatrix);

        if (params[0] && !params[3]) {
            texture(projectionViewModelMatrix, camera, graphicsContext, mesh, width, height, zBuffer, params[1], false);
        }
        if (params[0] && params[3]) {
            texture(projectionViewModelMatrix, camera, graphicsContext, mesh, width, height, zBuffer, params[1], false);
        }
        if (params[3] && !params[0]) {
            texture(projectionViewModelMatrix, camera, graphicsContext, mesh, width, height, zBuffer, params[1], true);
        }
        if (params[2]) {
            mesh(projectionViewModelMatrix, graphicsContext, mesh, width, height);
        }

    }

    private static void getColor(
            float x,
            float y,
            float x1,
            float y1,
            float x2,
            float y2,
            float x3,
            float y3,
            float z1,
            float z2,
            float z3,
            int width,
            GraphicsContext graphicsContext,
            float[] zBuffer,
            ArrayList<Vector2> VT,
            ArrayList<Float> N,
            boolean shadow,
            boolean fill) {

        Barycentric barycentric = new Barycentric(new Triangle(x1, y1, x2, y2, x3, y3), x, y);
        if (barycentric.isInside()) {
            float depth = barycentric.getL1() * z1 + barycentric.getL2() * z2 + barycentric.getL3() * z3;
            float shade = shadow ? barycentric.getL1() * N.get(0) + barycentric.getL2() * N.get(1) + barycentric.getL3() * N.get(2) : 1;
            float VX = barycentric.getL1() * VT.get(0).getX() + barycentric.getL2() * VT.get(1).getX() + barycentric.getL3() * VT.get(2).getX();
            float VY = 1 - (barycentric.getL1() * VT.get(0).getY() + barycentric.getL2() * VT.get(1).getY() + barycentric.getL3() * VT.get(2).getY());
            int zIndex = (int) (y * width + x);
            if (zBuffer[zIndex] < depth) {
                zBuffer[zIndex] = depth;
                int color = fill ? setDefaultColor(shade) : setTextureColor(shade, VX, VY);
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
