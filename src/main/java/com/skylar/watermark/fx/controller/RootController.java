package com.skylar.watermark.fx.controller;

import com.jfoenix.controls.*;
import com.skylar.watermark.fx.helper.PropertyStore;
import com.skylar.watermark.fx.task.ImposeWatermarkService;
import com.skylar.watermark.fx.utils.FileUtils;
import com.skylar.watermark.fx.utils.UIUtils;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import com.skylar.watermark.fx.utils.ImageHelper;
import javafx.stage.*;
import org.controlsfx.dialog.ProgressDialog;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

import static com.skylar.watermark.fx.helper.WatermarkerProperty.*;
import static com.skylar.watermark.fx.utils.UIUtils.createWindow;
import static com.skylar.watermark.fx.utils.UIUtils.parseSafeFloatFromString;
import static com.skylar.watermark.fx.utils.UIUtils.parseSafeIntFromString;

public class RootController implements IController {

    private PropertyStore propertyStore;
    private Stage stage;

    public RootController() {
        propertyStore = new PropertyStore();
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
    @FXML
    private MenuItem configurationItem;

    private File sourceFolderFile;
    private File destinationFolderFile;

    @FXML
    void chooseSourceFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Укажите директорию с изображениями");
        File file = directoryChooser.showDialog(window);
        if (file != null) {
            labelSourceFolder.setText(file.getAbsolutePath());
            sourceFolderFile = file;
        }
        validateIsCanEnableGeneration();
    }


    @FXML
    void chooseDestinationFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Укажите директориюдля сохранения");
        File file = directoryChooser.showDialog(window);
        if (file != null) {
            labelDestinationFolder.setText(file.getAbsolutePath());
            destinationFolderFile = file;
        }
        validateIsCanEnableGeneration();
    }

    @FXML
    void generate() {
        final ImposeWatermarkService service = new ImposeWatermarkService(getColor(), getFont(), getText(), getRadius(), getStepX(), getStepY(), sourceFolderFile, destinationFolderFile);
        ProgressDialog progressDialog = new ProgressDialog(service);
        progressDialog.setTitle("Обработка файлов");
        progressDialog.setOnCloseRequest(event -> service.cancel());
        progressDialog.setOnHiding(event -> UIUtils.showAlert("Обработка завершена", "Обработанно " + service.getCountProcessedFiles() + " файлов"));
        service.start();
    }

    @FXML
    void exit() {
        System.exit(0);
    }

    @FXML
    void updatePrototypeImage() {
        //convert image with parameters
        try (InputStream inputStream = FileUtils.getResourceAsStream("/META-INF/image/default-slide-img.jpg")) {
            BufferedImage bufferedImage = ImageHelper.addWaterMarkToImage(inputStream, getColor(), getFont(), getText(), getRadius(), getStepX(), getStepY());
            WritableImage writableImage = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
            SwingFXUtils.toFXImage(bufferedImage, writableImage);
            imageView.setImage(writableImage);
        } catch (Exception e) {
            e.printStackTrace();
            UIUtils.showErrorAlert(e);
        }
    }

    @FXML
    public void initialize() {
        setUp();
        addCustomListener();
        initializeDefaultFiledsWithProperties();
        updatePrototypeImage();
        validateIsCanEnableGeneration();
    }

    private void setUp() {
        fontList.setItems(FXCollections.observableList(UIUtils.getFontNames()));
    }

    private void addCustomListener() {
        this.radius.valueProperty().addListener((observable, oldValue, newValue) -> updatePrototypeImage());
        this.text.setOnKeyReleased(event -> {
            validateIsCanEnableGeneration();
            updatePrototypeImage();
        });
    }

    private void initializeDefaultFiledsWithProperties() {
        fontList.getSelectionModel().select(propertyStore.getProperty(FONT));
        colorPicker.setValue(javafx.scene.paint.Color.valueOf(propertyStore.getProperty(COLOR)));
        opacility.setText(propertyStore.getProperty(OPACITY));
        sizeText.setText(propertyStore.getProperty(TEXT_SIZE));
        stepX.setText(propertyStore.getProperty(STEP_X));
        stepY.setText(propertyStore.getProperty(STEP_Y));
        radius.setValue(Double.parseDouble(propertyStore.getProperty(RADIUS)));
        text.setText(propertyStore.getProperty(TEXT));
    }


    @FXML
    void openConfigurationWindow() {
        try {
            Stage window = createWindow(FileUtils.getFile("META-INF/fxml/configuration.fxml"),
                    "Стандартные параметры",
                    new ConfigurationController(propertyStore),
                    Modality.NONE,
                    StageStyle.UNDECORATED);
            window.show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // ------------------------ HELPER METHODS ------------------------ //


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


    private void validateIsCanEnableGeneration() {
        generation.setDisable(true);
        if (!labelDestinationFolder.getText().isEmpty() && !labelSourceFolder.getText().isEmpty() && !text.getText().isEmpty())
            generation.setDisable(false);
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
