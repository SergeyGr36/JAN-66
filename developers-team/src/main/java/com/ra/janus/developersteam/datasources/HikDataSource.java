package com.ra.janus.developersteam.datasources;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public enum HikDataSource implements IDataSource {

    INSTANCE;

    private HikariConfig config = new HikariConfig();
    private HikariDataSource dataSource;

    @Override
    public DataSource get(final String url, final String user, final String pass) {

        if (dataSource == null) {

            config.setJdbcUrl(url);
            config.setUsername(user);
            config.setPassword(pass);
            config.addDataSourceProperty( "cachePrepStmts" , "true" );
            config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
            config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
            dataSource = new HikariDataSource(config);

        }

        return dataSource;
    }
}
