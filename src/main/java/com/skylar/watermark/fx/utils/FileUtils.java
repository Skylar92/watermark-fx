package com.skylar.watermark.fx.utils;

import java.io.FileNotFoundException;
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

}
