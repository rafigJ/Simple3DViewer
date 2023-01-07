package com.cgvsu;

import com.cgvsu.math.Vector3;
import com.cgvsu.model.Model;
import com.cgvsu.model.ModelOnScene;
import com.cgvsu.model.ModelUtils;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.Camera;
import com.cgvsu.render_engine.RenderEngine;
import javafx.scene.canvas.Canvas;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scene {
    private List<Integer> activeIndex;
    private final ArrayList<ModelOnScene> modelList;
    private final ArrayList<Camera> cameraList;
    private Vector3 vS = new Vector3(1, 1, 1);
    private Vector3 vR = new Vector3(0, 0, 0);
    private Vector3 vT = new Vector3(0, 0, 0);

    private Camera camera;

    public Scene() {
        camera =  new Camera(new Vector3(0, 0, 15),
                             new Vector3(0, 0, 0),
                1.0F, 1, 0.01F, 100);
        cameraList = new ArrayList<>(6);
        modelList = new ArrayList<>(6);
        activeIndex = new ArrayList<>(6);
    }

    public void update(Canvas canvas, boolean[] params){
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
        camera.setAspectRatio((float) (width / height));
        Collections.sort(activeIndex);
        if(activeIndex.isEmpty()){
            for (ModelOnScene model : modelList) {
                RenderEngine.render(canvas.getGraphicsContext2D(), camera, model, (int) width, (int) height, params);
            }
        }
        else {
            for (int i : activeIndex) {
                RenderEngine.render(canvas.getGraphicsContext2D(), camera, modelList.get(i), (int) width, (int) height, params);
            }
        }
    }

    public List<Integer> getActiveIndex() {
        return activeIndex;
    }

    public void addActiveIndex(int index){
        if(activeIndex.size() < 7) activeIndex.add(index);
    }
    public void clearActiveIndex(){
        activeIndex.clear();
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(int index) {
        this.camera = cameraList.get(index);
    }

    public List<ModelOnScene> getModelList() {
        return modelList;
    }

    public List<Camera> getCameraList() {
        return cameraList;
    }

    public void setVectors(Vector3 vS, Vector3 vR, Vector3 vT) {
        this.vS = vS;
        this.vR = vR;
        this.vT = vT;
    }

    public void setVectorsOnModels(int index) {
        modelList.get(index).setVectors(vS, vR, vT);
    }

    public String addModel(Canvas canvas) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (file == null || modelList.size() > 6) {
            return null;
        }
        String name = file.getName();
        Path fileName = Path.of(file.getAbsolutePath());
        try {
            String fileContent = Files.readString(fileName);
            Model mesh = ObjReader.read(fileContent, false);
            ModelUtils.recalculateNormals(mesh);
            mesh.triangulate();
            ModelOnScene modelOnScene = new ModelOnScene(mesh, vS, vR, vT);

            modelList.add(modelOnScene);
            return name;
            // todo: обработка ошибок
        } catch (IOException ignored) {

        }
        return null;
    }

}
