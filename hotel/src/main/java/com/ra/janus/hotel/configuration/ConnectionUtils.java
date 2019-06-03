package com.ra.janus.hotel.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.Scanner;

public final class ConnectionUtils {

    private final static Properties PROPS = new Properties();
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionUtils.class);


    static {
        try {
            final InputStream resourceAsStream = Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("connect.properties");
            if (resourceAsStream != null) {
                PROPS.load(resourceAsStream);
            } else {
                throw new MissingResourceException("connect.properties not found", ConnectionUtils.class.getName(), "");
            }
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
    }

    private ConnectionUtils() {
    }

    public static DataSource getDefaultDataSource() {
        final HikariDataSource hikariDataSource;
        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(PROPS.getProperty("db.url"));
        config.setUsername(PROPS.getProperty("db.user"));
        config.setPassword(PROPS.getProperty("db.pass"));
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "150");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "1000");
        hikariDataSource = new HikariDataSource(config);
        initDatabase(hikariDataSource);
        return hikariDataSource;
    }

    public static void initDatabase(final HikariDataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.createStatement().execute(new Scanner(Thread.currentThread().getContextClassLoader().getResourceAsStream("sql/create-tables.sql"), "UTF-8").useDelimiter("\\Z").next());
        } catch (SQLException e) {
            LOGGER.info(e.getMessage());
        }
    }
}
