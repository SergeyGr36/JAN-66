package com.ra.janus.developersteam.schema;

import com.ra.janus.developersteam.datasources.DataSourceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DBSchemaCreatorMockTest {
    private DataSource mockDataSource;
    private Connection mockConnection;
    private Statement mockStatement;

    @BeforeEach
    public void before() throws Exception {
        mockDataSource = Mockito.mock(DataSource.class);
        mockConnection = Mockito.mock(Connection.class);
        Mockito.when(mockDataSource.getConnection()).thenReturn(mockConnection);
        mockStatement = Mockito.mock(Statement.class);
        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
    }

    @Test
    void whenCreateSchemaForAllFilesShouldCreateSchema() throws Exception {
        //given
        int notExpected = 0;
        Mockito.when(mockStatement.executeUpdate(Mockito.anyString())).thenReturn(1);

        //when
        int scriptsProcessed = DBSchemaCreator.createSchema(mockConnection);

        //then
        assertNotEquals(notExpected, scriptsProcessed);
    }

    @Test
    void whenCreateSchemaForAllFilesShouldThrowException() throws Exception {
        //given
        Mockito.when(mockStatement.executeUpdate(Mockito.anyString())).thenThrow(new SQLException());

        //when
        final Executable executable = () ->  DBSchemaCreator.createSchema(mockConnection);

        //then
        assertThrows(IllegalStateException.class, executable);
    }
}