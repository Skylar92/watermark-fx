package com.skylar.watermark.fx.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by Skylar on 11/17/2016.
 */
public class ImageHelper {

    public static BufferedImage addWaterMarkToImage(File image, Color color, Font font, String text, int radius, int stepX, int stepY) throws Exception {
        try (FileInputStream inputStream = new FileInputStream(image)) {
            return addWaterMarkToImage(inputStream, color, font, text, radius, stepX, stepY);
        }
    }

    public static BufferedImage addWaterMarkToImage(InputStream image, Color color, Font font, String text, int radius, int stepX, int stepY) throws Exception {
        BufferedImage processImage = ImageIO.read(image);
        int height = processImage.getHeight();
        int width = processImage.getWidth();
        BufferedImage destImage = new BufferedImage(width, height, 1);
        Graphics graphics = destImage.getGraphics();
        graphics.drawImage(processImage, 0, 0, width, height, null);
        graphics.setColor(color);
        fillText(graphics, width, height, font, text, radius, stepX, stepY);
        graphics.dispose();
        return destImage;
    }

    private static void fillText(Graphics graphics, int width, int height, Font font, String text, int radius, int stepX, int stepY) {
        AffineTransform affinetransform = new AffineTransform();
        affinetransform.rotate(Math.toRadians((double) radius));
        graphics.setFont(font.deriveFont(affinetransform));
        FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
        int widthText = (int) font.getStringBounds(text, frc).getWidth() + stepX;
        int heightText = (int) font.getStringBounds(text, frc).getHeight() + stepY;

        for (int y = -height; y < height; y += heightText) {
            for (int x = -width; x < width * 2; x += widthText) {
                graphics.drawString(text, x, y);
            }
        }

    }

    public static java.awt.Color convertFxColorToAwtColor(javafx.scene.paint.Color fx, float opacity) {
        return new java.awt.Color((float) fx.getRed(),
                (float) fx.getGreen(),
                (float) fx.getBlue(),
                 opacity);
    }

}
