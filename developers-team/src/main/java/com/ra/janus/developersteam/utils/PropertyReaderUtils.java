package com.ra.janus.developersteam.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * PropertyReaderUtils
 * <p>
 * Can work with many property files.
 * Default properties file is "config.properties"
 */
public final class PropertyReaderUtils{

    private PropertyReaderUtils() {
    }

    public static Properties getProperties() {
        return getProperties("config.properties");
    }

    public static Properties getProperties(final String propertiesFile){
        final Properties properties = new Properties();
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesFile)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read the application properties file.", e);
        }

        return properties;
    }
}
