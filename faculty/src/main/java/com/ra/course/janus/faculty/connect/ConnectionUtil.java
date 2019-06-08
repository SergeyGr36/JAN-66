package com.ra.course.janus.faculty.connect;

import com.ra.course.janus.faculty.exception.DaoException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.log4j.Logger;

import javax.sql.DataSource;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public final class ConnectionUtil {
    private final static Logger LOGGER = Logger.getLogger(ConnectionUtil.class);
    private final static String FILE = "config.properties";
    private final static Properties PROPERTIES = new Properties();
    private final static HikariConfig HIKARI_CONFIG = new HikariConfig();

    private ConnectionUtil() {}

    private static void loadProperties() throws IOException {
        PROPERTIES.load(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream(FILE)));
    }

    private static void initDataSource() {
        HIKARI_CONFIG.setJdbcUrl(PROPERTIES.getProperty("db.url"));
        HIKARI_CONFIG.setUsername(PROPERTIES.getProperty("db.username"));
        HIKARI_CONFIG.setPassword(PROPERTIES.getProperty("db.password"));
    }

    public static DataSource getDataSource() {
        try {
            loadProperties();
        } catch (IOException e) {
            LOGGER.error(e);
            throw new DaoException("Error loading DB properties",e);
        }

        initDataSource();
        return new HikariDataSource(HIKARI_CONFIG);
    }
}
