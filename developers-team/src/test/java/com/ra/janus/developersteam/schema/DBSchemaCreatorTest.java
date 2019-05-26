package com.ra.janus.developersteam.schema;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

class DBSchemaCreatorTest {
    private DataSource mockDataSource;
    private Connection mockConnection;

    @BeforeEach
    public void before() throws Exception {
        mockDataSource = Mockito.mock(DataSource.class);
        mockConnection = Mockito.mock(Connection.class);
        Mockito.when(mockDataSource.getConnection()).thenReturn(mockConnection);
        Statement mockStatement = Mockito.mock(Statement.class);
        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.executeUpdate(Mockito.anyString())).thenReturn(1);
    }
    @Test
    void whenCreateSchemaUsingDefaultScriptsDirectoryShouldCreateSchema() {
        //given
        int notExpected = 0;

        //when
        int scriptsProcessed = DBSchemaCreator.INSTANCE.createSchema(mockConnection);

        //then
        assertNotEquals(notExpected, scriptsProcessed);
    }
}