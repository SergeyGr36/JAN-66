package com.ra.course.janus.traintickets.dao.classes;

import com.ra.course.janus.traintickets.dao.interfaces.DataSourceFactory;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;

public class H2DataSourceFactory implements DataSourceFactory {

    @Override
    public DataSource getDataSource() {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        ds.setUser("sa");
        ds.setPassword("");
        return ds;
    }
}
