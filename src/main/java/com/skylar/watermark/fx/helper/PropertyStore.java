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

    public PropertyStore() {
        loadProperties();
    }

    public void loadProperties() {
        try {
            File propertiesFile = new File(".\\META-INF\\configuration.properties");
            properties.load(new FileInputStream(propertiesFile));
            config = new PropertiesConfiguration(propertiesFile);
        } catch (IOException | ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }

    public Integer getPropertyByInteger(String propertyName) {
        return Integer.parseInt(properties.getProperty(propertyName));
    }

    public boolean saveProperty(String propertyName, String propertyValue) {
        if (config == null)
            return false;
        try {
            config.setProperty(propertyName, propertyValue);
            config.save();
            return true;
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }

    }

}
