package com.ra.janus.developersteam.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * PropertyReader is Singleton
 *
 * Can work with many property files. implemented as HashMap where key is
 * properties file name. default properties file is "application.properties"
 *
 */
public enum PropertyReader {

    INSTANCE;

    private Map<String, Properties> propMap;

    public Properties getProperties() throws IOException {
        return getProperties("application.properties");
    }

    public Properties getProperties(final String propertiesFile) throws IOException {
        if (propMap == null) {
            propMap = new HashMap<>();
        }

        if (!propMap.containsKey(propertiesFile)) {
            propMap.put(propertiesFile, new Properties());
        }

        final Properties properties = propMap.get(propertiesFile);
        if (properties.isEmpty()) {

            //try (InputStream inputStream = PropertyReader.class.getClassLoader().getResourceAsStream(propertiesFile)) {
            try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesFile)) {
                properties.load(inputStream);
            }
        }

        return properties;
    }
}
