package com.ra.course.janus.traintickets.configuration;

import org.junit.jupiter.api.Test;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertAll;

class DataSourceFactoryIntegrationTest {

    @Test
    public void dataSourceIntegrationTest() throws SQLException {
        assertAll(() -> {
            DataSource ds = DataSourceFactory.DATA_SOURCE.getInstance();
            Connection conn = ds.getConnection();
            conn.close();
        });
    }

}