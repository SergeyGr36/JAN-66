package com.ra.course.janus.faculty.DataSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReaderUtil {
    private PropertyReaderUtil() {
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
