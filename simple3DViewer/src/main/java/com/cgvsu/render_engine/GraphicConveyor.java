package com.cgvsu.render_engine;
import com.cgvsu.math.Matrix4;
import com.cgvsu.math.Point2;
import com.cgvsu.math.Vector3;

import javax.vecmath.*;

public class GraphicConveyor {

    public static Matrix4 rotateScaleTranslate() { // переделать
        float[][] matrix = new float[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
        return new Matrix4(matrix);
    }

    public static Matrix4 lookAt(Vector3 eye, Vector3 target) { // переделать
        return lookAt(eye, target, new Vector3(0F, 1.0F, 0F));
    }

    public static Matrix4 lookAt(Vector3 eye, Vector3 target, Vector3 up) { // переделать
        Vector3 resultX = new Vector3();
        Vector3 resultY = new Vector3();
        Vector3 resultZ = new Vector3();

        resultZ.sub(target, eye);
        resultX.vectorProduct(up, resultZ);
        resultY.vectorProduct(resultZ, resultX);

        resultX.normalization();
        resultY.normalization();
        resultZ.normalization();

        float[][] matrix = new float[][]{
                {resultX.getX(), resultY.getX(), resultZ.getX(), 0},
                {resultX.getY(), resultY.getY(), resultZ.getY(), 0},
                {resultX.getZ(), resultY.getZ(), resultZ.getZ(), 0},
                {-resultX.scalarProduct(eye), -resultY.scalarProduct(eye), -resultZ.scalarProduct(eye), 1}
        };
        return new Matrix4(matrix);
    }

    public static Matrix4 perspective( // переделать
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        Matrix4 result = new Matrix4();
        float tangentMinusOnDegree = (float) (1.0F / (Math.tan(fov * 0.5F)));
        result.getMatrix()[0][0] = tangentMinusOnDegree / aspectRatio;
        result.getMatrix()[1][1] = tangentMinusOnDegree;
        result.getMatrix()[2][2] = (farPlane + nearPlane) / (farPlane - nearPlane);
        result.getMatrix()[2][3] = 1.0F;
        result.getMatrix()[3][2] = 2 * (nearPlane * farPlane) / (nearPlane - farPlane);
        return result;
    }

    public static Vector3 multiplyMatrix4ByVector3(final Matrix4 matrix, final Vector3 vertex) { // переделать
        final float x = (vertex.getX() * matrix.getMatrix()[0][0]) + (vertex.getY() * matrix.getMatrix()[1][0]) +
                (vertex.getZ() * matrix.getMatrix()[2][0]) + matrix.getMatrix()[3][0];
        final float y = (vertex.getX() * matrix.getMatrix()[0][1]) + (vertex.getY() * matrix.getMatrix()[1][1]) +
                (vertex.getZ() * matrix.getMatrix()[2][1]) + matrix.getMatrix()[3][1];
        final float z = (vertex.getX() * matrix.getMatrix()[0][2]) + (vertex.getY() * matrix.getMatrix()[1][2]) +
                (vertex.getZ() * matrix.getMatrix()[2][2]) + matrix.getMatrix()[3][2];
        final float w = (vertex.getX() * matrix.getMatrix()[0][3]) + (vertex.getY() * matrix.getMatrix()[1][3]) +
                (vertex.getZ() * matrix.getMatrix()[2][3]) + matrix.getMatrix()[3][3];
        return new Vector3(x / w, y / w, z / w);
    }

    public static Point2 vertexToPoint(final Vector3 vertex, final int width, final int height) {
        return new Point2(vertex.getX() * width + width / 2.0F, -vertex.getY() * height + height / 2.0F);
    }
}
