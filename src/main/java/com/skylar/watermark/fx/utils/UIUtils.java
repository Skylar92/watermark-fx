package com.skylar.watermark.fx.utils;

import javafx.scene.control.Alert;

import java.awt.*;
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

}
