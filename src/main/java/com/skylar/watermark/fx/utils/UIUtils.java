package com.skylar.watermark.fx.utils;

import com.skylar.watermark.fx.controller.ConfigurationController;
import com.skylar.watermark.fx.controller.IController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Created by Skylar on 11/20/2016.
 */
public class UIUtils {

    public static List<String> getFontNames() {
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = e.getAllFonts();
        List<String> fontNameList = new ArrayList<>();
        for (Font font : fonts)
            fontNameList.add(font.getFontName());
        return fontNameList;
    }

    public static void showErrorAlert(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(e.getClass().getName());
        alert.setContentText(e.getMessage());

        alert.showAndWait();
    }

    public static void showAlert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(text);
        alert.showAndWait();
    }

    public static <T extends IController> Stage createWindow(URL fileFxml, String title, T controller, Modality modality, StageStyle style) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(fileFxml);
        controller.setStage(stage);
        fxmlLoader.setController(controller);
        Parent root = fxmlLoader.load();
        stage.initModality(modality);
        stage.initStyle(style);
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        return stage;
    }

    public static int parseSafeIntFromString(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public static float parseSafeFloatFromString(String value, float defaultValue) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

}
