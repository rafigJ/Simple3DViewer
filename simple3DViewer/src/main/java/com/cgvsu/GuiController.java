package com.cgvsu;

import com.cgvsu.math.Vector3;
import com.cgvsu.render_engine.RenderEngine;
import com.cgvsu.tools.TextureSettings;
import javafx.animation.*;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

import com.cgvsu.render_engine.Camera;

import static javax.imageio.ImageIO.read;

public class GuiController {
    public TextField cameraName;
    private float TRANSLATION = 0.5f;
    public javafx.scene.image.ImageView expandedMenu, compressedMenu;
    public VBox vBox, vBoxCam;
    public TextField positionText, directionText;
    private List<Button> cameraButtonList, modelButtonList;
    private List<Button> multiList;
    private Button activeB;
    @FXML
    private Slider speedSlider;
    @FXML
    private Label speedLabel;
    @FXML
    AnchorPane anchorPane, pane, paneList;
    @FXML
    private Spinner<Double> sX, sY, sZ, rX, rY, rZ, tX, tY, tZ;
    @FXML
    private Canvas canvas;
    @FXML
    private CheckBox textureCheck, shadowCheck, meshCheck, fillCheck;


    private boolean pinMenu;
    private Scene scene;
    private final String standardStyle = "-fx-background-color: white;";
    private final String enterStyle = "-fx-background-color: white; -fx-border-color: red;";
    private final String activeStyle = "-fx-background-color: white; -fx-border-width: 3px; -fx-border-style: solid; -fx-border-color: #32a1ce; -fx-border-height: 3px;";
    private String paneStyle;
    private BufferedImage img;

    @FXML
    private void initialize() {
        paneStyle = paneList.getStyle();
        cameraButtonList = new ArrayList<>(6);
        modelButtonList = new ArrayList<>(6);
        multiList = new LinkedList<>();
        initializeSpinners();
        initializeAnimMenu();
        initializeTextFields();
        tooltip();
        scene = new Scene();
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        Button n = newCameraButton("Standard");
        putCamera(n, scene.getCamera());
        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            TextureSettings settings = new TextureSettings(textureCheck.isSelected(), shadowCheck.isSelected(), meshCheck.isSelected(), fillCheck.isSelected());
            speedSlider.setMax(10f);
            speedSlider.setMin(0.5f);
            speedSlider.setValue(3f);
            TRANSLATION = (float) speedSlider.getValue();
            speedLabel.setText("Speed: " + TRANSLATION);
            scene.update(canvas, settings, img);
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
        Tooltip t6 = new Tooltip("Закрепить / Открепить меню");
        t1.setFont(f);
        t2.setFont(f);
        t3.setFont(f);
        t4.setFont(f);
        t5.setFont(f);
        t6.setFont(f);
        Tooltip.install(compressedMenu, t1);
        Tooltip.install(expandedMenu, t2);
        Parent n = (Parent) compressedMenu.getParent().getParent().getChildrenUnmodifiable().get(0);
        Tooltip.install(n.getChildrenUnmodifiable().get(0), t3);
        n = (Parent) expandedMenu.getParent().getParent().getChildrenUnmodifiable().get(0);
        Tooltip.install(n.getChildrenUnmodifiable().get(0), t3);
        Tooltip.install(n.getChildrenUnmodifiable().get(2), t4);
        Tooltip.install(n.getChildrenUnmodifiable().get(3), t6);
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
        String name = scene.addModel(canvas);
        if (name == null) return;
        Button modelButton = newObjButton(name);
        // исправить или не исправлять добавление одинаковых объектов
        vBox.getChildren().add(modelButton);
        modelButtonList.add(modelButton);
    }

    private void putCamera(Button b, Camera c) {
        if (cameraButtonList.size() > 7) return;
        scene.getCameraList().add(c);
        cameraButtonList.add(b);
        vBoxCam.getChildren().add(b);
    }

    private void removeModel(int index) {
        scene.getModelList().remove(index);
        modelButtonList.remove(index);
        scene.getActiveIndex().remove((Integer) index);
        vBox.getChildren().remove(index);
    }

    private void removeCamera(int index) {
        scene.getCameraList().remove(index);
        cameraButtonList.remove(index);
        vBoxCam.getChildren().remove(index);
    }

    @FXML
    private void onDelete() {
        if (!multiList.isEmpty()) {
            List<Integer> indexes = new ArrayList<>(6);
            for (Button b : multiList) {
                indexes.add(modelButtonList.indexOf(b));
            }
            Collections.sort(indexes);
            for (int i = indexes.size() - 1; i >= 0; i--) {
                removeModel(indexes.get(i));
            }
            multiList.clear();
        } else {
            int c = -1;
            int m = -1;
            for (Button b : cameraButtonList) {
                if (b.isFocused()) c = cameraButtonList.indexOf(b);
            }
            for (Button b : modelButtonList) {
                if (b.isFocused()) m = modelButtonList.indexOf(b);
            }
            if (c != -1) removeCamera(c);
            if (m != -1) removeModel(m);
        }
        activeB = null;
    }

    private Button newObjButton(String name) {
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
            vBoxCam.getChildren().forEach(n -> n.setStyle(standardStyle));
            if (!e.isControlDown()) vBox.getChildren().forEach(n -> n.setStyle(standardStyle));
            objB.setStyle(activeStyle);
            if (e.isControlDown()) {
                if (!objB.equals(activeB) && activeB != null && !multiList.contains(activeB)) multiList.add(activeB);
                if (!multiList.contains(objB)) multiList.add(objB);
            } else {
                if (!multiList.isEmpty()) multiList.clear();
                scene.clearActiveIndex();
                scene.addActiveIndex(modelButtonList.indexOf(objB));
            }
            activeB = objB;
        });
        return objB;
    }

    private void rotateScaleTranslation() {
        float scX = sX.getValue().floatValue();
        float scY = sY.getValue().floatValue();
        float scZ = sZ.getValue().floatValue();
        Vector3 vS = new Vector3(scX, scY, scZ);

        float roX = rX.getValue().floatValue();
        float roY = rY.getValue().floatValue();
        float roZ = rZ.getValue().floatValue();
        Vector3 vR = new Vector3(roX, roY, roZ);

        float trX = tX.getValue().floatValue();
        float trY = tY.getValue().floatValue();
        float trZ = tZ.getValue().floatValue();
        Vector3 vT = new Vector3(trX, trY, trZ);
        scene.setVectors(vS, vR, vT);
        if (modelButtonList != null && modelButtonList.size() == 1) scene.setVectorsOnModels(0);
        if (modelButtonList != null && activeB != null && !vBoxCam.getChildren().contains(activeB)) {
            scene.setVectorsOnModels(modelButtonList.indexOf(activeB));
        }
        if (multiList != null && !multiList.isEmpty()) {
            multiList.forEach(n -> scene.setVectorsOnModels(modelButtonList.indexOf(n)));
        }
        canvas.requestFocus();
    }

    private void initializeSpinners() {
        SpinnerValueFactory<Double> scaX = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, 1, 0.25);
        SpinnerValueFactory<Double> scaY = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, 1, 0.25);
        SpinnerValueFactory<Double> scaZ = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, 1, 0.25);

        SpinnerValueFactory<Double> roaX = new SpinnerValueFactory.DoubleSpinnerValueFactory(-360, 360, 0, 0.25);
        SpinnerValueFactory<Double> roaY = new SpinnerValueFactory.DoubleSpinnerValueFactory(-360, 360, 0, 0.25);
        SpinnerValueFactory<Double> roaZ = new SpinnerValueFactory.DoubleSpinnerValueFactory(-360, 360, 0, 0.25);

        SpinnerValueFactory<Double> traX = new SpinnerValueFactory.DoubleSpinnerValueFactory(-100, 100, 0, 0.25);
        SpinnerValueFactory<Double> traY = new SpinnerValueFactory.DoubleSpinnerValueFactory(-100, 100, 0, 0.25);
        SpinnerValueFactory<Double> traZ = new SpinnerValueFactory.DoubleSpinnerValueFactory(-100, 100, 0, 0.25);

        scaX.valueProperty().addListener(e -> rotateScaleTranslation());
        scaY.valueProperty().addListener(e -> rotateScaleTranslation());
        scaZ.valueProperty().addListener(e -> rotateScaleTranslation());

        roaX.valueProperty().addListener(e -> rotateScaleTranslation());
        roaY.valueProperty().addListener(e -> rotateScaleTranslation());
        roaZ.valueProperty().addListener(e -> rotateScaleTranslation());

        traX.valueProperty().addListener(e -> rotateScaleTranslation());
        traY.valueProperty().addListener(e -> rotateScaleTranslation());
        traZ.valueProperty().addListener(e -> rotateScaleTranslation());

        sX.setValueFactory(scaX);
        sY.setValueFactory(scaY);
        sZ.setValueFactory(scaZ);

        rX.setValueFactory(roaX);
        rY.setValueFactory(roaY);
        rZ.setValueFactory(roaZ);

        tX.setValueFactory(traX);
        tY.setValueFactory(traY);
        tZ.setValueFactory(traZ);

        meshCheck.getParent().getChildrenUnmodifiable().forEach(n -> n.setVisible(false));
        meshCheck.setSelected(true);
    }


    private void initializeAnimMenu() {
        pane.setVisible(false);
        pinMenu = false;
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), pane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), paneList);
        translateTransition.setByX(-600);
        translateTransition.play();

        compressedMenu.setOnMouseClicked(event -> {
            pane.setVisible(true);
            FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), pane);
            fadeTransition1.setFromValue(0);
            fadeTransition1.setToValue(0.35);
            fadeTransition1.play();

            TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), paneList);
            translateTransition1.setByX(+600);
            translateTransition1.setToX(0);
            translateTransition1.play();
            compressedMenu.getParent().getParent().setVisible(false);
            expandedMenu.getParent().getParent().setVisible(true);
        });

        pane.setOnMouseClicked(event -> {
            FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), pane);
            fadeTransition1.setFromValue(0.35);
            fadeTransition1.setToValue(0);
            fadeTransition1.play();

            fadeTransition1.setOnFinished(event1 -> pane.setVisible(false));

            TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), paneList);
            translateTransition1.setByX(-600);
            translateTransition1.setToX(-600);
            translateTransition1.play();
            compressedMenu.getParent().getParent().setVisible(true);
            expandedMenu.getParent().getParent().setVisible(false);
            canvas.requestFocus();
        });
        expandedMenu.setOnMouseClicked(pane.getOnMouseClicked());
    }

    // Загрузка текстуры для каждой модели по отдельности. Пока что меняю статическое поле в render
    public void onOpenTexture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.jpg)", "*.jpg", "Model (*.png)", "*.png"));
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


    public void addCamera() {
        StringTokenizer tokenizerS1 = new StringTokenizer(positionText.getText(), " ,", false);
        StringTokenizer tokenizerS2 = new StringTokenizer(directionText.getText(), " ,", false);
        var s1 = new ArrayList<String>();
        var s2 = new ArrayList<String>();
        while (tokenizerS1.hasMoreElements()) {
            s1.add(tokenizerS1.nextToken());
        }
        while (tokenizerS2.hasMoreElements()) {
            s2.add(tokenizerS2.nextToken());
        }
        if (s1.size() == 3 && s2.size() == 3) {
            Vector3 v1, v2;
            v1 = new Vector3(Float.parseFloat(s1.get(0)), Float.parseFloat(s1.get(1)), Float.parseFloat(s1.get(2)));
            v2 = new Vector3(Float.parseFloat(s2.get(0)), Float.parseFloat(s2.get(1)), Float.parseFloat(s2.get(2)));
            Camera c = new Camera(v1, v2, 1.0F, 1, 0.01F, 100);
            Button n = cameraName.getText().trim().isEmpty() ? newCameraButton(Integer.toString(cameraButtonList.size())) :
                    newCameraButton(cameraName.getText());
            putCamera(n, c);
        }
    }

    private Button newCameraButton(String name) {
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
            vBox.getChildren().forEach(n -> n.setStyle(standardStyle));
            if (!e.isControlDown()) vBoxCam.getChildren().forEach(n -> n.setStyle(standardStyle));
            camB.setStyle(activeStyle);
            activeB = camB;
            if (!e.isControlDown()) {
                scene.setCamera(cameraButtonList.indexOf(activeB));
            }
        });
        return camB;
    }

    @FXML
    public void handleCameraForward() {
        scene.getCamera().movePosition(new Vector3(0, 0, -TRANSLATION));
    }

    @FXML
    public void handleCameraBackward() {
        scene.getCamera().movePosition(new Vector3(0, 0, TRANSLATION));
    }

    @FXML
    public void handleCameraLeft() {
        scene.getCamera().movePosition(new Vector3(TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraRight() {
        scene.getCamera().movePosition(new Vector3(-TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraUp() {
        scene.getCamera().movePosition(new Vector3(0, TRANSLATION, 0));
    }

    @FXML
    public void handleCameraDown() {
        scene.getCamera().movePosition(new Vector3(0, -TRANSLATION, 0));
    }

    public void canvasClick() {
        //        mousePosX = (float) mouseEvent.getSceneX();
//        mousePosY = (float) mouseEvent.getSceneY();
//        System.out.print(camera.getTarget().getX() + " " + camera.getTarget().getY() + " " + camera.getTarget;
//
//        canvas.setOnMouseDragged(event -> {
//            camera.movePosition(new Vector3(TRANSLATION, TRANSLATION, 0));
//        });

//            anchorX = mouseEvent.getSceneX();
//            anchorY = mouseEvent.getSceneY();
//            anchorAngleX = angleX.get();
//            anchorAngleY = angleY.get();
//
//        canvas.setOnMouseDragged(event -> {
//            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
//            angleY.set(anchorAngleY + anchorX - event.getSceneX());
//            camera.setTarget(new Vector3((float) (100 * Math.sin(anchorAngleX)), (float) (100*Math.cos(anchorAngleX)), 0));
//        });

        canvas.setOnMouseDragged(event -> {

        });

        if (activeB != null) activeB.setStyle(standardStyle);
        activeB = null;
        canvas.requestFocus();
    }

    public void pin() {
        if (pinMenu) {
            pane.setVisible(true);
            pane.setDisable(false);
            paneList.setStyle(paneStyle);
            FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), pane);
            fadeTransition1.setFromValue(0);
            fadeTransition1.setToValue(0.35);
            fadeTransition1.play();
            pinMenu = false;
            compressedMenu.setDisable(false);
            expandedMenu.setDisable(false);
        } else {
            pane.setDisable(true);
            paneList.setStyle(paneStyle + "-fx-border-color: #545454 #545454 #545454 #FFFFFF;");
            FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), pane);
            fadeTransition1.setFromValue(0.35);
            fadeTransition1.setToValue(0);
            fadeTransition1.play();
            fadeTransition1.setOnFinished(event1 -> pane.setVisible(false));
            compressedMenu.setDisable(true);
            expandedMenu.setDisable(true);
            pinMenu = true;
        }
    }

    public void onMultiView() {
        for (Button b : multiList) {
            scene.addActiveIndex(modelButtonList.indexOf(b));
        }
    }
}