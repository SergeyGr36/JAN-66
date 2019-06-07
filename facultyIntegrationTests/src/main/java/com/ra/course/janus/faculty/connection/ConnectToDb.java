package com.ra.course.janus.faculty.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConnectToDb {
    private ConnectToDb() {}

    private final static String file = "C:\\Users\\User\\Desktop\\projects\\Java\\Hillel\\janus\\facultyIntegrationTests\\src\\test\\resources\\config.properties";
    private final static Properties properties = new Properties();
    private final static HikariConfig hikariConfig = new HikariConfig();

    private static void loadProperties() throws IOException {
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        properties.load(inputStream);
    }

    private static void initDataSource() {
        hikariConfig.setJdbcUrl(properties.getProperty("db.url"));
        hikariConfig.setUsername(properties.getProperty("db.username"));
        hikariConfig.setPassword(properties.getProperty("db.password"));
    }

    public static DataSource getDataSource() throws IOException {
        loadProperties();
        initDataSource();
        return new HikariDataSource(hikariConfig);
    }
}
