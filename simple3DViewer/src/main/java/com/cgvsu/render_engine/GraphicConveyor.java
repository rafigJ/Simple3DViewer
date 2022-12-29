package com.cgvsu.render_engine;
import com.cgvsu.math.*;

public class GraphicConveyor {

    public static Matrix4 rotateScaleTranslate(Vector3 vS, Vector3 vR, Vector3 vT) {
        return Matrix4.multiply(Matrix4.multiply(scale(vS), rotate(vR)), translate(vT));
    }

    public static Matrix4 rotateScaleTranslate() {
        float[][] matrix = new float[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
        return new Matrix4(matrix);
    }

    public static Matrix4 lookAt(Vector3 eye, Vector3 target) {
        return lookAt(eye, target, new Vector3(0F, 1.0F, 0F));
    }

    public static Matrix4 lookAt(Vector3 eye, Vector3 target, Vector3 up) {
        Vector3 resultZ = Vector3.sub(target, eye);
        Vector3 resultX = Vector3.crossProduct(up, resultZ);
        Vector3 resultY = Vector3.crossProduct(resultZ, resultX);

        resultX.normalization();
        resultY.normalization();
        resultZ.normalization();

        float[][] matrix = new float[][] {
                {resultX.getX(), resultX.getY(), resultX.getZ(), -resultX.dotProduct(eye)},
                {resultY.getX(), resultY.getY(), resultY.getZ(), -resultY.dotProduct(eye)},
                {resultZ.getX(), resultZ.getY(), resultZ.getZ(), -resultZ.dotProduct(eye)},
                {0, 0, 0, 1}
        };

        return new Matrix4(matrix);
    }

    public static Matrix4 perspective(
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

    public static Vector3 multiplyMatrix4ByVector3(final Matrix4 matrix, final Vector3 vertex) {
        Vector4 v4 = new Vector4(vertex.getX(), vertex.getY(), vertex.getZ(), 1.0F);
        v4 = matrix.multiply(v4);
        return new Vector3(v4.getX() / v4.getM(), v4.getY() / v4.getM(), v4.getZ() / v4.getM());
    }

    public static Vector2 vertexToPoint(final Vector3 vertex, final int width, final int height) {
        return new Vector2(vertex.getX() * width + width / 2.0F, -vertex.getY() * height + height / 2.0F);
    }

    public static Matrix4 scale(final Vector3 vS) {
        float[][] matrix = new float[][]{
                {vS.getX(), 0, 0, 0},
                {0, vS.getY(), 0, 0},
                {0, 0, vS.getZ(), 0},
                {0, 0, 0, 1}
        };
        return new Matrix4(matrix);
    }

    public static Matrix4 rotate(final Vector3 vR) {
        final float sinX = (float) Math.sin(vR.getX());
        final float cosX = (float) Math.cos(vR.getX());
        final float sinY = (float) Math.sin(vR.getY());
        final float cosY = (float) Math.cos(vR.getY());
        final float sinZ = (float) Math.sin(vR.getX());
        final float cosZ = (float) Math.cos(vR.getX());

        float[][] matrix = new float[][] {
                {cosY * cosZ, sinX * sinY * cosZ - cosX * sinZ, cosX * sinY * cosZ + sinX * sinZ, 0},
                {cosY * sinZ, sinX * sinY * sinZ + cosX * cosZ, cosX * sinY * sinZ - sinX * cosZ, 0},
                {-sinY, sinX * cosY, cosX * cosY, 0},
                {0, 0, 0, 0}
        };

        return new Matrix4(matrix);
    }

    public static Matrix4 translate(final Vector3 vT) {
        float[][] matrix = new float[][]{
                {1, 0, 0, vT.getX()},
                {0, 1, 0, vT.getY()},
                {0, 0, 1, vT.getZ()},
                {0, 0, 0, 1}
        };
        return new Matrix4(matrix);
    }
}
