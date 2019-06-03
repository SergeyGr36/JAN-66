package com.ra.course.janus.faculty.dao;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DataSourceUtilsTest {

    @Test
    public void whenGetDataSource()  {

        DataSource dataSource = DataSourceUtils.getDataSource();

        assertEquals(true, dataSource instanceof HikariDataSource);
    }
}