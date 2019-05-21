package com.ra.janus.hotel.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

public class H2PoolConnectionFactory {

    private static HikariDataSource dataSource;
    private static HikariConfig poolconfig;
    private static H2PoolConnectionFactory poolConnectionFactory;

    public H2PoolConnectionFactory() throws IOException {
        createPoolConfig();
    }

    private void createPoolConfig() throws IOException {
        Properties properties = new Properties();
        properties.load(ClassLoader.getSystemResourceAsStream("poolconnect.properties"));
        poolconfig = new HikariConfig();
        poolconfig.setJdbcUrl(properties.getProperty("jdbc_url"));
        poolconfig.setUsername(properties.getProperty("database_username"));
        poolconfig.setPassword(properties.getProperty("database_password"));
        poolconfig.addDataSourceProperty( "cachePrepStmts" , "true" );
        poolconfig.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        poolconfig.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
    }

    public static H2PoolConnectionFactory getInstance() throws IOException {
        synchronized (H2PoolConnectionFactory.class) {
            if (poolConnectionFactory == null) {
                poolConnectionFactory = new H2PoolConnectionFactory();
                dataSource = new HikariDataSource(poolconfig);
            }
            return poolConnectionFactory;
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

}
