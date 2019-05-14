package com.ra.janus.hotel.database;

import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.Properties;

public final class H2ConnectionUtils {

    private final static Properties props = new Properties();

    static {
        try {
            final InputStream resourceAsStream = H2ConnectionUtils.class
                    .getClassLoader()
                    .getResourceAsStream("database.properties");
            if (resourceAsStream != null) {
                props.load(resourceAsStream);
            } else {
                throw new MissingResourceException("database.properties not found", H2ConnectionUtils.class.getName(), "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private H2ConnectionUtils() {
    }

    public static DataSource getDefaultDataSource() {
        final JdbcDataSource jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setUrl(props.getProperty("database.url"));
        jdbcDataSource.setUser(props.getProperty("database.username"));
        jdbcDataSource.setPassword(props.getProperty("database.password"));
        return jdbcDataSource;
    }

}
