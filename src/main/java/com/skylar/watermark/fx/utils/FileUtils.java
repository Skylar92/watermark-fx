package com.skylar.watermark.fx.utils;

import java.io.FileNotFoundException;
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
