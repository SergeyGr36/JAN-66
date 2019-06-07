package com.ra.course.janus.faculty.connection;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ConnectToDbTest {
    @Test
    void whenGetDataSourceThenReturnNewDataSource() throws IOException {
        DataSource dataSource = ConnectToDb.getDataSource();
        assertTrue(dataSource instanceof HikariDataSource);
    }
}
