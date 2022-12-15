package com.cgvsu.math;

public class Matrix4 {
    private float[][] matrix;

    public Matrix4(float[][] matrix) {
        this.matrix = matrix;
    }

    public Matrix4() {
        this.matrix = new float[4][4];
    }

    public float[][] getMatrix() {
        return matrix;
    }

    public void sum(Matrix4 matrix1) {
        for (int col = 0; col < matrix1.getMatrix()[0].length; col++) {
            for (int row = 0; row < matrix1.getMatrix().length; row++) {
                matrix[row][col] += matrix1.getMatrix()[row][col];
            }
        }
    }

    public void sum(Matrix4 matrix1, Matrix4 matrix2) {
        for (int col = 0; col < matrix1.getMatrix()[0].length; col++) {
            for (int row = 0; row < matrix1.getMatrix().length; row++) {
                matrix[row][col] = matrix1.getMatrix()[row][col] + matrix2.getMatrix()[row][col];
            }
        }
    }

    public void sub(Matrix4 matrix1) {
        for (int col = 0; col < matrix1.getMatrix()[0].length; col++) {
            for (int row = 0; row < matrix1.getMatrix().length; row++) {
                matrix[row][col] -= matrix1.getMatrix()[row][col];
            }
        }
    }

    public void sub(Matrix4 matrix1, Matrix4 matrix2) {
        for (int col = 0; col < matrix1.getMatrix()[0].length; col++) {
            for (int row = 0; row < matrix1.getMatrix().length; row++) {
                matrix[row][col] = matrix1.getMatrix()[row][col] - matrix2.getMatrix()[row][col];
            }
        }
    }

    public float[][] increaseOnVector(Vector4 vector) {
        float[][] matrixResult = new float[4][1];
        float[][] vectorMatrix = new float[4][1];
        vectorMatrix[0][0] = vector.getX();
        vectorMatrix[1][0] = vector.getY();
        vectorMatrix[2][0] = vector.getZ();
        vectorMatrix[3][0] = vector.getM();

        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                matrixResult[row][0] += matrix[row][col] * vectorMatrix[col][0];
            }
        }
        return matrixResult;
    }

    public float[][] increaseOnVector(Matrix4 matrix, Vector4 vector) {
        float[][] matrixResult = new float[4][1];
        float[][] vectorMatrix = new float[4][1];
        vectorMatrix[0][0] = vector.getX();
        vectorMatrix[1][0] = vector.getY();
        vectorMatrix[2][0] = vector.getZ();
        vectorMatrix[3][0] = vector.getM();

        for (int row = 0; row < matrix.getMatrix().length; row++) {
            for (int col = 0; col < matrix.getMatrix()[0].length; col++) {
                matrixResult[row][0] += matrix.getMatrix()[row][col] * vectorMatrix[col][0];
            }
        }
        return matrixResult;
    }

    public void increaseMatrix(Matrix4 matrix1) {
        float[][] matrixResult = new float[4][4];

        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix1.getMatrix()[0].length; col++) {
                for (int i = 0; i < matrix1.getMatrix().length; i++) {
                    matrixResult[row][col] += (matrix[row][i] * matrix1.getMatrix()[i][col]);
                }
            }
        }
        matrix = matrixResult;
    }

    public void increaseMatrix(Matrix4 matrix1, Matrix4 matrix2) {
        for (int row = 0; row < matrix1.getMatrix().length; row++) {
            for (int col = 0; col < matrix2.getMatrix()[0].length; col++) {
                for (int i = 0; i < matrix2.getMatrix().length; i++) {
                    matrix[row][col] += (matrix1.getMatrix()[row][i] * matrix2.getMatrix()[i][col]);
                }
            }
        }
    }

    public void transMatrix() {
        float[][] matrixResult = new float[4][4];
        for (int col = 0; col < matrix[0].length; col++) {
            for (int row = 0; row < matrix.length; row++) {
                matrixResult[col][row] = matrix[row][col];
            }
        }
        matrix = matrixResult;
    }

    public void transMatrix(Matrix4 matrix1) {
        for (int col = 0; col < matrix1.getMatrix()[0].length; col++) {
            for (int row = 0; row < matrix1.getMatrix().length; row++) {
                matrix[col][row] = matrix1.getMatrix()[row][col];
            }
        }
    }

    public void  nullMatrix() {
        for (int row = 0; row < matrix[0].length; row++) {
            for (int col = 0; col < matrix.length; col++) {
                matrix[row][col] = 0;
            }
        }
    }

    public void oneMatrix() {
        for (int row = 0; row < matrix[0].length; row++) {
            for (int col = 0; col < matrix.length; col++) {
                if(row == col) {
                    matrix[row][col] = 1;
                    continue;
                }
                matrix[row][col] = 0;
            }
        }
    }
}
