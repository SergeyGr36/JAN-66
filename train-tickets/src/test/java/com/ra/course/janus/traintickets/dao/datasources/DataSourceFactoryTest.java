package com.ra.course.janus.traintickets.dao.datasources;

import com.ra.course.janus.traintickets.datasources.DataSourceFactory;
import org.junit.jupiter.api.Test;
import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertAll;

class DataSourceFactoryTest {

    @Test
    void getDataSource() throws SQLException {
        assertAll(() -> {
            DataSource ds = DataSourceFactory.H2_IN_MEMORY.getDataSource();
            ds.getConnection();
        });
    }
}