package com.ra.janus.developersteam.datasources;

import com.ra.janus.developersteam.utils.PropertyReaderUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

final public class DataSourceFactory {

    final private static HikariConfig CONFIG = new HikariConfig();

    final private static String DB_URL = "db.url";
    final private static String DB_USER = "db.username";
    final private static String DB_PASS = "db.password";

    private DataSourceFactory() {
    }

    public static DataSource get() {

            initConfig();
            return new HikariDataSource(CONFIG);
    }

    private static void initConfig() {

        try {
            final Properties properties = PropertyReaderUtils.getProperties("config.properties");

            CONFIG.setJdbcUrl(properties.getProperty(DB_URL));
            CONFIG.setUsername(properties.getProperty(DB_USER));
            CONFIG.setPassword(properties.getProperty(DB_PASS));
            CONFIG.addDataSourceProperty("cachePrepStmts", "true");
            CONFIG.addDataSourceProperty("prepStmtCacheSize", "250");
            CONFIG.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        } catch (IOException e) {
            throw new IllegalStateException("Could not read the application properties file.", e);
        }
    }
}

