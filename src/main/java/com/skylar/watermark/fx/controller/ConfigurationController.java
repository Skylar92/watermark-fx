package com.skylar.watermark.fx.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.skylar.watermark.fx.helper.PropertyStore;
import com.skylar.watermark.fx.utils.UIUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static com.skylar.watermark.fx.helper.WatermarkerProperty.*;

/**
 * Created by Skylar on 11/20/2016.
 */
public class ConfigurationController implements IController {

    private PropertyStore propertyStore;
    private Stage stage;

    public ConfigurationController(PropertyStore propertyStore) {
        this.propertyStore = propertyStore;
    }

    @FXML
    private JFXButton close;
    @FXML
    private JFXButton save;

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

    @FXML
    public void close() {
        stage.close();
    }

    @FXML
    public void save() {
        propertyStore.saveProperty(COLOR, colorPicker.getValue().toString());
        propertyStore.saveProperty(OPACITY, opacility.getText());
        propertyStore.saveProperty(TEXT_SIZE, sizeText.getText());
        propertyStore.saveProperty(STEP_X, stepX.getText());
        propertyStore.saveProperty(STEP_Y, stepY.getText());
        propertyStore.saveProperty(RADIUS, String.valueOf(this.radius.getValue()));
        propertyStore.saveProperty(TEXT, text.getText());
        propertyStore.saveProperty(FONT, fontList.getSelectionModel().getSelectedItem().toString());
        UIUtils.showAlert("Выполнено! Настройки сохранены", "Требуется перезагрузить приложения для загрузки новых настроек");
        stage.close();
    }

    @FXML
    public void initialize() {
        colorPicker.setValue(Color.valueOf(propertyStore.getProperty(COLOR)));
        opacility.setText(propertyStore.getProperty(OPACITY));
        sizeText.setText(propertyStore.getProperty(TEXT_SIZE));
        stepX.setText(propertyStore.getProperty(STEP_X));
        stepY.setText(propertyStore.getProperty(STEP_Y));
        radius.setValue(Double.parseDouble(propertyStore.getProperty(RADIUS)));
        text.setText(propertyStore.getProperty(TEXT));

        fontList.setItems(FXCollections.observableList(UIUtils.getFontNames()));
        fontList.getSelectionModel().select(propertyStore.getProperty(FONT));
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
