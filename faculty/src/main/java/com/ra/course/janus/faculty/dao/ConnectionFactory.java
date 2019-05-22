package com.ra.course.janus.faculty.dao;

import org.h2.jdbcx.JdbcDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

public final class ConnectionFactory {

        private static final String CONFIG_FILE = "config.properties";

        private static ConnectionFactory connFactory;
        private static JdbcDataSource dataSource;
        private static Properties dbProperties = new Properties();

        private ConnectionFactory() throws IOException {
            //dbProperties = new Properties();
            loadProperties();
        }

        /**
         * this method create Property and load data from config.properties file.
         */
        private void loadProperties() throws IOException {
            dbProperties.load(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream(CONFIG_FILE)));
        }

        /**
         * Method to produce singleton config factory.
         *
         * @return ConnectionFactory instance.
         */
        public static ConnectionFactory getInstance() throws IOException {
            synchronized (ConnectionFactory.class) {
                if (connFactory == null) {
                    connFactory = new ConnectionFactory();
                    dataSource = new JdbcDataSource();
                    dataSource.setURL(dbProperties.getProperty("db.url")+";INIT=runscript from 'classpath:scripts/"+
                                      dbProperties.getProperty("db.start")+"'"
                                    );
                    dataSource.setUser(dbProperties.getProperty("db.username"));
                    dataSource.setPassword(dbProperties.getProperty("db.password"));
                }
            }
            return connFactory;
        }


        /**
         * Returns new config.
         *
         * @return Connection.
         */
        public Connection getConnection() throws SQLException {
            return dataSource.getConnection();
        }
    }

