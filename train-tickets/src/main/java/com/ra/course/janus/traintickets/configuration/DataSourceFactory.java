package com.ra.course.janus.traintickets.configuration;

import com.ra.course.janus.traintickets.exception.ConfigException;
import com.zaxxer.hikari.HikariDataSource;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public enum DataSourceFactory {

    H2_IN_MEMORY {
        @Override
        protected DataSource createDataSource() {
            final JdbcDataSource ds = new JdbcDataSource();
            ds.setUrl(props.getProperty("db.h2_in_memory.url"));
            ds.setUser(props.getProperty("db.h2_in_memory.user"));
            ds.setPassword(props.getProperty("db.h2_in_memory.password"));
            return ds;
        }
    },

    HIKARY_H2_IN_MEMORY {
        @Override
        protected DataSource createDataSource() {
            final HikariDataSource ds = new HikariDataSource();
            ds.setJdbcUrl(props.getProperty("db.h2_in_memory.url"));
            ds.setUsername(props.getProperty("db.h2_in_memory.user"));
            ds.setPassword(props.getProperty("db.h2_in_memory.password"));
            return ds;
        }
    };

    protected static Properties props;
    static {
        props = new Properties();
        try {
            props.load(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("config.properties")));
        } catch (IOException e) {
            throw new ConfigException(e);
        }
    }

    protected DataSource dataSource;

    public DataSource getDataSource() {
        if (this.dataSource == null) {
            this.dataSource = createDataSource();
        }
        return this.dataSource;
    }

    abstract protected DataSource createDataSource();
}
