package com.skylar.watermark.fx.controller;

import com.jfoenix.controls.*;
import com.skylar.watermark.fx.utils.FileUtils;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import com.skylar.watermark.fx.utils.ImageHelper;
import javafx.stage.*;
import org.controlsfx.dialog.ProgressDialog;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RootController {

    private static final File image = new File(FileUtils.getFile("image/default-slide-img.jpg").getFile());

    private int countImagesInSourceFolder;

    public RootController() {
    }

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
    //Folder chooser
    @FXML
    private JFXButton sourceFolder;
    @FXML
    private JFXButton destinationFolder;
    @FXML
    private Label labelSourceFolder;
    @FXML
    private Label labelDestinationFolder;
    @FXML
    private javafx.stage.Window window;

    private File sourceFolderFile;
    private File destinationFolderFile;

    @FXML
    void chooseSourceFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Укажите директорию с изображениями");
        File file = directoryChooser.showDialog(window);
        labelSourceFolder.setText(file.getAbsolutePath());
        sourceFolderFile = file;
        validateIsCanEnableGeneration();

        EventQueue.invokeLater(()-> countFiles(sourceFolderFile));
    }



    @FXML
    void chooseDestinationFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Укажите директориюдля сохранения");
        File file = directoryChooser.showDialog(window);
        labelDestinationFolder.setText(file.getAbsolutePath());
        destinationFolderFile = file;
        validateIsCanEnableGeneration();
    }

    @FXML
    void generate() {
        final Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new TaskVoid();
            }
        };

        ProgressDialog progressDialog = new ProgressDialog(service);
        progressDialog.setTitle("Обработка файлов");
        service.start();
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
            String text = getText();
            int radius = getRadius();
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
        validateIsCanEnableGeneration();
        this.radius.valueProperty().addListener((observable, oldValue, newValue) -> updatePrototypeImage());
        this.text.setOnKeyReleased(event -> {
            validateIsCanEnableGeneration();
            updatePrototypeImage();
        });
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

    private String getText() {
        return this.text.getText();
    }

    private int getRadius() {
        return (int) this.radius.getValue();
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

    private File getSourceDirectory() {
        return sourceFolderFile;
    }

    private File getDestinationFolder() {
        return destinationFolderFile;
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

    private void validateIsCanEnableGeneration() {
        generation.setDisable(true);
        if (!labelDestinationFolder.getText().isEmpty() && !labelSourceFolder.getText().isEmpty() && !text.getText().isEmpty())
            generation.setDisable(false);
    }

    private void countFiles(File folder) {
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (isImage(listOfFiles[i])) {
                countImagesInSourceFolder++;
            } else if (listOfFiles[i].isDirectory()) {
                countFiles(listOfFiles[i]);
            }
        }
    }

    private boolean isImage(File file) {
        try {
            return ImageIO.read(file) != null;
        } catch (IOException var3) {
            return false;
        }
    }

    private class TaskVoid extends Task<Void> {

        private int processedImages;

        public TaskVoid() {
            processedImages = 0;
        }

        @Override
        protected Void call() throws Exception {
            generate();
            return null;
        }

        private void generate() throws Exception {
            File[] files = getSourceDirectory().listFiles();
            this.doCopy(files, getDestinationFolder());

        }

        private void doCopy(File[] files, File destDir) throws Exception {
            File[] var3 = files;
            int var4 = files.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                File file = var3[var5];
                File copiedFile = new File(destDir, file.getName());
                if (file.isDirectory()) {
                    this.doCopyDirectory(file, copiedFile);
                } else if (isImage(file)) {
                    this.doCopyFile(file, copiedFile);
                }
            }

        }

        private void doCopyDirectory(File srcDir, File destDir) throws Exception {
            if (destDir.exists()) {
                if (!destDir.isDirectory()) {
                    throw new IOException("Destination \'" + destDir + "\' exists but is not a directory");
                }
            } else if (!destDir.mkdirs()) {
                return;
            }

            File[] files = srcDir.listFiles();
            if (files == null) {
                throw new IOException("Failed to list contents of " + srcDir);
            } else {
                this.doCopy(files, destDir);
            }
        }

        private void doCopyFile(File srcFile, File destFile) throws Exception {
            if (destFile.isDirectory()) {
                throw new IOException("Destination \'" + destFile + "\' exists but is a directory");
            } else {
                String format = substringAfterLast(srcFile.getName(), ".");
                format = isEmpty(format) ? "jpeg" : format;

                java.awt.Color color = getColor();
                Font font = getFont();
                String text = getText();
                int radius = getRadius();
                int stepX = getStepX();
                int stepY = getStepY();

                BufferedImage bufferedImage = ImageHelper.addWaterMarkToImage(srcFile, color, font, text, radius, stepX, stepY);
                ImageIO.write(bufferedImage, format, destFile);
                processedImages++;
                updateMessage("Обработано " + processedImages + " из " + countImagesInSourceFolder);
            }
        }

        private String substringAfterLast(String str, String separator) {
            if (isEmpty(str)) {
                return str;
            } else if (isEmpty(separator)) {
                return "";
            } else {
                int pos = str.lastIndexOf(separator);
                return pos != -1 && pos != str.length() - separator.length() ? str.substring(pos + separator.length()) : "";
            }
        }

        private boolean isEmpty(CharSequence cs) {
            return cs == null || cs.length() == 0;
        }


    }

}
