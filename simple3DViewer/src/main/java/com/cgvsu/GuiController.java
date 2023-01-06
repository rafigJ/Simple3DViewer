package com.cgvsu;

import com.cgvsu.math.Vector3;
import com.cgvsu.model.ModelOnScene;
import com.cgvsu.model.ModelUtils;
import com.cgvsu.render_engine.RenderEngine;
import javafx.animation.*;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.Camera;

import static javax.imageio.ImageIO.read;

public class GuiController {

    public Pane speedPane;
    public javafx.scene.image.ImageView menu, menu1;
    public HashMap<Button, ModelOnScene> modelMap;
    public LinkedList<Button> multiList;
    public VBox vBox, vBoxCam;
    private HashMap<Button, Camera> cameraMap;
    public TextField positionText, directionText;
    private Button activeB;
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
    private Vector3 vS = new Vector3(1, 1, 1);
    private Vector3 vR = new Vector3(0, 0, 0);
    private Vector3 vT = new Vector3(0, 0, 0);
    private Model mesh = null;
    private ModelOnScene modelOnScene = null;
    private BufferedImage img = null;

    private Camera camera = new Camera(
            new Vector3(0, 0, 300),
            new Vector3(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    @FXML
    private void initialize() {
        initializeSpinners();
        initializeAnimMenu();
        initializeTextFields();
        tooltip();
        cameraMap = new HashMap<>(6);
        modelMap = new HashMap<>(6);
        multiList = new LinkedList<>();
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        Button n = newCameraButton("Standard");
        cameraMap.put(n, camera);
        vBoxCam.getChildren().add(n);
        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();
            boolean[] params = {textureCheck.isSelected(), shadowCheck.isSelected(), meshCheck.isSelected(), fillCheck.isSelected()};
            speedSlider.setMax(10F);
            speedSlider.setMin(0.5F);
            TRANSLATION = (float) speedSlider.getValue();
            speedLabel.setText("Speed: " + TRANSLATION);
            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));
            for (ModelOnScene mesh : modelMap.values()) {
//                if (mesh != null) {
//                    RenderEngine.render(canvas.getGraphicsContext2D(), camera, mesh, (int) width, (int) height, params);
//                }
                RenderEngine.render(canvas.getGraphicsContext2D(), camera, mesh, (int) width, (int) height, params);

            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    //Pane
    public void mouseEntered() {
        meshCheck.getParent().getChildrenUnmodifiable().forEach(n -> n.setVisible(true));
    }

    public void mouseExited() {
        meshCheck.getParent().getChildrenUnmodifiable().forEach(n -> n.setVisible(false));
    }

    private void tooltip() {
        Font f = new Font(13);
        Tooltip t1 = new Tooltip("Открыть меню");
        Tooltip t2 = new Tooltip("Закрыть меню");
        Tooltip t3 = new Tooltip("Добавить объект (.obj)");
        Tooltip t4 = new Tooltip("Удалить объект из списка");
        Tooltip t5 = new Tooltip("Скачать преобразованный obj файл"); // Пока не используется
        t1.setFont(f);
        t2.setFont(f);
        t3.setFont(f);
        t4.setFont(f);
        t5.setFont(f);
        Tooltip.install(menu1, t1);
        Tooltip.install(menu, t2);
        Parent n = (Parent) menu1.getParent().getParent().getChildrenUnmodifiable().get(0);
        Tooltip.install(n.getChildrenUnmodifiable().get(0), t3);
        n = (Parent) menu.getParent().getParent().getChildrenUnmodifiable().get(0);
        Tooltip.install(n.getChildrenUnmodifiable().get(0), t3);
        Tooltip.install(n.getChildrenUnmodifiable().get(2), t4);
    }

    // только цифры и запятые.
    private void initializeTextFields() {
        Pattern p = Pattern.compile("(\\d+\\.?\\d*)?([ ,]+)?(\\d+\\.?\\d*)?([ ,]+)?(\\d+\\.?\\d*)?");
        directionText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) directionText.setText(oldValue);
        });
        positionText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) positionText.setText(oldValue);
        });
    }

    @FXML
    private void onOpenModel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null || modelMap.size() >= 6) {
            return;
        }
        String name = file.getName();
        Path fileName = Path.of(file.getAbsolutePath());
        try {
            String fileContent = Files.readString(fileName);
            mesh = ObjReader.read(fileContent, false);
            ModelUtils.recalculateNormals(mesh);
            mesh.triangulate();
            modelOnScene = new ModelOnScene(mesh, vS, vR, vT);
            Button modelButton = newObjButton(name);

            // исправить или не исправлять добавление одинаковых объектов
            vBox.getChildren().add(modelButton);
            modelMap.put(modelButton, modelOnScene);

            // todo: обработка ошибок
        } catch (IOException ignored) {

        }
    }

    @FXML
    private void onDelete() {
        if (!multiList.isEmpty()) {
            for (Button b : multiList) {
                modelMap.remove(b);
                vBox.getChildren().remove(b);
            }
            multiList.removeAll(multiList);
        } else {
            var r = new HashSet<Button>();
            cameraMap.keySet().forEach(b -> {
                if (b.isFocused()) r.add(b);
            });
            modelMap.keySet().forEach(b -> {
                if (b.isFocused()) r.add(b);
            });
            r.forEach(b -> {
                if (vBox.getChildren().contains(b)) {
                    modelMap.remove(b);
                    vBox.getChildren().remove(b);
                } else {
                    cameraMap.remove(b);
                    vBoxCam.getChildren().remove(b);
                    camera = new Camera(
                            new Vector3(0, 0, 300),
                            new Vector3(0, 0, 0),
                            1.0F, 1, 0.01F, 100);

                }
            });
        }
    }

    private Button newObjButton(String name) {
        String standardStyle = "-fx-background-color: white;";
        String enterStyle = "-fx-background-color: white; -fx-border-color: red;";
        String activeStyle = "-fx-background-color: white; -fx-border-width: 3px; -fx-border-style: solid; -fx-border-color: #32a1ce; -fx-border-height: 3px;";
        Button objB = new Button(name);
        objB.setFocusTraversable(true);
        objB.setMnemonicParsing(false);
        objB.setPrefHeight(40.0);
        objB.setPrefWidth(209.0);
        objB.setFont(new Font(15));
        objB.setStyle(standardStyle);
        // поработать
        objB.setOnMouseEntered(e -> {
            if (!objB.getStyle().equals(activeStyle)) objB.setStyle(enterStyle);
        });
        objB.setOnMouseExited(e -> {
            if (!objB.getStyle().equals(activeStyle)) objB.setStyle(standardStyle);
        });
        objB.setOnMouseClicked(e -> {
            if (!e.isControlDown()) vBox.getChildren().forEach(n -> n.setStyle(standardStyle));
            objB.setStyle(activeStyle);
            if (e.isControlDown()) {
                if (!objB.equals(activeB)) multiList.add(activeB);
                if (!multiList.contains(objB)) multiList.add(objB);
            } else {
                if (!multiList.isEmpty()) multiList.removeAll(multiList);
            }
            activeB = objB;
        });
        return objB;
    }


    @FXML
    public void rST() {
        float scX = sX.getValue().floatValue();
        float scY = sY.getValue().floatValue();
        float scZ = sZ.getValue().floatValue();
        Vector3 sV = new Vector3(scX, scY, scZ);

        float roX = rX.getValue().floatValue();
        float roY = rY.getValue().floatValue();
        float roZ = rZ.getValue().floatValue();
        Vector3 vR = new Vector3(roX, roY, roZ);

        float trX = tX.getValue().floatValue();
        float trY = tY.getValue().floatValue();
        float trZ = tZ.getValue().floatValue();
        Vector3 vT = new Vector3(trX, trY, trZ);

        if (modelMap != null && activeB != null) {
            modelMap.get(activeB).setVectors(vS, vR, vT);
        }
        if (multiList != null && multiList.isEmpty()) {
            multiList.forEach(n -> {
                modelMap.get(n).setVectors(vS, vR, vT);
            });
        }
        titledPane.setExpanded(false);
        canvas.requestFocus();
    }

    private void initializeSpinners() {
        SpinnerValueFactory<Double> scaX = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, 1, 0.5);
        SpinnerValueFactory<Double> scaY = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, 1, 0.5);
        SpinnerValueFactory<Double> scaZ = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, 1, 0.5);

        SpinnerValueFactory<Double> roaX = new SpinnerValueFactory.DoubleSpinnerValueFactory(-360, 360, 0, 0.5);
        SpinnerValueFactory<Double> roaY = new SpinnerValueFactory.DoubleSpinnerValueFactory(-360, 360, 0, 0.5);
        SpinnerValueFactory<Double> roaZ = new SpinnerValueFactory.DoubleSpinnerValueFactory(-360, 360, 0, 0.5);

        SpinnerValueFactory<Double> traX = new SpinnerValueFactory.DoubleSpinnerValueFactory(-100, 100, 0, 0.5);
        SpinnerValueFactory<Double> traY = new SpinnerValueFactory.DoubleSpinnerValueFactory(-100, 100, 0, 0.5);
        SpinnerValueFactory<Double> traZ = new SpinnerValueFactory.DoubleSpinnerValueFactory(-100, 100, 0, 0.5);

        sX.setValueFactory(scaX);
        sY.setValueFactory(scaY);
        sZ.setValueFactory(scaZ);

        rX.setValueFactory(roaX);
        rY.setValueFactory(roaY);
        rZ.setValueFactory(roaZ);

        tX.setValueFactory(traX);
        tY.setValueFactory(traY);
        tZ.setValueFactory(traZ);
        // видимость всех-всех узлов на pane
        meshCheck.getParent().getChildrenUnmodifiable().forEach(n -> n.setVisible(false));
        meshCheck.setSelected(true);
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
            fadeTransition1.setToValue(0.35);
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
            fadeTransition1.setFromValue(0.35);
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
            canvas.requestFocus();
        });
        menu.setOnMouseClicked(pane1.getOnMouseClicked());
    }


    // Загрузка текстуры для каждой модели по отдельности. Пока что меняю статическое поле в render
    public void onOpenTexture(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.jpg)", "*.jpg","Model (*.png)", "*.png"));
        fileChooser.setTitle("Load Texture");

        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (file == null) {
            return;
        }
        try {
            img = read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addCamera(MouseEvent mouseEvent) {
        String s1[] = positionText.getText().split("[, ]");
        String s2[] = directionText.getText().split("[, ]");
        if (s1.length == 3 && s2.length == 3) {
            Vector3 v1, v2;
            v1 = new Vector3(Float.parseFloat(s1[0]), Float.parseFloat(s1[1]), Float.parseFloat(s1[2]));
            v2 = new Vector3(Float.parseFloat(s2[0]), Float.parseFloat(s2[1]), Float.parseFloat(s2[2]));
            Camera c = new Camera(v1, v2,
                    1.0F, 1, 0.01F, 100);
            Button n = newCameraButton(Integer.toString(cameraMap.size()));
            if (cameraMap.size() < 6) {
                cameraMap.put(n, c);
                vBoxCam.getChildren().add(n);
            }
        }
    }

    private Button newCameraButton(String name) {
        String standardStyle = "-fx-background-color: white;";
        String enterStyle = "-fx-background-color: white; -fx-border-color: red;";
        String activeStyle = "-fx-background-color: white; -fx-border-width: 3px; -fx-border-style: solid; -fx-border-color: #32a1ce; -fx-border-height: 3px;";
        Button camB = new Button(name);
        camB.setFocusTraversable(true);
        camB.setMnemonicParsing(false);
        camB.setPrefHeight(40.0);
        camB.setPrefWidth(209.0);
        camB.setFont(new Font(15));
        camB.setStyle(standardStyle);
        camB.setOnMouseEntered(e -> {
            if (!camB.getStyle().equals(activeStyle)) camB.setStyle(enterStyle);
        });
        camB.setOnMouseExited(e -> {
            if (!camB.getStyle().equals(activeStyle)) camB.setStyle(standardStyle);
        });
        camB.setOnMouseClicked(e -> {
            if (!e.isControlDown()) vBoxCam.getChildren().forEach(n -> n.setStyle(standardStyle));
            camB.setStyle(activeStyle);
            activeB = camB;
            if (!e.isControlDown()) camera = cameraMap.get(activeB);
        });

        return camB;
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

    public void canvasClick(MouseEvent mouseEvent) {
        titledPane.setExpanded(false);
        canvas.requestFocus();
    }
}