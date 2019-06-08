package com.ra.janus.developersteam.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * PropertyReaderUtils
 * <p>
 * Can work with many property files.
 * Default properties file is "configuration.properties"
 */
public final class PropertyReaderUtils{

    private PropertyReaderUtils() {
    }

    public static Properties getProperties() throws IOException {
        return getProperties("config.properties");
    }

    public static Properties getProperties(final String propertiesFile) throws IOException {
        final Properties properties = new Properties();
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesFile)) {
            properties.load(inputStream);
        }

        return properties;
    }
}
