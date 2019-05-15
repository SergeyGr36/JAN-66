package com.ra.janus.developersteam.connections;

import com.ra.janus.developersteam.utils.PropertyReader;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public enum H2Connection implements iConnection {

    INSTANCE;

    private JdbcDataSource dataSource;
    private Properties properties;

    private DataSource init() throws IOException {

        properties = PropertyReader.INSTANCE.getProperties("config.properties");

        if (dataSource == null) {
            dataSource = new JdbcDataSource();
            dataSource.setURL(properties.getProperty("db.url"));
            dataSource.setUser(properties.getProperty("db.username"));
            dataSource.setPassword(properties.getProperty("db.password"));
        }

        return dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException, IOException {
        return init().getConnection();
    }
}
