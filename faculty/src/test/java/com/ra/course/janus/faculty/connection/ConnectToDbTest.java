package com.ra.course.janus.faculty.connection;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ConnectToDbTest {
    private DataSource dataSource;
    private HikariDataSource hikariDataSource;
    private ConnectToDb connectToDb;
    private Properties properties;
    private String fileName;

    @BeforeEach
    void before() {
        dataSource = mock(DataSource.class);
        hikariDataSource = mock(HikariDataSource.class);
        connectToDb = mock(ConnectToDb.class);
        properties = mock(Properties.class);
        fileName = "C:/Users/User/Desktop/projects/Java/Hillel/janus/faculty/src/test/resources/config.properties";
    }

    @Test
    void whenCalledLoadPropertiesAndStreamNotNullThenLoadProperties() {
        Properties testProp = mock(Properties.class);
        testProp.setProperty("db.url", "jdbc:h2:./test");
        testProp.setProperty("db.username", "root");
        testProp.setProperty("db.password", "root");

        assertEquals(testProp, connectToDb.loadProperties(fileName));
    }

    @Test
    void whenCalledConnectThenReturnDataSource() {

    }
}
