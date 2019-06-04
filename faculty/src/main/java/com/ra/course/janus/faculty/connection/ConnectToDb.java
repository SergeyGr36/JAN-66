package com.ra.course.janus.faculty.connection;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.log4j.Logger;

import javax.sql.DataSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class ConnectToDb {
    private final static Logger logger = Logger.getLogger(ConnectToDb.class);

    Properties loadProperties(String name) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(name)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    DataSource connect(Properties properties) {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(properties.getProperty("db.url"));
        hikariDataSource.setUsername(properties.getProperty("db.username"));
        hikariDataSource.setPassword(properties.getProperty("db.password"));
        return hikariDataSource;
    }
}
