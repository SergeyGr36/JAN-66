package com.ra.course.janus.faculty.connect;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionUtilTest {
    @Test
    void whenGetDataSourceThenReturnNewDataSource() {
        DataSource dataSource = ConnectionUtil.getDataSource();
        assertTrue(dataSource instanceof HikariDataSource);
    }
}
