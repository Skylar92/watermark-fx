package com.skylar.watermark.fx.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.skylar.watermark.fx.utils.FileUtils;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import com.skylar.watermark.fx.utils.ImageHelper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RootController {

    private static final File image = new File(FileUtils.getFile("image/default-slide-img.jpg").getFile());

    public RootController() {}

    //Editable image
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private JFXComboBox fontList;
    @FXML
    private JFXTextField opacility;
    @FXML
    private JFXTextField sizeText;
    @FXML
    private JFXTextField text;
    @FXML
    private JFXTextField stepX;
    @FXML
    private JFXTextField stepY;
    @FXML
    private JFXSlider radius;
    //Preview
    @FXML
    private ImageView imageView;
    //Actions
    @FXML
    private JFXButton generation;
    @FXML
    private JFXButton exit;


    @FXML
    void generate() {
        updatePrototypeImage();
    }

    @FXML
    void exit() {
        System.exit(0);
    }

    @FXML
    void updatePrototypeImage() {
        //convert image with parameters
        try {
            java.awt.Color color = getColor();
            Font font = getFont();
            String text = this.text.getText();
            int radius = (int) this.radius.getValue();
            int stepX = getStepX();
            int stepY = getStepY();

            BufferedImage bufferedImage = ImageHelper.addWaterMarkToImage(image, color, font, text, radius, stepX, stepY);
            WritableImage writableImage = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
            SwingFXUtils.toFXImage(bufferedImage, writableImage);
            imageView.setImage(writableImage);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert(e);
        }
    }

    @FXML
    public void initialize() {
        initFonts();
    }

    @FXML
    void initFonts() {
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = e.getAllFonts();
        List<String> fontNameList = new ArrayList<>();
        for (Font font : fonts)
            fontNameList.add(font.getFontName());

        fontList.setItems(FXCollections.observableList(fontNameList));
        fontList.getSelectionModel().selectFirst();
    }


    // ------------------------ HELPER METHODS ------------------------ //

    private void showErrorAlert(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(e.getClass().getName());
        alert.setContentText(e.getMessage());

        alert.showAndWait();
    }

    private Font getFont() {
        return new Font(fontList.getValue().toString(), 1, parseSafeIntFromString(sizeText.getText(), 1));
    }

    private java.awt.Color getColor() {
        float opacity = parseSafeFloatFromString(opacility.getText(), 1f) / 100;
        if (opacity > 1) opacity = 1.0f;
        return ImageHelper.convertFxColorToAwtColor(colorPicker.getValue(), opacity);
    }

    private int getStepX() {
        return parseSafeIntFromString(this.stepX.getText(), 1);
    }

    private int getStepY() {
        return parseSafeIntFromString(this.stepY.getText(), 1);
    }

    private int parseSafeIntFromString(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    private float parseSafeFloatFromString(String value, float defaultValue) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }


}
