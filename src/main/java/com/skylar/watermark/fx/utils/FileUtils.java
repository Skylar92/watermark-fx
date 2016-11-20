package com.skylar.watermark.fx.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Skylar on 11/18/2016.
 */
public class FileUtils {

    public static URL getFile(String path) {
        URL resource = FileUtils.class.getClassLoader().getResource(path);
        if (resource == null)
            throw new RuntimeException(new FileNotFoundException("Not found file in " + path));
        return resource;
    }

    public static InputStream getResourceAsStream(String path) {
        InputStream resource = FileUtils.class.getResourceAsStream(path);
        if (resource == null)
            throw new RuntimeException(new FileNotFoundException("Not found file in " + path));
        return resource;
    }

    public static URL getResource(String path) {
        URL resource = FileUtils.class.getResource(path);
        if (resource == null)
            throw new RuntimeException(new FileNotFoundException("Not found file in " + path));
        return resource;
    }

}
