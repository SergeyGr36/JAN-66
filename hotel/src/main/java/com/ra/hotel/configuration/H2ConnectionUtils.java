package com.ra.hotel.configuration;

import com.ra.hotel.exception.DaoException;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.Scanner;

public final class H2ConnectionUtils {

    private final static Properties PROPS = new Properties();

    static {
        try {
            final InputStream resourceAsStream = Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("database.properties");
            if (resourceAsStream != null) {
                PROPS.load(resourceAsStream);
            } else {
                throw new MissingResourceException("database.properties not found", H2ConnectionUtils.class.getName(), "");
            }
        } catch (IOException e) {
            new DaoException(e.getMessage(),e);
        }
    }

    private H2ConnectionUtils() {
    }

    public static DataSource getDefaultDataSource() {
        final JdbcDataSource jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setUrl(PROPS.getProperty("database.url"));
        jdbcDataSource.setUser(PROPS.getProperty("database.username"));
        jdbcDataSource.setPassword(PROPS.getProperty("database.password"));
        initDatabase(jdbcDataSource);
        return jdbcDataSource;
    }

    public static void initDatabase(final JdbcDataSource dataSource) {
        try (Connection connection = dataSource.getConnection();) {
            connection.createStatement().execute(new Scanner(Thread.currentThread().getContextClassLoader().getResourceAsStream("sql/Client.sql")).useDelimiter("\\Z").next());
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }
}
