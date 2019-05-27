package com.ra.janus.developersteam.schema;

import com.ra.janus.developersteam.datasources.DataSourceFactory;
import com.ra.janus.developersteam.datasources.DataSourceType;
import com.ra.janus.developersteam.utils.PropertyReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.Executable;

import javax.sql.DataSource;
import java.nio.file.InvalidPathException;
import java.sql.*;
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
    void whenCreateSchemaForAllFilesShouldCreateSchema() throws Exception {
        //given
        int notExpected = 0;
        Mockito.when(mockStatement.executeUpdate(Mockito.anyString())).thenReturn(1);

        //when
        int scriptsProcessed = DBSchemaCreator.INSTANCE.createSchema(mockConnection);

        //then
        assertNotEquals(notExpected, scriptsProcessed);
    }

    @Test
    void whenCreateSchemaForAllFilesShouldThrowException() throws Exception {
        //given
        int notExpected = 0;
        Mockito.when(mockStatement.executeUpdate(Mockito.anyString())).thenThrow(new SQLException());

        //when
        final Executable executable = () ->  DBSchemaCreator.INSTANCE.createSchema(mockConnection);

        //then
        assertThrows(IllegalStateException.class, executable);
    }

    @Test
    void whenCreateSchemaUsingExistingFileShouldCreateSchema() throws Exception {
        //given
        int notExpected = 0;
        Mockito.when(mockStatement.executeUpdate(Mockito.anyString())).thenReturn(1);

        //when
        int scriptsProcessed = DBSchemaCreator.INSTANCE.createSchema(mockConnection, "CREATE_TABLE_customer.sql");

        //then
        assertNotEquals(notExpected, scriptsProcessed);
    }

    @Test
    void whenCreateSchemaUsingNonExistingFileShouldThrowException() throws Exception {
        //when
        final Executable executable = () ->  DBSchemaCreator.INSTANCE.createSchema(mockConnection, "NonExistingFile");

        //then
        assertThrows(IllegalStateException.class, executable);
    }

    @Test
    void whenCreateSchemaUsingNonExistingDirectoryShouldThrowException() throws Exception {
        //given
        Properties properties = PropertyReader.INSTANCE.getProperties();
        String oldValue = properties.getProperty(DBSchemaCreator.PROP_KEY);
        properties.setProperty(DBSchemaCreator.PROP_KEY, "NonExistingDirectory");

        //when
        final Executable executable = () ->  DBSchemaCreator.INSTANCE.createSchema(mockConnection);

        //then
        assertThrows(IllegalStateException.class, executable);

        if (oldValue == null) {
            properties.remove(DBSchemaCreator.PROP_KEY);
        } else {
            properties.setProperty(DBSchemaCreator.PROP_KEY, oldValue);
        }
    }

    @Test
    void whenCreateSchemaUsingFileInsteadOfdirectoryShouldThrowException() throws Exception {
        //given
        Properties properties = PropertyReader.INSTANCE.getProperties();
        String oldValue = properties.getProperty(DBSchemaCreator.PROP_KEY);
        properties.setProperty(DBSchemaCreator.PROP_KEY, "config.properties");

        //when
        final Executable executable = () -> DBSchemaCreator.INSTANCE.createSchema(mockConnection);

        //then
        assertThrows(IllegalStateException.class, executable);

        if (oldValue == null) {
            properties.remove(DBSchemaCreator.PROP_KEY);
        } else {
            properties.setProperty(DBSchemaCreator.PROP_KEY, oldValue);
        }
    }

    @Test
    void integrationTest() throws Exception{
        //given
        DataSource dataSource = new DataSourceFactory().get(DataSourceType.HIKARI);
        Connection connection = dataSource.getConnection();
        int notExpected = 0;

        //when
        int scriptsProcessed = DBSchemaCreator.INSTANCE.createSchema(connection);

        DatabaseMetaData md = connection.getMetaData();
        ResultSet rs = md.getTables(null, null, "%", null);
        while (rs.next()) {
            System.out.println(rs.getString(3));
        }

        //then
        assertNotEquals(notExpected, scriptsProcessed);
    }
}