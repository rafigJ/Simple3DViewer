package com.cgvsu.render_engine;

import com.cgvsu.math.Matrix4;
import com.cgvsu.math.Vector3;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class GraphicConveyorTest {
    private final Vector3 vS = new Vector3(1f, 2f, 3f);
    private final Vector3 vR = new Vector3(0,90,180);
    private final Vector3 vT = new Vector3(10, 20, 0);
    private final Matrix4 matrixScale = GraphicConveyor.scale(vS);
    private final Matrix4 matrixRotate = GraphicConveyor.rotate(vR);
    private final Matrix4 matrixTranslate = GraphicConveyor.translate(vT);
    private final Matrix4 matrixTRS = GraphicConveyor.translateRotateScale(vS, vR, vT);

    @Test
    void rotateScaleTranslate() {
        float[][] mTRS = matrixTRS.getData();
        float[][] matrixRes = new float[][] {
                {0,0,-3,0},
                {0,-2,0,0},
                {-1,0,0,0},
                {0,0,0,0}
        };
        Arrays.deepEquals(mTRS, matrixRes);
    }

    @Test
    void scale() {
        float[][] mS = matrixScale.getData();
        float[][] matrixRes = new float[][] {
                {1,0,0,0},
                {0,2,0,0},
                {0,0,3,0},
                {0,0,0,1}
        };
        Arrays.deepEquals(mS, matrixRes);
    }

    @Test
    void rotate() {
        float[][] mR = matrixRotate.getData();
        float[][] matrixRes = new float[][] {
                {0, 0, -1, 0},
                {0, -1, 0, 0},
                {-1, 0, 0, 0},
                {0, 0, 0, 0}
        };
        Arrays.deepEquals(mR, matrixRes);
    }

    @Test
    void translate() {
        float[][] mT = matrixTranslate.getData();
        float[][] matrixRes = new float[][] {
                {1, 0, 0, 10},
                {0, 1, 0, 20},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
        Arrays.deepEquals(mT, matrixRes);
    }

}