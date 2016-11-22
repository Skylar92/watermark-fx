package com.skylar.watermark.fx.task;

import com.skylar.watermark.fx.utils.ImageHelper;
import javafx.concurrent.Task;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Skylar on 11/21/2016.
 */
public class ImposeWatermarkTask extends Task<Void> {

    private Color color;
    private Font font;
    private String text;
    private int radius;
    private int stepX;
    private int stepY;
    private File sourceFolder;
    private File destFolded;
    private int processedImages;
    private int countImagesInSourceFolder;

    public ImposeWatermarkTask(Color color, Font font, String text, int radius, int stepX, int stepY, File sourceFolder, File destFolded) {
        this.color = color;
        this.font = font;
        this.text = text;
        this.radius = radius;
        this.stepX = stepX;
        this.stepY = stepY;
        this.sourceFolder = sourceFolder;
        this.destFolded = destFolded;
        this.processedImages = 0;
        this.countImagesInSourceFolder = 0;
    }

    @Override
    protected Void call() throws Exception {
        try {
            EventQueue.invokeLater(() -> countFiles(sourceFolder));
            generate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private void countFiles(File folder) {
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (ImageHelper.isImage(listOfFiles[i])) {
                countImagesInSourceFolder++;
            } else if (listOfFiles[i].isDirectory()) {
                countFiles(listOfFiles[i]);
            }
        }
    }

    private void generate() throws Exception {
        File[] files = sourceFolder.listFiles();
        this.doCopy(files, destFolded);

    }

    private void doCopy(File[] files, File destDir) throws Exception {
        File[] var3 = files;
        int var4 = files.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            File file = var3[var5];
            File copiedFile = new File(destDir, file.getName());
            if (file.isDirectory()) {
                this.doCopyDirectory(file, copiedFile);
            } else if (ImageHelper.isImage(file)) {
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

    public int getProcessedImages() {
        return processedImages;
    }

    public int getCountImagesInSourceFolder() {
        return countImagesInSourceFolder;
    }
}
