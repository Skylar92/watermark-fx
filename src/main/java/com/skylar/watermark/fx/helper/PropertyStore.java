package com.skylar.watermark.fx.helper;

import com.skylar.watermark.fx.utils.FileUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.*;
import java.util.Properties;

/**
 * Created by Skylar on 11/19/2016.
 */
public class PropertyStore {

    private Properties properties = new Properties();
    private PropertiesConfiguration config;

    public void loadProperties() {
        try {
            InputStream resourceAsStream = FileUtils.getResourceAsStream("/META-INF/configuration.properties");
            properties.load(resourceAsStream);
            //don't work in JavaFX Application
            config = null;//new PropertiesConfiguration(FileUtils.getFile("META-INF/configuration.properties").getFile());
        } catch (IOException  e) {
            throw new RuntimeException(e);
        }
    }

    public String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }

    public Integer getPropertyByInteger(String propertyName) {
        return Integer.parseInt(properties.getProperty(propertyName));
    }

    public void saveProperty(String propertyName, String propertyValue) {
        if (config == null)
            return;
        try {
            config.setProperty(propertyName, propertyValue);
            config.save();
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }

    }

}
