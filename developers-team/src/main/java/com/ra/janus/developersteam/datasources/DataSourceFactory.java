package com.ra.janus.developersteam.datasources;

import com.ra.janus.developersteam.exception.DataSourceException;
import com.ra.janus.developersteam.utils.PropertyReader;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

public class DataSourceFactory {

    private transient IDataSource iDataSource;
    final private static String DB_URL = "db.url";
    final private static String DB_USER = "db.username";
    final private static String DB_PASS = "db.password";

    public DataSource get(final DataSourceType dataSourceType) {

        try {
            final Properties properties = PropertyReader.INSTANCE.getProperties("config.properties");

            if (dataSourceType == DataSourceType.H2) {
                iDataSource = H2DataSource.INSTANCE;
            } else if (dataSourceType == DataSourceType.HIKARI) {
                iDataSource = HikDataSource.INSTANCE;
            }

            return iDataSource.get(properties.getProperty(DB_URL), properties.getProperty(DB_USER), properties.getProperty(DB_PASS));

        } catch (IOException ex) {
            throw new DataSourceException(ex);
        }
    }
}

