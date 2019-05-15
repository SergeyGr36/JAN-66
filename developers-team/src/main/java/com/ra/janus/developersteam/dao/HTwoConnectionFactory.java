package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.dao.interfaces.ConnectionFactory;
import org.h2.jdbcx.JdbcDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

public final class HTwoConnectionFactory implements ConnectionFactory {
    private static HTwoConnectionFactory connFactory;
    private static JdbcDataSource dataSource;
    private static Properties dbProperties = new Properties();

    private HTwoConnectionFactory() throws IOException {
        loadProperties();
    }
    
    /**
     * this method create Property and load data from config.properties file.
     */
    private void loadProperties() throws IOException {
        dbProperties.load(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("config.properties")));
    }

    /**
     * Method to produce singleton config factory.
     *
     * @return ConnectionFactory instance.
     */
    public static HTwoConnectionFactory getInstance() throws IOException {
        synchronized (HTwoConnectionFactory.class) {
            if (connFactory == null) {
                connFactory = new HTwoConnectionFactory();
                dataSource = new JdbcDataSource();
                dataSource.setURL(dbProperties.getProperty("db.url"));
                dataSource.setUser(dbProperties.getProperty("db.username"));
                dataSource.setPassword(dbProperties.getProperty("db.password"));
            }
        }
        return connFactory;
    }


    /**
     * Returns new config.
     *
     * @return Connection.
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
