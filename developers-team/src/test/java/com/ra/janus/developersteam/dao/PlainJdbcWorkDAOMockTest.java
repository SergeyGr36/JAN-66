package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Work;
import com.ra.janus.developersteam.exception.DAOException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

class PlainJdbcWorkDAOMockTest {
    private static final String INSERT_SQL = "INSERT INTO works (name, price) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE works SET name=?,price=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM works";
    private static final String SELECT_ONE_SQL = "SELECT * FROM works WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM works WHERE id=?";
    private DataSource mockDataSource;

    private PlainJdbcWorkDAO workDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void before() throws Exception {
        mockDataSource = Mockito.mock(DataSource.class);
        workDAO = new PlainJdbcWorkDAO(mockDataSource);

        mockConnection = Mockito.mock(Connection.class);
        Mockito.when(mockDataSource.getConnection()).thenReturn(mockConnection);

        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    void whenCreateWorkShouldReturnWork() throws Exception {
        //given
        long testId = 1L;
        int columnIdIndex = 1;
        Work testCustomer = new Work(testId, null, null);
        Mockito.when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(true);
        Mockito.when(mockResultSet.getLong(columnIdIndex)).thenReturn(testId);

        //when
        Work work = workDAO.create(testCustomer);

        //then
        assertEquals(testCustomer, work);
    }

    @Test
    void whenCreateWorkShouldThrowExceptionIfIdWasNotGenerated() throws Exception {
        //given
        long testId = 1L;
        Work testWork = new Work(testId, null, null);
        Mockito.when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(false);

        //when
        final Executable executable = () -> workDAO.create(testWork);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenCreateWorkShouldThrowException() throws Exception {
        //given
        long testId = 1L;
        Work testWork = new Work(testId, null, null);
        Mockito.when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> workDAO.create(testWork);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenReadWorkFromDbByIdThenReturnIt() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(mockResultSet.getLong("id")).thenReturn(testId);

        //when
        Work work = workDAO.get(testId);

        //then
        assertEquals(testId, work.getId());
    }

    @Test
    void whenReadAbsentWorkFromDbByIdThenReturnNull() throws Exception {
        //given
        long testId = 1L;
        Work expectedCustomer = null;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockResultSet.next()).thenReturn(false);

        //when
        Work work = workDAO.get(testId);

        //then
        assertEquals(expectedCustomer, work);
    }

    @Test
    void whenReadWorkFromDbByIdThenThrowExceptionOnGettingConnection() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockDataSource.getConnection()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> workDAO.get(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenReadWorkFromDbByIdThenThrowExceptionOnPreparingStatement() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> workDAO.get(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenReadWorkFromDbByIdThenThrowExceptionOnExecutingOfQuery() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> workDAO.get(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenReadWorkFromDbByIdThenThrowExceptionOnIteratingOverResultSet() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> workDAO.get(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenReadAllWorksFromDbThenReturnNonEmptyList() throws Exception {
        //given
        Mockito.when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(false);

        //when
        List<Work> list = workDAO.getAll();

        //then
        assertFalse(list.isEmpty());
    }

    @Test
    void whenReadAllWorksFromDbThenThrowException() throws Exception {
        //given
        Mockito.when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> workDAO.getAll();

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenUpdateWorkInDbThenReturnTrue() throws Exception {
        //given
        long testId = 1L;
        int testCount = 1;
        Work testCustomer = new Work(testId, null,null);
        Mockito.when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean updated = workDAO.update(testCustomer);

        //then
        assertEquals(true, updated);
    }

    @Test
    void whenUpdateWorkInDbThenReturnFalse() throws Exception {
        //given
        long testId = 1L;
        int testCount = 0;
        Work testWork = new Work(testId, null, null);
        Mockito.when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean updated = workDAO.update(testWork);

        //then
        assertEquals(false, updated);
    }

    @Test
    void whenUpdateWorkInDbThenThrowException() throws Exception {
        //given
        long testId = 1L;
        Work testWork = new Work(testId, null, null);
        Mockito.when(mockConnection.prepareStatement(UPDATE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> workDAO.update(testWork);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenDeleteWorkFromDbThenReturnTrue()throws Exception  {
        //given
        long testId = 1L;
        int testCount = 1;
        Mockito.when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean deleted = workDAO.delete(testId);

        //then
        assertEquals(true, deleted);
    }

    @Test
    void whenDeleteWorkFromDbThenReturnFalse()throws Exception  {
        //given
        long testId = 1L;
        int testCount = 0;
        Mockito.when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean deleted = workDAO.delete(testId);

        //then
        assertEquals(false, deleted);
    }

    @Test
    void whenDeleteWorkFromDbThenThrowException()throws Exception  {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(DELETE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> workDAO.delete(testId);

        //then
        assertThrows(DAOException.class, executable);
    }
}