package com.cgvsu.math;

import org.junit.jupiter.api.Test;
import java.util.Arrays;

class Matrix4Test {
    float[][] data1 = new float[][]{
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 16}
    };

    float[][] data2 = new float[][] {
            {1, 1, 1, 1},
            {2, 2, 2, 2},
            {3, 3, 3, 3},
            {4, 4, 4, 4}
    };

    float eps = 1e-7F;

    @Test
    void multiply(){
        Matrix4 matrix = new Matrix4(data1);
        Matrix4 matrix2 = new Matrix4(data2);

        matrix.multiply(matrix2);

        float[][] dataRes = new float[][] {
                {30,30,30,30},
                {70,70,70,70},
                {110,110,110,110},
                {150,150,150,150}
        };

        Matrix4 matrixRes = new Matrix4(dataRes);

        assert Arrays.deepEquals(matrix.getData(), matrixRes.getData());
    }

    @Test
    void transposeInPlace() {
        Matrix4 matrix = new Matrix4(data1);

        matrix.transposeInPlace();

        float[][] dataRes = new float[][] {
                {1,5,9,13},
                {2,6,10,14},
                {3,7,11,15},
                {4,8,12,16}
        };

        Matrix4 matrixRes = new Matrix4(dataRes);

        assert Arrays.deepEquals(matrix.getData(), matrixRes.getData());
    }

    @Test
    void multiply2() {
        Matrix4 matrix = new Matrix4(data1);
        Vector4 v4 = new Vector4(2,2,2,2);
        Vector4 vF = new Vector4(20, 52, 84, 116);

        v4 = matrix.multiply(v4);

        assert (v4.getX() - vF.getX()) < eps;
        assert (v4.getY() - vF.getY()) < eps;
        assert (v4.getZ() - vF.getZ()) < eps;
        assert (v4.getM() - vF.getM()) < eps;
    }

}