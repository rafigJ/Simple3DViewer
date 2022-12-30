package com.cgvsu;

import com.cgvsu.math.Vector3;
import com.cgvsu.model.ModelOnScene;
import com.cgvsu.model.ModelUtils;
import com.cgvsu.render_engine.RenderEngine;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.File;

import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.Camera;

public class GuiController {

    public Pane speedPane;
    private float TRANSLATION;

    @FXML
    private Slider speedSlider;
    @FXML
    private Label speedLabel;
    @FXML
    AnchorPane anchorPane;

    @FXML
    private Spinner<Double> sX;
    @FXML
    private Spinner<Double> sY;
    @FXML
    private Spinner<Double> sZ;

    @FXML
    private Spinner<Double> rX;
    @FXML
    private Spinner<Double> rY;
    @FXML
    private Spinner<Double> rZ;

    @FXML
    private Spinner<Double> tX;
    @FXML
    private Spinner<Double> tY;
    @FXML
    private Spinner<Double> tZ;

    @FXML
    private Canvas canvas;

    @FXML
    private CheckBox textureCheck;
    @FXML
    private CheckBox shadowCheck;
    @FXML
    private CheckBox meshCheck;
    @FXML
    private CheckBox fillCheck;

    @FXML
    private TitledPane titledPane;
    private Vector3 sV;
    private Vector3 vR;
    private Vector3 vT;

    private Model mesh = null;
    private ModelOnScene modelOnScene = null;


    private Camera camera = new Camera(

            new Vector3(0, 0, 300),

            new Vector3(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;

    @FXML
    private void initialize() {
        initializeSpinners();
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));
        speedSlider.setVisible(false);
        speedLabel.setVisible(false);
        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();
            speedSlider.setMax(10F);
            speedSlider.setMin(0.5F);
            TRANSLATION = (float) speedSlider.getValue();
            speedLabel.setText("Speed: " + TRANSLATION);
            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));


            if (mesh != null) {
                 RenderEngine.render(canvas.getGraphicsContext2D(), camera, mesh, (int) width, (int) height);
            }
            if(modelOnScene != null) {
                RenderEngine.render(canvas.getGraphicsContext2D(), camera, mesh, (int) width, (int) height);
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    public void mouseEntered(MouseEvent mouseEvent) {
        speedSlider.setVisible(true);
        speedLabel.setVisible(true);
    }

    public void mouseExited(MouseEvent mouseEvent) {
        speedSlider.setVisible(false);
        speedLabel.setVisible(false);
    }

    @FXML
    private void onOpenModelMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            String fileContent = Files.readString(fileName);
            mesh = ObjReader.read(fileContent, false);
            ModelUtils.recalculateNormals(mesh);
            mesh.triangulate();
            // todo: обработка ошибок
        } catch (IOException exception) {

        }
    }

    @FXML
    public void rST() {
        final float scX = sX.getValue().floatValue();
        final float scY = sY.getValue().floatValue();
        final float scZ = sZ.getValue().floatValue();
        sV = new Vector3(scX, scY, scZ);

        final float roX = rX.getValue().floatValue();
        final float roY = rY.getValue().floatValue();
        final float roZ = rZ.getValue().floatValue();
        vR = new Vector3(roX, roY, roZ);

        final float trX = tX.getValue().floatValue();
        final float trY = tY.getValue().floatValue();
        final float trZ = tZ.getValue().floatValue();
        vT = new Vector3(trX, trY, trZ);

        modelOnScene = new ModelOnScene(mesh.getVertices(), mesh.getTextureVertices(), mesh.getNormals(), mesh.getPolygons(), sV, vR, vT);
        titledPane.setExpanded(false);
        titledPane.setFocusTraversable(true);
        titledPane.setFocusTraversable(false);
    }

    private void initializeSpinners(){
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