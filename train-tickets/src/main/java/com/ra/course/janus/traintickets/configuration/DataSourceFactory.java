package com.ra.course.janus.traintickets.configuration;

import com.ra.course.janus.traintickets.exception.ConfigException;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public enum DataSourceFactory {

    DATA_SOURCE {
        @Override
        protected DataSource createDataSource() {
            final HikariDataSource ds = new HikariDataSource();
            ds.setJdbcUrl(DB_PROPS.getProperty("db.url"));
            ds.setUsername(DB_PROPS.getProperty("db.user"));
            ds.setPassword(DB_PROPS.getProperty("db.password"));
            return ds;
        }
    };

    static final Properties DB_PROPS;
    static {
        DB_PROPS = new Properties();
        try {
            DB_PROPS.load(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("config.properties")));
        } catch (IOException e) {
            throw new ConfigException(e);
        }
    }

    protected DataSource dataSource;

    public DataSource getInstance() {
        if (this.dataSource == null) {
            this.dataSource = createDataSource();
        }
        return this.dataSource;
    }

    abstract protected DataSource createDataSource();
}
