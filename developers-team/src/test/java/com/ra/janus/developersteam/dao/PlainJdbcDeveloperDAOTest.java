package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Customer;
import com.ra.janus.developersteam.entity.Developer;
import com.ra.janus.developersteam.exception.DAOException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

class PlainJdbcDeveloperDAOTest {
    private static final String INSERT_SQL = "INSERT INTO developers (name) VALUES (?)";
    private static final String UPDATE_SQL = "UPDATE developers SET name=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM developers";
    private static final String SELECT_ONE_SQL = "SELECT * FROM developers WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM developers WHERE id=?";
    private DataSource mockDataSource;

    private PlainJdbcDeveloperDAO developerDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void before() throws Exception {
        mockDataSource = Mockito.mock(DataSource.class);
        developerDAO = new PlainJdbcDeveloperDAO(mockDataSource);

        mockConnection = Mockito.mock(Connection.class);
        Mockito.when(mockDataSource.getConnection()).thenReturn(mockConnection);

        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    void whenCreateDeveloperShouldReturnDeveloper() throws Exception {
        //given
        long testId = 1L;
        int columnIdIndex = 1;
        String testName = null;
        Developer testDeveloper = new Developer(testId, testName);
        Mockito.when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(true);
        Mockito.when(mockResultSet.getLong(columnIdIndex)).thenReturn(testId);

        //when
        Developer developer = developerDAO.create(testDeveloper);

        //then
        assertEquals(testDeveloper, developer);
    }

    @Test
    void whenCreateDeveloperShouldThrowExceptionIfIdWasNotGenerated() throws Exception {
        //given
        long testId = 1L;
        String testName = null;
        Developer testDeveloper = new Developer(testId, testName);
        Mockito.when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(false);

        //when
        final Executable executable = () -> developerDAO.create(testDeveloper);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenCreateDeveloperShouldThrowException() throws Exception {
        //given
        long testId = 1L;
        String testName = null;
        Developer testDeveloper = new Developer(testId, testName);
        Mockito.when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> developerDAO.create(testDeveloper);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenReadDeveloperFromDbByIdThenReturnIt() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(mockResultSet.getLong("id")).thenReturn(testId);

        //when
        Developer customer = developerDAO.get(testId);

        //then
        assertEquals(testId, customer.getId());
    }

    @Test
    void whenReadAbsentDeveloperFromDbByIdThenReturnNull() throws Exception {
        //given
        long testId = 1L;
        Developer expectedDeveloper = null;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockResultSet.next()).thenReturn(false);

        //when
        Developer customer = developerDAO.get(testId);

        //then
        assertEquals(expectedDeveloper, customer);
    }

    @Test
    void whenReadDeveloperFromDbByIdThenThrowExceptionOnGettingConnection() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockDataSource.getConnection()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> developerDAO.get(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenReadDeveloperFromDbByIdThenThrowExceptionOnPreparingStatement() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> developerDAO.get(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenReadDeveloperFromDbByIdThenThrowExceptionOnExecutingOfQuery() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> developerDAO.get(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenReadDeveloperFromDbByIdThenThrowExceptionOnIteratingOverResultSet() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> developerDAO.get(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenReadAllDevelopersFromDbThenReturnNonEmptyList() throws Exception {
        //given
        Mockito.when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(false);

        //when
        List<Developer> list = developerDAO.getAll();

        //then
        assertFalse(list.isEmpty());
    }

    @Test
    void whenReadAllDevelopersFromDbThenThrowException() throws Exception {
        //given
        Mockito.when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> developerDAO.getAll();

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenUpdateDeveloperInDbThenReturnTrue() throws Exception {
        //given
        long testId = 1L;
        int testCount = 1;
        String testName = null;
        Developer testDeveloper = new Developer(testId, testName);
        Mockito.when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean updated = developerDAO.update(testDeveloper);

        //then
        assertEquals(true, updated);
    }

    @Test
    void whenUpdateDeveloperInDbThenReturnFalse() throws Exception {
        //given
        long testId = 1L;
        int testCount = 0;
        String testName = null;
        Developer testDeveloper = new Developer(testId, testName);
        Mockito.when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean updated = developerDAO.update(testDeveloper);

        //then
        assertEquals(false, updated);
    }

    @Test
    void whenUpdateDeveloperInDbThenThrowException() throws Exception {
        //given
        long testId = 1L;
        String testName = null;
        Developer testDeveloper = new Developer(testId, testName);
        Mockito.when(mockConnection.prepareStatement(UPDATE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> developerDAO.update(testDeveloper);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenDeleteDeveloperFromDbThenReturnTrue()throws Exception  {
        //given
        long testId = 1L;
        int testCount = 1;
        Mockito.when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean deleted = developerDAO.delete(testId);

        //then
        assertEquals(true, deleted);
    }

    @Test
    void whenDeleteDeveloperFromDbThenReturnFalse()throws Exception  {
        //given
        long testId = 1L;
        int testCount = 0;
        Mockito.when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean deleted = developerDAO.delete(testId);

        //then
        assertEquals(false, deleted);
    }

    @Test
    void whenDeleteCustomerFromDbThenThrowException()throws Exception  {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(DELETE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> developerDAO.delete(testId);

        //then
        assertThrows(DAOException.class, executable);
    }
}