package com.ra.janus.developersteam.datasources;

import com.ra.janus.developersteam.configuration.DBProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


public class DataSourceFactoryMockTest {


    @Test
    public void whenCreateDataSourceFactoryThenReturnsDataSourceFactoryInstance() throws Exception {

        //given
        DBProperties dbProperties = mock(DBProperties.class);

        //when
        DataSourceFactory dataSourceFactory = new DataSourceFactory(dbProperties);

        //then
        assertNotNull(dataSourceFactory);
    }

    @Test
    public void whenDBPropertiesAreNullThenReturnsHikariDataSource() throws Exception {

        //given
        DBProperties mockDbProperties = mock(DBProperties.class);
        DataSourceFactory dataSourceFactory = new DataSourceFactory(mockDbProperties);

        //when
        final Executable executable = () -> dataSourceFactory.get();

        //then
        assertThrows(IllegalArgumentException.class, executable);
    }
}
