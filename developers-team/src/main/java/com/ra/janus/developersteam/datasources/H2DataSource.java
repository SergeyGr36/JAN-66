package com.ra.janus.developersteam.datasources;

import com.ra.janus.developersteam.exception.DataSourceException;
import com.ra.janus.developersteam.utils.PropertyReader;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

public enum H2DataSource implements IDataSource {

    INSTANCE;

    private JdbcDataSource dataSource;

    @Override
    public DataSource get() {

        try {
            if (dataSource == null) {
                final Properties properties = PropertyReader.INSTANCE.getProperties("config.properties");
                dataSource = new JdbcDataSource();
                dataSource.setURL(properties.getProperty("db.url"));
                dataSource.setUser(properties.getProperty("db.username"));
                dataSource.setPassword(properties.getProperty("db.password"));
            }
        } catch (IOException ex) {
            throw new DataSourceException(ex);
        }

        return dataSource;
    }
}
