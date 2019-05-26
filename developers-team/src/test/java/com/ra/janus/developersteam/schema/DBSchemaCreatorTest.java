package com.ra.janus.developersteam.schema;

import com.ra.janus.developersteam.utils.PropertyReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.Executable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

class DBSchemaCreatorTest {
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
    void whenCreateSchemaUsingDefaultScriptsDirectoryShouldCreateSchema() throws Exception {
        //given
        int notExpected = 0;
        Mockito.when(mockStatement.executeUpdate(Mockito.anyString())).thenReturn(1);

        //when
        int scriptsProcessed = DBSchemaCreator.INSTANCE.createSchema(mockConnection);

        //then
        assertNotEquals(notExpected, scriptsProcessed);
    }

    @Test
    void whenCreateSchemaUsingSetScriptsDirectoryShouldCreateSchema() throws Exception {
        //given
        int notExpected = 0;
        Properties properties = PropertyReader.INSTANCE.getProperties();
        String oldValue = properties.getProperty(DBSchemaCreator.PROP_KEY);
        properties.setProperty(DBSchemaCreator.PROP_KEY, DBSchemaCreator.DEFAULT_DIRS);
        Mockito.when(mockStatement.executeUpdate(Mockito.anyString())).thenReturn(1);

        //when
        int scriptsProcessed = DBSchemaCreator.INSTANCE.createSchema(mockConnection);
        if (oldValue == null) {
            properties.remove(DBSchemaCreator.PROP_KEY);
        }{
            properties.setProperty(DBSchemaCreator.PROP_KEY, oldValue);
        }

        //then
        assertNotEquals(notExpected, scriptsProcessed);
    }

    @Test
    void whenCreateSchemaUsingDefaultScriptsDirectoryShouldThrowException() throws Exception {
        //given
        int notExpected = 0;
        Mockito.when(mockStatement.executeUpdate(Mockito.anyString())).thenThrow(new SQLException());

        //when
        final Executable executable = () ->  DBSchemaCreator.INSTANCE.createSchema(mockConnection);

        //then
        assertThrows(IllegalStateException.class, executable);
    }

}