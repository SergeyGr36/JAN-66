package com.ra.course.janus.faculty.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

final public class DataSourceFactoryHelper {

    final private static String CONFIG_FILE = "config.properties";
    final private static Properties DB_PROPERTIES = new Properties();
    final private static HikariConfig CONFIG = new HikariConfig();

    private DataSourceFactoryHelper() throws IOException {

    }

    private static void loadProperties() throws IOException {
       // if (!dbProperties.isEmpty()) {
        DB_PROPERTIES.load(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream(CONFIG_FILE)));
       // }
    }

    private static void initConfig() throws IOException{
            CONFIG.setJdbcUrl(DB_PROPERTIES.getProperty("db.url")+
                                  ";INIT=runscript from 'classpath:scripts/"+
                    DB_PROPERTIES.getProperty("db.start")+"'");
            CONFIG.setUsername(DB_PROPERTIES.getProperty("db.username"));
            CONFIG.setPassword(DB_PROPERTIES.getProperty("db.password"));
            CONFIG.addDataSourceProperty("cachePrepStmts", "true");
            CONFIG.addDataSourceProperty("prepStmtCacheSize", "250");
            CONFIG.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    }

    public static DataSource getDataSource() throws IOException{
        loadProperties();
        initConfig();
        return new HikariDataSource(CONFIG);
    }




}

