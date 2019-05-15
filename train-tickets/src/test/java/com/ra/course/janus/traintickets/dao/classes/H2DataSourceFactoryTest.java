package com.ra.course.janus.traintickets.dao.classes;

import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;



class H2DataSourceFactoryTest {

    @Test
    void getDataSource() throws SQLException {
        System.out.println("ccc");
        DataSource ds = new H2DataSourceFactory().getDataSource();
        Connection con = ds.getConnection();
        System.out.println(ds);
        System.out.println(con);

    }
}