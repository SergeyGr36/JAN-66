package com.ra.course.janus.faculty.DataSource;
import org.h2.jdbcx.JdbcDataSource;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class DataSourceFactory {
    private static DataSourceFactory dataSourceFactory;
    private static JdbcDataSource dataSource;
    private static Properties dbProperties;

    private DataSourceFactory() throws IOException {
        dbProperties = new Properties();
        loadProperties();
    }

    private void loadProperties() throws IOException {
        dbProperties.load(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("config.properties")));
    }


    public static DataSourceFactory getInstance() throws IOException {
        synchronized (DataSourceFactory.class) {
            if (dataSourceFactory == null) {
                dataSourceFactory = new DataSourceFactory();
                dataSource = new JdbcDataSource();
                dataSource.setURL(dbProperties.getProperty("db.url"));
                dataSource.setUser(dbProperties.getProperty("db.username"));
                dataSource.setPassword(dbProperties.getProperty("db.password"));
            }
        }
        return dataSourceFactory;
    }
    }

