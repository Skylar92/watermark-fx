package com.skylar.watermark.fx.helper;

import com.skylar.watermark.fx.utils.FileUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * Created by Skylar on 11/19/2016.
 */
public class PropertyStore {

    private Properties properties = new Properties();
    private PropertiesConfiguration config;

    public void loadProperties() {
        try {
            URL url = FileUtils.getFile("META-INF/configuration.properties");
            properties.load(new FileInputStream(url.getFile()));
            config = new PropertiesConfiguration(url.getFile());
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

    public void saveProperty(String propertyName, String propertyValue) {
        try {
            config.setProperty(propertyName, propertyValue);
            config.save();
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }

    }

}
