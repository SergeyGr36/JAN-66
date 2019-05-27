package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Manager;
import com.ra.janus.developersteam.exception.DAOException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;


import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class PlainJdbcManagerDAOTest {

    private static final String INSERT_SQL = "INSERT INTO managers (name, email, phone) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE managers SET name=?,email=?,phone=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM managers";
    private static final String SELECT_ONE_SQL = "SELECT * FROM managers WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM managers WHERE id=?";

    private DataSource mockDataSource;
    private BaseDao<Manager> managerDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void before() throws Exception {
        mockDataSource = Mockito.mock(DataSource.class);
        managerDAO = new PlainJdbcManagerDAO(mockDataSource);

        mockConnection = Mockito.mock(Connection.class);
        Mockito.when(mockDataSource.getConnection()).thenReturn(mockConnection);

        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    void whenCreateManagerShouldReturnManager() throws Exception {
        //given
        long testId = 1L;
        int columnIdIndex = 1;
        Manager testManager = new Manager(testId);
        Mockito.when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(true);
        Mockito.when(mockResultSet.getLong(columnIdIndex)).thenReturn(testId);

        //when
        Manager manager = managerDAO.create(testManager);

        //then
        assertEquals(testManager, manager);
    }

    @Test
    void whenCreateManagerShouldThrowExceptionIfIdWasNotGenerated() throws Exception {
        //given
        long testId = 1L;
        Manager testManager = new Manager(testId);
        Mockito.when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(false);

        //when
        final Executable executable = () -> managerDAO.create(testManager);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenCreateManagerShouldThrowException() throws Exception {
        //given
        long testId = 1L;
        Manager testManager = new Manager(testId);
        Mockito.when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> managerDAO.create(testManager);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenGetManagerFromDbByIdThenReturnIt() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(mockResultSet.getLong("id")).thenReturn(testId);

        //when
        Manager manager = managerDAO.get(testId);

        //then
        assertEquals(testId, manager.getId());
    }

    @Test
    void whenGetAbsentManagerFromDbByIdThenReturnNull() throws Exception {
        //given
        long testId = 1L;
        Manager expectedManager = null;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockResultSet.next()).thenReturn(false);

        //when
        Manager manager = managerDAO.get(testId);

        //then
        assertEquals(expectedManager, manager);
    }

    @Test
    void whenGetManagerFromDbByIdThenThrowExceptionOnGettingConnection() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockDataSource.getConnection()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> managerDAO.get(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenGetManagerFromDbByIdThenThrowExceptionOnPreparingStatement() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> managerDAO.get(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenGetManagerFromDbByIdThenThrowExceptionOnExecutingOfQuery() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> managerDAO.get(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenGetManagerFromDbByIdThenThrowExceptionOnIteratingOverResultSet() throws Exception {
        //given
        long testId = 1L;
        final int testParametherIndex = 1;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> managerDAO.get(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenGetAllManagersFromDbThenReturnNonEmptyList() throws Exception {
        //given
        Mockito.when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(false);

        //when
        List<Manager> list = managerDAO.getAll();

        //then
        assertFalse(list.isEmpty());
    }

    @Test
    void whenGetAllManagersFromDbThenThrowException() throws Exception {
        //given
        Mockito.when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> managerDAO.getAll();

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenUpdateManagerInDbThenReturnTrue() throws Exception {
        //given
        long testId = 1L;
        int testCount = 1;
        Manager testManager = new Manager(testId);
        Mockito.when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean updated = managerDAO.update(testManager);

        //then
        assertEquals(true, updated);
    }

    @Test
    void whenUpdateManagerInDbThenReturnFalse() throws Exception {
        //given
        long testId = 1L;
        int testCount = 0;
        Manager testManager = new Manager(testId);
        Mockito.when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean updated = managerDAO.update(testManager);

        //then
        assertEquals(false, updated);
    }

    @Test
    void whenUpdateManagerInDbThenThrowException() throws Exception {
        //given
        long testId = 1L;
        Manager testManager = new Manager(testId);
        Mockito.when(mockConnection.prepareStatement(UPDATE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> managerDAO.update(testManager);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenDeleteManagerFromDbThenReturnTrue()throws Exception  {
        //given
        long testId = 1L;
        int testCount = 1;
        Mockito.when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean deleted = managerDAO.delete(testId);

        //then
        assertEquals(true, deleted);
    }

    @Test
    void whenDeleteManagerFromDbThenReturnFalse()throws Exception  {
        //given
        long testId = 1L;
        int testCount = 0;
        Mockito.when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean deleted = managerDAO.delete(testId);

        //then
        assertEquals(false, deleted);
    }

    @Test
    void whenDeleteManagerFromDbThenThrowException()throws Exception  {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(DELETE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> managerDAO.delete(testId);

        //then
        assertThrows(DAOException.class, executable);
    }
}
