package com.cgvsu;

import com.cgvsu.math.Vector3;
import com.cgvsu.model.ModelOnScene;
import com.cgvsu.model.ModelUtils;
import com.cgvsu.render_engine.RenderEngine;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.File;
import java.util.*;

import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.Camera;

public class GuiController {

    public Pane speedPane;
    public javafx.scene.image.ImageView menu, menu1;
    public HashMap<Button, Model> objList;
    public VBox vBox;
    private float TRANSLATION;

    @FXML
    private Slider speedSlider;
    @FXML
    private Label speedLabel;
    @FXML
    AnchorPane anchorPane, pane1, pane2;
    @FXML
    private Spinner<Double> sX, sY, sZ, rX, rY, rZ, tX, tY, tZ;

    @FXML
    private Canvas canvas;

    @FXML
    private CheckBox textureCheck, shadowCheck, meshCheck, fillCheck;

    @FXML
    private TitledPane titledPane;

    private Model mesh = null;
    private ModelOnScene modelOnScene = null;
    private LinkedList<Model> modelList;

    private final Camera camera = new Camera(
            new Vector3(0, 0, 100),
            new Vector3(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    @FXML
    private void initialize() {
        initializeSpinners();
        objList = new HashMap<>(6);
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));
        speedSlider.setVisible(false);
        speedLabel.setVisible(false);
        textureCheck.setVisible(false);
        fillCheck.setVisible(false);
        meshCheck.setVisible(false);
        shadowCheck.setVisible(false);
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        initializeAnimMenu();


        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();
            speedSlider.setMax(10F);
            speedSlider.setMin(0.5F);
            TRANSLATION = (float) speedSlider.getValue();
            speedLabel.setText("Speed: " + TRANSLATION);
            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));
            for (Model mesh : objList.values()) {
                if (mesh != null) {
                    RenderEngine.render(canvas.getGraphicsContext2D(), camera, mesh, (int) width, (int) height);
                }
                if (modelOnScene != null) {
                    RenderEngine.render(canvas.getGraphicsContext2D(), camera, mesh, (int) width, (int) height);
                }
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    public void mouseEntered() {
        textureCheck.setVisible(true);
        fillCheck.setVisible(true);
        meshCheck.setVisible(true);
        shadowCheck.setVisible(true);
        speedSlider.setVisible(true);
        speedLabel.setVisible(true);
    }

    public void mouseExited() {
        textureCheck.setVisible(false);
        fillCheck.setVisible(false);
        meshCheck.setVisible(false);
        shadowCheck.setVisible(false);
        speedSlider.setVisible(false);
        speedLabel.setVisible(false);
    }

    @FXML
    private void onOpenModel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }
        String name = file.getName();

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            String fileContent = Files.readString(fileName);
            mesh = ObjReader.read(fileContent, false);
            ModelUtils.recalculateNormals(mesh);
            mesh.triangulate();
            if (objList.size() <= 6) {
                Button objFile = new Button(name);
                objFile.setFocusTraversable(true);
                objFile.setMnemonicParsing(false);
                objFile.setPrefHeight(40.0);
                objFile.setPrefWidth(209.0);
                objFile.setFont(new Font(15));
                if(!objList.containsKey(objFile)){
                    vBox.getChildren().add(objFile);
                    objList.put(objFile, mesh);
                }
            } else {
                System.out.println("max six models");
            }
            // todo: обработка ошибок
        } catch (IOException ignored) {

        }
    }
    @FXML
    private void onDeleteModel(){
        for (Button b : objList.keySet()) {
            if(b.isFocused()){
                vBox.getChildren().remove(b);
                objList.remove(b);
            }
        }
    }
    @FXML
    public void rST() {
        final float scX = sX.getValue().floatValue();
        final float scY = sY.getValue().floatValue();
        final float scZ = sZ.getValue().floatValue();
        Vector3 sV = new Vector3(scX, scY, scZ);

        final float roX = rX.getValue().floatValue();
        final float roY = rY.getValue().floatValue();
        final float roZ = rZ.getValue().floatValue();
        Vector3 vR = new Vector3(roX, roY, roZ);

        final float trX = tX.getValue().floatValue();
        final float trY = tY.getValue().floatValue();
        final float trZ = tZ.getValue().floatValue();
        Vector3 vT = new Vector3(trX, trY, trZ);
        modelOnScene = new ModelOnScene(mesh.getVertices(), mesh.getTextureVertices(), mesh.getNormals(), mesh.getPolygons(), sV, vR, vT);
        titledPane.setExpanded(false);
        titledPane.setFocusTraversable(true);
        titledPane.setFocusTraversable(false);
    }

    private void initializeSpinners() {
        SpinnerValueFactory<Double> scaX = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, 15, 0.5);
        SpinnerValueFactory<Double> scaY = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, 15, 0.5);
        SpinnerValueFactory<Double> scaZ = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, 15, 0.5);

        SpinnerValueFactory<Double> roaX = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, 15, 0.5);
        SpinnerValueFactory<Double> roaY = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, 15, 0.5);
        SpinnerValueFactory<Double> roaZ = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, 15, 0.5);

        SpinnerValueFactory<Double> traX = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, 15, 0.5);
        SpinnerValueFactory<Double> traY = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, 15, 0.5);
        SpinnerValueFactory<Double> traZ = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, 15, 0.5);

        sX.setValueFactory(scaX);
        sY.setValueFactory(scaY);
        sZ.setValueFactory(scaZ);

        rX.setValueFactory(roaX);
        rY.setValueFactory(roaY);
        rZ.setValueFactory(roaZ);

        tX.setValueFactory(traX);
        tY.setValueFactory(traY);
        tZ.setValueFactory(traZ);
    }

    private void initializeAnimMenu() {
        pane1.setVisible(false);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), pane1);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), pane2);
        translateTransition.setByX(-600);
        translateTransition.play();

        menu1.setOnMouseClicked(event -> {
            pane1.setVisible(true);
            FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), pane1);
            fadeTransition1.setFromValue(0);
            fadeTransition1.setToValue(0.15);
            fadeTransition1.play();

            TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), pane2);
            translateTransition1.setByX(+600);
            translateTransition1.setToX(0);
            translateTransition1.play();
            menu1.getParent().getParent().setVisible(false);
            menu.getParent().getParent().setVisible(true);
        });

        pane1.setOnMouseClicked(event -> {
            FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), pane1);
            fadeTransition1.setFromValue(0.15);
            fadeTransition1.setToValue(0);
            fadeTransition1.play();

            fadeTransition1.setOnFinished(event1 -> {
                pane1.setVisible(false);
            });

            TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), pane2);
            translateTransition1.setByX(-600);
            translateTransition1.setToX(-600);
            translateTransition1.play();
            menu1.getParent().getParent().setVisible(true);
            menu.getParent().getParent().setVisible(false);
        });
        menu.setOnMouseClicked(pane1.getOnMouseClicked());
    }


    @FXML
    public void handleCameraForward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3(0, 0, -TRANSLATION));
    }

    @FXML
    public void handleCameraBackward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3(0, 0, TRANSLATION));
    }

    @FXML
    public void handleCameraLeft(ActionEvent actionEvent) {
        camera.movePosition(new Vector3(TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraRight(ActionEvent actionEvent) {
        camera.movePosition(new Vector3(-TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraUp(ActionEvent actionEvent) {
        camera.movePosition(new Vector3(0, TRANSLATION, 0));
    }

    @FXML
    public void handleCameraDown(ActionEvent actionEvent) {
        camera.movePosition(new Vector3(0, -TRANSLATION, 0));
    }

}