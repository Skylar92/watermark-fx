package com.skylar.watermark.fx.app;

import com.skylar.watermark.fx.utils.FileUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppRunner extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(FileUtils.getFile("fxml/root.fxml"));
        primaryStage.setTitle("Watermark");
        primaryStage.setScene(new Scene(root, 600, 800));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
