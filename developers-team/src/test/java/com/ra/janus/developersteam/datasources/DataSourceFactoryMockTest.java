package com.ra.janus.developersteam.datasources;

import com.ra.janus.developersteam.configuration.DBProperties;
import com.ra.janus.developersteam.utils.PropertyReaderUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class DataSourceFactoryMockTest {


//    @Test
//    public void whenAskHikariDataSourceFactoryReturnsThat() {
//
//        //given
//        DBProperties dbProperties = new DBProperties();
//
//        DataSourceFactory dataSourceFactory = new DataSourceFactory(dbProperties);
//
//        //when
//        when(dataSourceFactory.get()).thenReturn(new HikariDataSource());
//        DataSource dataSource = dataSourceFactory.get();
//
//        //then
//        assertEquals(true, dataSource instanceof DataSource);
//    }
}
