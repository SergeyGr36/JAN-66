package com.ra.course.janus.faculty.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class DataSourceUtils {

    final private static String CONFIG_FILE = "config.properties";
    final private static Properties DB_PROPERTIES = new Properties();
    final private static HikariConfig CONFIG = new HikariConfig();

    private DataSourceUtils() {
    }

    private static void loadProperties(){
        try {
            DB_PROPERTIES.load(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream(CONFIG_FILE)));
        }
        catch (IOException e){
            throw new IllegalStateException("Could not read the application properties file.", e);
        }
    }

    private static void initConfig() {
       CONFIG.setJdbcUrl(DB_PROPERTIES.getProperty("db.url") );/*+*//* ";INIT=runscript from 'classpath:scripts/" + DB_PROPERTIES.getProperty("db.start") + "'");*/
        CONFIG.setUsername(DB_PROPERTIES.getProperty("db.username"));
        CONFIG.setPassword(DB_PROPERTIES.getProperty("db.password"));
        CONFIG.addDataSourceProperty("cachePrepStmts", "true");
        CONFIG.addDataSourceProperty("prepStmtCacheSize", "250");
        CONFIG.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    }

    public static DataSource getDataSource() {
        loadProperties();
        initConfig();
        return new HikariDataSource(CONFIG);
    }

}
