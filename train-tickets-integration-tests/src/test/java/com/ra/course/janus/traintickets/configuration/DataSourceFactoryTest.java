package com.ra.course.janus.traintickets.configuration;

import org.junit.jupiter.api.Test;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertAll;

class DataSourceFactoryTest {

    @Test
    void hikaryH2InMemoryConnectionTest() throws SQLException {
        assertAll(() -> {
            DataSource ds = DataSourceFactory.HIKARY_H2_IN_MEMORY.getDataSource();
            Connection conn = ds.getConnection();
            conn.close();
        });
    }

}