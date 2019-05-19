package com.ra.course.janus.traintickets.dao.datasources;

import org.junit.jupiter.api.Test;
import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertAll;

class DataSourceFactoryTest {

    @Test
    void h2InMemoryConnectionTest() throws SQLException {
        assertAll(() -> {
            DataSource ds = DataSourceFactory.H2_IN_MEMORY.getDataSource();
            ds.getConnection();
        });
    }

    @Test
    void hikaryH2InMemoryConnectionTest() throws SQLException {
        assertAll(() -> {
            DataSource ds = DataSourceFactory.HIKARY_H2_IN_MEMORY.getDataSource();
            ds.getConnection();
        });
    }

}