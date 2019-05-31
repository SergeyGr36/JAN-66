package com.ra.course.janus.faculty.connection;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.log4j.Logger;

import javax.sql.DataSource;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConnectToDb {
    private final static Logger logger = Logger.getLogger(ConnectToDb.class);

    private static Properties properties;

    public ConnectToDb(String name) {
        loadProperties(name);
    }

    private void loadProperties(String name) {
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(name))) {
            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public DataSource connect() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(properties.getProperty("db.url"));
        hikariDataSource.setUsername(properties.getProperty("db.username"));
        hikariDataSource.setPassword(properties.getProperty("db.password"));
        return hikariDataSource;
    }
}
