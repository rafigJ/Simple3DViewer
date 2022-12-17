package com.cgvsu.render_engine;
import com.cgvsu.math.Matrix4;
import com.cgvsu.math.Vector2;
import com.cgvsu.math.Vector3;
import com.cgvsu.math.Vector4;

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
        Vector3 resultZ = Vector3.sub(target, eye);
        Vector3 resultX = Vector3.crossProduct(up, resultZ);
        Vector3 resultY = Vector3.crossProduct(resultZ, resultX);

        resultX.normalization();
        resultY.normalization();
        resultZ.normalization();

        float[][] matrix = new float[][]{
                {resultX.getX(), resultY.getX(), resultZ.getX(), 0},
                {resultX.getY(), resultY.getY(), resultZ.getY(), 0},
                {resultX.getZ(), resultY.getZ(), resultZ.getZ(), 0},
                {-resultX.dotProduct(eye), -resultY.dotProduct(eye), -resultZ.dotProduct(eye), 1}
        };
        Matrix4 matrix4 = new Matrix4(matrix);
        matrix4.transposeInPlace();
        return matrix4;
    }

    public static Matrix4 perspective( // переделать
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        Matrix4 result = new Matrix4();
        float tangentMinusOnDegree = (float) (1.0F / (Math.tan(fov * 0.5F)));
        result.getData()[0][0] = tangentMinusOnDegree / aspectRatio;
        result.getData()[1][1] = tangentMinusOnDegree;
        result.getData()[2][2] = (farPlane + nearPlane) / (farPlane - nearPlane);
        result.getData()[3][2] = 1.0F;
        result.getData()[2][3] = 2 * (nearPlane * farPlane) / (nearPlane - farPlane);
        return result;
    }

    public static Vector3 multiplyMatrix4ByVector3(final Matrix4 matrix, final Vector3 vertex) { // Переделывал
        Vector4 v4 = new Vector4(vertex.getX(), vertex.getY(), vertex.getZ(), 1.0F);
        v4 = matrix.multiply(v4);
        return new Vector3(v4.getX() / v4.getM(), v4.getY() / v4.getM(), v4.getZ() / v4.getM());
    }

    public static Vector2 vertexToPoint(final Vector3 vertex, final int width, final int height) {
        return new Vector2(vertex.getX() * width + width / 2.0F, -vertex.getY() * height + height / 2.0F);
    }
}
