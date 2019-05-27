package com.ra.janus.developersteam.schema;

import com.ra.janus.developersteam.datasources.DataSourceFactory;
import com.ra.janus.developersteam.datasources.DataSourceType;
import com.ra.janus.developersteam.utils.PropertyReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        Mockito.when(mockStatement.executeUpdate(Mockito.anyString())).thenThrow(new SQLException());

        //when
        final Executable executable = () ->  DBSchemaCreator.INSTANCE.createSchema(mockConnection);

        //then
        assertThrows(IllegalStateException.class, executable);
    }


    @Test
    void integrationTest() throws Exception{
        //given
        DataSource dataSource = new DataSourceFactory().get(DataSourceType.HIKARI);
        Connection connection = dataSource.getConnection();
        int notExpected = 0;

        //when
        int scriptsProcessed = DBSchemaCreator.INSTANCE.createSchema(connection);

//        DatabaseMetaData md = connection.getMetaData();
//        ResultSet rs = md.getTables(null, null, "%", null);
//        while (rs.next()) {
//            System.out.println(rs.getString(3));
//        }

        //then
        assertNotEquals(notExpected, scriptsProcessed);
    }
}