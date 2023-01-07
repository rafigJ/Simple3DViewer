package com.cgvsu;

import com.cgvsu.math.Vector3;
import com.cgvsu.model.ModelOnScene;
import com.cgvsu.render_engine.Camera;
import com.cgvsu.tools.TextureSettings;
import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

import static javax.imageio.ImageIO.read;

public class GuiController extends Pane {
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

    //камера
    private double mousePosX;
    private double mousePosY;
    private double oldMousePosX;
    private double oldMousePosY;
    private double centerX = getWidth() / 2;
    private double centerY = getHeight() / 2;
    private double initialAngle;
    private double initialAnglePane;
    private double angle;

    @FXML
    private void initialize() {
        paneStyle = paneList.getStyle();
        cameraButtonList = new ArrayList<>(6);
        modelButtonList = new ArrayList<>(6);
        multiList = new LinkedList<>();
        updateSpinners(-1);
        initializeAnimMenu();
        initializeTextFields();
        tooltip();
        speedSlider.setMax(10f);
        speedSlider.setMin(0.5f);
        speedSlider.setValue(3f);
        meshCheck.getParent().getChildrenUnmodifiable().forEach(n -> n.setVisible(false));
        meshCheck.setSelected(true);
        scene = new Scene();
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        Button n = newCameraButton("Standard");
        putCamera(n, scene.getCamera());
        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            TextureSettings settings = new TextureSettings(textureCheck.isSelected(), shadowCheck.isSelected(), meshCheck.isSelected(), fillCheck.isSelected());
            TRANSLATION = (float) speedSlider.getValue();
            speedLabel.setText("Speed: " + TRANSLATION);
            scene.update(canvas, settings);
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
        Tooltip.install(n.getChildrenUnmodifiable().get(1), t5);
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
    private void saveObj() {
        if (activeB == null) {
            System.out.println("ERROR SELECT MODEL");
            return;
        }
        scene.saveModel(canvas, modelButtonList.indexOf(activeB));
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
        scene.getTextureList().remove(index);
        modelButtonList.remove(index);
        scene.getActiveIndex().remove((Integer) index);
        vBox.getChildren().remove(index);
    }

    private void removeCamera(int index) {
        if (index == 0) return;
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
        String changeButtonStyle = standardStyle + "-fx-border-color: yellow; -fx-border-height: 3px; -fx-border-width: 3px;";
        Button objB = new Button(name);
        objB.setFocusTraversable(true);
        objB.setMnemonicParsing(false);
        objB.setPrefHeight(40.0);
        objB.setPrefWidth(209.0);
        objB.setFont(new Font(15));
        objB.setStyle(standardStyle);
        // поработать
        objB.setOnMouseEntered(e -> {
            if (!objB.getStyle().equals(activeStyle) && !objB.getStyle().equals(changeButtonStyle))
                objB.setStyle(enterStyle);
        });
        objB.setOnMouseExited(e -> {
            if (!objB.getStyle().equals(activeStyle) && !objB.getStyle().equals(changeButtonStyle))
                objB.setStyle(standardStyle);
        });
        objB.setOnMouseClicked(e -> {
            vBoxCam.getChildren().forEach(n -> n.setStyle(standardStyle));  // очищаем стили кнопок из раздела Камера
            if (!e.isControlDown()) vBox.getChildren().forEach(n -> n.setStyle(standardStyle));
            objB.setStyle(activeStyle);
            if (e.getButton() == MouseButton.SECONDARY) {
                activeB = objB;
                activeB.setStyle(changeButtonStyle);
                updateSpinners(modelButtonList.indexOf(objB));
                return;
            }
            if (e.isControlDown()) {
                if (!objB.equals(activeB) && activeB != null && !multiList.contains(activeB)) multiList.add(activeB);
                if (!multiList.contains(objB)) multiList.add(objB);
                activeB = null;
            } else {
                if (!multiList.isEmpty()) multiList.clear();
                scene.clearActiveIndex();
                int i = modelButtonList.indexOf(objB);
                scene.addActiveIndex(i);
                updateSpinners(i);
                activeB = objB;
            }
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

        if (modelButtonList.size() == 1)
            scene.setVectorsOnModel(vS, vR, vT, 0); // случай когда в списке лишь одна модель
        // случай когда выбрана только одна модель
        if (activeB != null && !vBoxCam.getChildren().contains(activeB))
            scene.setVectorsOnModel(vS, vR, vT, modelButtonList.indexOf(activeB));
        if (!multiList.isEmpty() && activeB == null) updateSpinners(-1);
        canvas.requestFocus();
    }

    private void updateSpinners(int index) {
        SpinnerValueFactory<Double>[] s = getSpinnerValue(index);

        sX.setValueFactory(s[0]);
        sY.setValueFactory(s[1]);
        sZ.setValueFactory(s[2]);

        rX.setValueFactory(s[3]);
        rY.setValueFactory(s[4]);
        rZ.setValueFactory(s[5]);

        tX.setValueFactory(s[6]);
        tY.setValueFactory(s[7]);
        tZ.setValueFactory(s[8]);
        s[0].valueProperty().addListener(e -> rotateScaleTranslation());
        s[1].valueProperty().addListener(e -> rotateScaleTranslation());
        s[2].valueProperty().addListener(e -> rotateScaleTranslation());

        s[3].valueProperty().addListener(e -> rotateScaleTranslation());
        s[4].valueProperty().addListener(e -> rotateScaleTranslation());
        s[5].valueProperty().addListener(e -> rotateScaleTranslation());

        s[6].valueProperty().addListener(e -> rotateScaleTranslation());
        s[7].valueProperty().addListener(e -> rotateScaleTranslation());
        s[8].valueProperty().addListener(e -> rotateScaleTranslation());
    }

    private SpinnerValueFactory<Double>[] getSpinnerValue(int index) {
        SpinnerValueFactory<Double>[] res = new SpinnerValueFactory.DoubleSpinnerValueFactory[9];
        double[] arr = {0, 0, 0, 0, 0, 0, 0, 0, 0};
        if (index != -1) {
            ModelOnScene model = scene.getModelList().get(index);
            arr[0] = model.getVS().getX();
            arr[1] = model.getVS().getY();
            arr[2] = model.getVS().getZ();

            arr[3] = model.getVR().getX();
            arr[4] = model.getVR().getY();
            arr[5] = model.getVR().getZ();

            arr[6] = model.getVT().getX();
            arr[7] = model.getVT().getY();
            arr[8] = model.getVT().getZ();
        }
        res[0] = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, arr[0], 0.25);
        res[1] = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, arr[1], 0.25);
        res[2] = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, arr[2], 0.25);

        res[3] = new SpinnerValueFactory.DoubleSpinnerValueFactory(-360, 360, arr[3], 0.25);
        res[4] = new SpinnerValueFactory.DoubleSpinnerValueFactory(-360, 360, arr[4], 0.25);
        res[5] = new SpinnerValueFactory.DoubleSpinnerValueFactory(-360, 360, arr[5], 0.25);

        res[6] = new SpinnerValueFactory.DoubleSpinnerValueFactory(-1000, 1000, arr[6], 0.25);
        res[7] = new SpinnerValueFactory.DoubleSpinnerValueFactory(-1000, 1000, arr[7], 0.25);
        res[8] = new SpinnerValueFactory.DoubleSpinnerValueFactory(-1000, 1000, arr[8], 0.25);

        return res;
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
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.jpg)", "*.jpg"));
        fileChooser.setTitle("Load Texture");

        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (file == null || activeB == null || cameraButtonList.contains(activeB)) {
            return;
        }
        try {
            BufferedImage img = read(file);
            scene.getTextureList().add(modelButtonList.indexOf(activeB), img);
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
        scene.getCamera().moveTarget(new Vector3(TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraRight() {
        scene.getCamera().movePosition(new Vector3(-TRANSLATION, 0, 0));
        scene.getCamera().moveTarget(new Vector3(-TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraUp() {
        scene.getCamera().movePosition(new Vector3(0, TRANSLATION, 0));
        scene.getCamera().moveTarget(new Vector3(0, TRANSLATION, 0));
    }

    @FXML
    public void handleCameraDown() {
        scene.getCamera().movePosition(new Vector3(0, -TRANSLATION, 0));
        scene.getCamera().moveTarget(new Vector3(0, -TRANSLATION, 0));
    }

    public void canvasClick() {

        if (activeB != null) activeB.setStyle(standardStyle);
        canvas.requestFocus();

        //        mousePosX = (float) mouseEvent.getSceneX();
//        mousePosY = (float) mouseEvent.getSceneY();
//        System.out.print(camera.getTarget().getX() + " " + camera.getTarget().getY() + " " + camera.getTarget;

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

        canvas.setOnMousePressed(event -> {
            oldMousePosX = event.getSceneX();
            oldMousePosY = event.getSceneY();
        });
        canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double x = event.getSceneX();
                double y = event.getSceneY();
                double angleX;
                double angleY;
                double dx = x - oldMousePosX;
                double dy = y - oldMousePosY;

                if (dx > 0) {
                    scene.getCamera().movePosition(new Vector3(TRANSLATION, 0, 0));
                    angleY = TRANSLATION;
                } else if (dx < 0) {
                    scene.getCamera().movePosition(new Vector3(-TRANSLATION, 0, 0));
                    angleY = -TRANSLATION;
                } else {
                    angleY = 0;
                }

                if (dy > 0) {
                    scene.getCamera().movePosition(new Vector3(0, TRANSLATION, 0));
                    angleX = -TRANSLATION;
                } else if (dy < 0) {
                    scene.getCamera().movePosition(new Vector3(0, -TRANSLATION, 0));
                    angleX = TRANSLATION;
                } else {
                    angleX = 0;
                }
            }

            ;
        });

        canvas.setOnScrollStarted(event -> {
            oldMousePosX = event.getSceneX();
            oldMousePosY = event.getSceneY();
        });

        canvas.setOnScroll(event -> {
            double x = event.getDeltaX();
            double y = event.getDeltaY();


            double dx = x - oldMousePosX;
            double dy = y - oldMousePosY;

            if (y < 0) {
                scene.getCamera().setPosition(Vector3.sum(scene.getCamera().getPosition(), new Vector3(0, 0, TRANSLATION)));
            } else {
                scene.getCamera().setPosition(Vector3.sum(scene.getCamera().getPosition(), new Vector3(0, 0, -TRANSLATION)));
            }
        });
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