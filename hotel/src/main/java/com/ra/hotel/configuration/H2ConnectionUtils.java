package com.ra.hotel.configuration;

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

public final class H2ConnectionUtils {

    private final static Properties PROPS = new Properties();
    private static final Logger LOGGER = LoggerFactory.getLogger(H2ConnectionUtils.class);


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
            LOGGER.info(e.getMessage());
        }
    }

    private H2ConnectionUtils() {
    }

    public static DataSource getDefaultDataSource() {
        final HikariDataSource hikariDataSource;
        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(PROPS.getProperty("database.url"));
        config.setUsername(PROPS.getProperty("database.username"));
        config.setPassword(PROPS.getProperty("database.password"));
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "150");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "1000");
        hikariDataSource = new HikariDataSource(config);
        initDatabase(hikariDataSource);
        return hikariDataSource;
    }

    public static void initDatabase(final HikariDataSource dataSource) {
        try (Connection connection = dataSource.getConnection();) {
            connection.createStatement().execute(new Scanner(Thread.currentThread().getContextClassLoader().getResourceAsStream("sql/Client.sql")).useDelimiter("\\Z").next());
        } catch (SQLException e) {
            LOGGER.info(e.getMessage());
        }
    }
}
