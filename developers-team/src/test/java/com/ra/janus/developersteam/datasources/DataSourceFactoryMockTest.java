package com.ra.janus.developersteam.datasources;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class DataSourceFactoryMockTest {

    DataSource mockDataSource;
    DataSourceFactory mockDataSourceFactory;

    @Test
    public void whenAskHikariDataSourceFactoryReturnsThat() {

        DataSource dataSource = DataSourceFactory.get();

        assertEquals(true, dataSource instanceof HikariDataSource);
    }
}
