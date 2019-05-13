package com.ra.janus.hotel.database;

import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public final class H2ConnectionFactory {

    private static JdbcDataSource dataSource;
    private static Properties connectProperties;
    private static H2ConnectionFactory factory;

    private H2ConnectionFactory() throws IOException {
        loadProperties();
        createDateSource();
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public static H2ConnectionFactory getInstance() throws SQLException, IOException {
        synchronized (H2ConnectionFactory.class) {

            if (factory == null) {
                factory = new H2ConnectionFactory();
            }
            return factory;
        }
    }

    private void loadProperties() throws IOException {
        connectProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("connect.properties"));
    }

    private void createDateSource() {
        if (dataSource == null) {
            dataSource = new JdbcDataSource();
            dataSource.setURL(connectProperties.getProperty("db.url"));
            dataSource.setUser(connectProperties.getProperty("db.user"));
            dataSource.setPassword(connectProperties.getProperty("db.pass"));
       }
    }

}
