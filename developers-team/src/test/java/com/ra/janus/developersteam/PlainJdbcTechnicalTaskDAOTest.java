package com.ra.janus.developersteam;

import com.ra.janus.developersteam.dao.PlainJdbcTechnicalTaskDAO;
import com.ra.janus.developersteam.entity.TechnicalTask;
import com.ra.janus.developersteam.exception.DAOException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

class PlainJdbcTechnicalTaskDAOTest {
    private static final String INSERT_SQL = "INSERT INTO tasks (title, description) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE tasks SET title=?,description=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM tasks";
    private static final String SELECT_ONE_SQL = "SELECT * FROM tasks WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM tasks WHERE id=?";

    private DataSource mockDataSource;

    private PlainJdbcTechnicalTaskDAO taskDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void before() throws Exception {
        mockDataSource = Mockito.mock(DataSource.class);
        taskDAO = new PlainJdbcTechnicalTaskDAO(mockDataSource);

        mockConnection = Mockito.mock(Connection.class);
        Mockito.when(mockDataSource.getConnection()).thenReturn(mockConnection);

        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    void whenCreateTaskShouldReturnTask() throws Exception {
        //given
        long testId = 1L;
        int columnIdIndex = 1;
        TechnicalTask testTask = new TechnicalTask(testId, null,null);
        Mockito.when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(true);
        Mockito.when(mockResultSet.getLong(columnIdIndex)).thenReturn(testId);

        //when
        TechnicalTask task = taskDAO.create(testTask);

        //then
        assertEquals(testTask, task);
    }

    @Test
    void whenCreateTaskShouldThrowExceptionIfIdWasNotGenerated() throws Exception {
        //given
        long testId = 1L;
        TechnicalTask testTask = new TechnicalTask(testId, null,null);
        Mockito.when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(false);

        //when
        final Executable executable = () -> taskDAO.create(testTask);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenCreateTaskShouldThrowException() throws Exception {
        //given
        long testId = 1L;
        TechnicalTask testTask = new TechnicalTask(testId, null, null);
        Mockito.when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> taskDAO.create(testTask);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenReadTaskFromDbByIdThenReturnIt() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(mockResultSet.getLong("id")).thenReturn(testId);

        //when
        TechnicalTask task = taskDAO.read(testId);

        //then
        assertEquals(testId, task.getId());
    }

    @Test
    void whenReadAbsentTaskFromDbByIdThenReturnNull() throws Exception {
        //given
        long testId = 1L;
        TechnicalTask expectedTask = null;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockResultSet.next()).thenReturn(false);

        //when
        TechnicalTask task = taskDAO.read(testId);

        //then
        assertEquals(expectedTask, task);
    }

    @Test
    void whenReadTaskFromDbByIdThenThrowExceptionOnGettingConnection() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockDataSource.getConnection()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> taskDAO.read(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenReadTaskFromDbByIdThenThrowExceptionOnPreparingStatement() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> taskDAO.read(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenReadTaskFromDbByIdThenThrowExceptionOnExecutingOfQuery() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> taskDAO.read(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenReadTaskFromDbByIdThenThrowExceptionOnIteratingOverResultSet() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> taskDAO.read(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenReadAllTasksFromDbThenReturnNonEmptyList() throws Exception {
        //given
        Mockito.when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(false);

        //when
        List<TechnicalTask> list = taskDAO.readAll();

        //then
        assertFalse(list.isEmpty());
    }

    @Test
    void whenReadAllTasksFromDbThenThrowException() throws Exception {
        //given
        Mockito.when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> taskDAO.readAll();

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenUpdateTaskInDbThenReturnTrue() throws Exception {
        //given
        long testId = 1L;
        int testCount = 1;
        TechnicalTask task = new TechnicalTask(testId, null, null);
        Mockito.when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean updated = taskDAO.update(task);

        //then
        assertEquals(true, updated);
    }

    @Test
    void whenUpdateTaskInDbThenReturnFalse() throws Exception {
        //given
        long testId = 1L;
        int testCount = 0;
        TechnicalTask testCustomer = new TechnicalTask(testId, null, null);
        Mockito.when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean updated = taskDAO.update(testCustomer);

        //then
        assertEquals(false, updated);
    }

    @Test
    void whenUpdateTaskInDbThenThrowException() throws Exception {
        //given
        long testId = 1L;
        TechnicalTask testTask = new TechnicalTask(testId, null, null);
        Mockito.when(mockConnection.prepareStatement(UPDATE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> taskDAO.update(testTask);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenDeleteTaskFromDbThenReturnTrue()throws Exception  {
        //given
        long testId = 1L;
        int testCount = 1;
        Mockito.when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean deleted = taskDAO.delete(testId);

        //then
        assertEquals(true, deleted);
    }

    @Test
    void whenDeleteTaskFromDbThenReturnFalse()throws Exception  {
        //given
        long testId = 1L;
        int testCount = 0;
        Mockito.when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean deleted = taskDAO.delete(testId);

        //then
        assertEquals(false, deleted);
    }

    @Test
    void whenDeleteTaskFromDbThenThrowException()throws Exception  {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(DELETE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> taskDAO.delete(testId);

        //then
        assertThrows(DAOException.class, executable);
    }
}