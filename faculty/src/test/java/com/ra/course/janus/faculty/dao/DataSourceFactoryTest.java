package com.ra.course.janus.faculty.dao;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DataSourceFactoryTest {

    @Test
    public void whenGetDataSourceFactory()  throws IOException {

        DataSource dataSource = DataSourceFactoryHelper.getDataSource();

        assertEquals(true, dataSource instanceof HikariDataSource);
    }
}