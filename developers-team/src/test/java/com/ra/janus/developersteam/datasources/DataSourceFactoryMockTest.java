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

//    @BeforeEach
//    public void before() throws IOException {
//
//        mockDataSourceFactory = Mockito.mock(DataSourceFactory.class);
//        mockDataSource = Mockito.mock(DataSource.class);
//    }

    @Test
    public void whenAskHikariDataSourceFactoryReturnsThat() {

        DataSource dataSource = DataSourceFactory.get();

        assertEquals(true, dataSource instanceof HikariDataSource);
    }

//    @Test
//
//    public void whenAskHikariDataSourceFactoryThrowException() {
//
//        Mockito.when(mockDataSourceFactory.get()).thenThrow(new DataSourceException());
//
//        final Executable executable = () ->  mockDataSourceFactory.get();
//
//        assertThrows(DataSourceException.class, executable);
//    }
}
