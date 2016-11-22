package com.skylar.watermark.fx.task;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.awt.*;
import java.io.File;

/**
 * Created by Skylar on 11/21/2016.
 */
public class ImposeWatermarkService extends Service<Void> {

    private ImposeWatermarkTask task;

    public ImposeWatermarkService(Color color, Font font, String text, int radius, int stepX, int stepY, File sourceFolder, File destFolded) {
        task = new ImposeWatermarkTask(color, font, text, radius, stepX, stepY, sourceFolder, destFolded);
    }

    @Override
    protected Task<Void> createTask() {
        return task;
    }

    public int getCountProcessedFiles() {
        return task.getProcessedImages();
    }
}
