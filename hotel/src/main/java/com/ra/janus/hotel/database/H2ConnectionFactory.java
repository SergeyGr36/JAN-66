package com.ra.janus.hotel.database;

import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

public final class H2ConnectionFactory {

    private static JdbcDataSource dataSource;
    private static Properties connectProperties;
    private static H2ConnectionFactory factory;

    private H2ConnectionFactory() throws IOException {
        loadProperties();
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public static H2ConnectionFactory getInstance() throws IOException {
        synchronized (H2ConnectionFactory.class) {
            if (factory == null) {
                factory = new H2ConnectionFactory();
                dataSource = new JdbcDataSource();
                dataSource.setURL(connectProperties.getProperty("db.url"));
                dataSource.setUser(connectProperties.getProperty("db.user"));
                dataSource.setPassword(connectProperties.getProperty("db.pass"));

//                createDateSource();
            }
            return factory;
        }
    }

    private void loadProperties() throws IOException {
        connectProperties = new Properties();
        connectProperties.load(ClassLoader.getSystemResourceAsStream("connect.properties"));
    }

    private static void createDateSource() {
            dataSource = new JdbcDataSource();
            dataSource.setURL(connectProperties.getProperty("db.url"));
            dataSource.setUser(connectProperties.getProperty("db.user"));
            dataSource.setPassword(connectProperties.getProperty("db.pass"));
    }

}
