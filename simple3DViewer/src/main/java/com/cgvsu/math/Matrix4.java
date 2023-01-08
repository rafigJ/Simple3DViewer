package com.cgvsu.math;

public class Matrix4 {
    private float[][] data;
    static final float EPS = 0.00000001f;

    public Matrix4(float[][] matrix) {
        this.data = matrix;
    }

    public Matrix4() {
        this.data = new float[4][4];
    }

    public float[][] getData() {
        return data;
    }

    public void sum(final Matrix4 matrix1) {
        for (int col = 0; col < matrix1.getData()[0].length; col++) {
            for (int row = 0; row < matrix1.getData().length; row++) {
                data[row][col] += matrix1.getData()[row][col];
            }
        }
    }

    public static Matrix4 sum(final Matrix4 matrix1, final Matrix4 matrix2) {
        Matrix4 matrixResult = new Matrix4();
        for (int col = 0; col < matrix1.getData()[0].length; col++) {
            for (int row = 0; row < matrix1.getData().length; row++) {
                matrixResult.getData()[row][col] = matrix1.getData()[row][col] + matrix2.getData()[row][col];
            }
        }
        return matrixResult;
    }

    public void sub(final Matrix4 matrix1) {
        for (int col = 0; col < matrix1.getData()[0].length; col++) {
            for (int row = 0; row < matrix1.getData().length; row++) {
                data[row][col] -= matrix1.getData()[row][col];
            }
        }
    }

    public static Matrix4 sub(final Matrix4 matrix1, final Matrix4 matrix2) {
        Matrix4 matrixResult = new Matrix4();
        for (int col = 0; col < matrix1.getData()[0].length; col++) {
            for (int row = 0; row < matrix1.getData().length; row++) {
                matrixResult.getData()[row][col] = matrix1.getData()[row][col] - matrix2.getData()[row][col];
            }
        }
        return matrixResult;
    }

    public Vector4 multiply(final Vector4 vector) {
        float[][] matrixResult = new float[4][1];
        final float[][] vectorMatrix = new float[4][1];
        vectorMatrix[0][0] = vector.getX();
        vectorMatrix[1][0] = vector.getY();
        vectorMatrix[2][0] = vector.getZ();
        vectorMatrix[3][0] = vector.getM();

        for (int row = 0; row < data.length; row++) {
            for (int col = 0; col < data[0].length; col++) {
                matrixResult[row][0] += data[row][col] * vectorMatrix[col][0];
            }
        }
        return new Vector4(matrixResult[0][0], matrixResult[1][0], matrixResult[2][0], matrixResult[3][0]);
    }

    public static Vector4 multiply(final Matrix4 matrix, final Vector4 vector) {
        float[][] matrixResult = new float[4][1];
        float[][] vectorMatrix = new float[4][1];
        vectorMatrix[0][0] = vector.getX();
        vectorMatrix[1][0] = vector.getY();
        vectorMatrix[2][0] = vector.getZ();
        vectorMatrix[3][0] = vector.getM();

        for (int row = 0; row < matrix.getData().length; row++) {
            for (int col = 0; col < matrix.getData()[0].length; col++) {
                matrixResult[row][0] += matrix.getData()[row][col] * vectorMatrix[col][0];
            }
        }
        return new Vector4(matrixResult[0][0], matrixResult[1][0], matrixResult[2][0], matrixResult[3][0]);
    }

    public void multiply(final Matrix4 matrix1) {
        float[][] matrixResult = new float[4][4];

        for (int row = 0; row < data.length; row++) {
            for (int col = 0; col < matrix1.getData()[0].length; col++) {
                for (int i = 0; i < matrix1.getData().length; i++) {
                    matrixResult[row][col] += (data[row][i] * matrix1.getData()[i][col]);
                }
            }
        }
        data = matrixResult;
    }

    public static Matrix4 multiply(final Matrix4 matrix1,final Matrix4 matrix2) {
        Matrix4 matrixResult = new Matrix4();
        for (int row = 0; row < matrix1.getData().length; row++) {
            for (int col = 0; col < matrix2.getData()[0].length; col++) {
                for (int i = 0; i < matrix2.getData().length; i++) {
                    matrixResult.getData()[row][col] += (matrix1.getData()[row][i] * matrix2.getData()[i][col]);
                }
            }
        }
        return matrixResult;
    }

    public void transposeInPlace() {
        Matrix4 matrixResult = new Matrix4();
        for (int col = 0; col < data[0].length; col++) {
            for (int row = 0; row < data.length; row++) {
                matrixResult.getData()[row][col] = data[col][row];
            }
        }
        data = matrixResult.getData();
    }

    public static Matrix4 transpose(final Matrix4 matrix1) {
        Matrix4 matrixResult = new Matrix4();
        for (int col = 0; col < matrix1.getData()[0].length; col++) {
            for (int row = 0; row < matrix1.getData().length; row++) {
                matrixResult.getData()[col][row] = matrix1.getData()[row][col];
            }
        }
        return matrixResult;
    }

    public void setZero() {
        for (int row = 0; row < data[0].length; row++) {
            for (int col = 0; col < data.length; col++) {
                data[row][col] = 0;
            }
        }
    }

    public static Matrix4 zeroMatrix() {
        Matrix4 matrix = new Matrix4();
        for (int row = 0; row < matrix.getData()[0].length; row++) {
            for (int col = 0; col < matrix.getData().length; col++) {
                matrix.getData()[row][col] = 0;
            }
        }
        return matrix;
    }

    public void setIdentity() {
        for (int row = 0; row < data[0].length; row++) {
            for (int col = 0; col < data.length; col++) {
                if(row == col) {
                    data[row][col] = 1;
                    continue;
                }
                data[row][col] = 0;
            }
        }
    }

    public static Matrix4 identityMatrix() {
        Matrix4 matrix = new Matrix4();
        for (int row = 0; row < matrix.getData()[0].length; row++) {
            for (int col = 0; col < matrix.getData().length; col++) {
                if(row == col) {
                    matrix.getData()[row][col] = 1;
                    continue;
                }
                matrix.getData()[row][col] = 0;
            }
        }
        return matrix;
    }

    public static Matrix4 inverse(float[][] matrix) {
        if (matrix.length != matrix[0].length) return null;
        int n = matrix.length;
        float[][] augmented = new float[n][n * 2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) augmented[i][j] = matrix[i][j];
            augmented[i][i + n] = 1;
        }
        solve(augmented);
        float[][] inv = new float[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) inv[i][j] = augmented[i][j + n];
        return new Matrix4(inv);
    }

    // Solves a system of linear equations as an augmented matrix
    // with the rightmost column containing the constants. The answers
    // will be stored on the rightmost column after the algorithm is done.
    // NOTE: make sure your matrix is consistent and does not have multiple
    // solutions before you solve the system if you want a unique valid answer.
    // Time Complexity: O(r²c)
    static void solve(float[][] augmentedMatrix) {
        int nRows = augmentedMatrix.length, nCols = augmentedMatrix[0].length, lead = 0;
        for (int r = 0; r < nRows; r++) {
            if (lead >= nCols) break;
            int i = r;
            while (Math.abs(augmentedMatrix[i][lead]) < EPS) {
                if (++i == nRows) {
                    i = r;
                    if (++lead == nCols) return;
                }
            }
            float[] temp = augmentedMatrix[r];
            augmentedMatrix[r] = augmentedMatrix[i];
            augmentedMatrix[i] = temp;
            float lv = augmentedMatrix[r][lead];
            for (int j = 0; j < nCols; j++) augmentedMatrix[r][j] /= lv;
            for (i = 0; i < nRows; i++) {
                if (i != r) {
                    lv = augmentedMatrix[i][lead];
                    for (int j = 0; j < nCols; j++) augmentedMatrix[i][j] -= lv * augmentedMatrix[r][j];
                }
            }
            lead++;
        }
    }

//    public static float det(float[][] matrix) {
//        int n = matrix.length;
//        if (n == 1) return matrix[0][0];
//        float ans = 0;
//        float[][] B = new float[n - 1][n - 1];
//        int l = 1;
//        for (int i = 0; i < n; ++i) {
//            int x = 0, y = 0;
//            for (int j = 1; j < n; ++j) {
//                for (int k = 0; k < n; ++k) {
//                    if (i == k) {
//                        continue;
//                    }
//                    B[x][y] = matrix[j][k];
//                    ++y;
//                    if (y == n - 1) {
//                        y = 0;
//                        ++x;
//                    }
//                }
//            }
//            ans += l * matrix[0][i] * det(B);
//            l *= (-1);
//        }
//        return ans;
//    }

//    public static Matrix4 invMatrix(float[][] matrix) {
//        double temp;
//
//        int n = matrix.length;
//        float [][] E = new float [n][n];
//
//
//        for (int i = 0; i < n; i++)
//            for (int j = 0; j < n; j++)
//            {
//                E[i][j] = 0f;
//
//                if (i == j)
//                    E[i][j] = 1f;
//            }
//
//        for (int k = 0; k < n; k++)
//        {
//            temp = matrix[k][k];
//
//            for (int j = 0; j < n; j++)
//            {
//                matrix[k][j] /= temp;
//                E[k][j] /= temp;
//            }
//
//            for (int i = k + 1; i < n; i++)
//            {
//                temp = matrix[i][k];
//
//                for (int j = 0; j < n; j++)
//                {
//                    matrix[i][j] -= matrix[k][j] * temp;
//                    E[i][j] -= E[k][j] * temp;
//                }
//            }
//        }
//
//        for (int k = n - 1; k > 0; k--)
//        {
//            for (int i = k - 1; i >= 0; i--)
//            {
//                temp = matrix[i][k];
//
//                for (int j = 0; j < n; j++)
//                {
//                    matrix[i][j] -= matrix[k][j] * temp;
//                    E[i][j] -= E[k][j] * temp;
//                }
//            }
//        }
//
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < n; j++) {
//                matrix[i][j] = E[i][j];
//            }
//        }
//
//        return new Matrix4(matrix);
//    }
}
