package com.ra.course.janus.traintickets.entity;
import org.h2.jdbcx.JdbcDataSource;
import javax.sql.DataSource;

public enum DataSourceFactory {

    H2_IN_MEMORY {
        @Override
        protected DataSource createDataSource() {
            JdbcDataSource ds = new JdbcDataSource();
            ds.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
            ds.setUser("sa");
            ds.setPassword("");
            return ds;
        }
    };

    final protected DataSource dataSource;

    DataSourceFactory() {
        this.dataSource = createDataSource();
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    abstract protected DataSource createDataSource();
}
