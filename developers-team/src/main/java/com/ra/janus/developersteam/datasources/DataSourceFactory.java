package com.ra.janus.developersteam.datasources;

import com.ra.janus.developersteam.configuration.DBProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DataSourceFactory {

    private final transient DBProperties dbProperties;

    @Autowired
    public DataSourceFactory(final DBProperties dbProperties) {
        this.dbProperties = dbProperties;
    }

    public DataSource get() {
            return new HikariDataSource(getHikariConfig());
    }

    private HikariConfig getHikariConfig() {

        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(dbProperties.url);
        hikariConfig.setUsername(dbProperties.username);
        hikariConfig.setPassword(dbProperties.password);
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return hikariConfig;
    }
}

