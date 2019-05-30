package com.ra.janus.developersteam.datasources;

import com.ra.janus.developersteam.exception.DataSourceException;
import com.ra.janus.developersteam.utils.PropertyReaderUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

public class DataSourceFactory {

    final private static String DB_URL = "db.url";
    final private static String DB_USER = "db.username";
    final private static String DB_PASS = "db.password";

    public DataSource get() {

        try {
            final Properties properties = PropertyReaderUtils.getProperties("config.properties");

            return HikDataSource.INSTANCE.get(properties.getProperty(DB_URL), properties.getProperty(DB_USER), properties.getProperty(DB_PASS));

        } catch (IOException ex) {
            throw new DataSourceException(ex);
        }
    }
}

