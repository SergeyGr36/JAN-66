package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Manager;
import com.ra.janus.developersteam.exception.DAOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlainJdbcManagerDAOMockTest {
    private static final String INSERT_SQL = "INSERT INTO managers (name, email, phone) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE managers SET name=?,email=?,phone=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM managers";
    private static final String SELECT_ONE_SQL = "SELECT * FROM managers WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM managers WHERE id=?";
    private static final long TEST_ID = 1L;
    private static final Manager TEST_MANAGER = new Manager(TEST_ID, "John", "box@mail.com", "911");

    private DataSource mockDataSource = mock(DataSource.class);

    private PlainJdbcManagerDAO managerDAO;
    private Connection mockConnection = mock(Connection.class);
    private PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
    private ResultSet mockResultSet = mock(ResultSet.class);

    @BeforeEach
    void before() throws Exception {
        managerDAO = new PlainJdbcManagerDAO(mockDataSource);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockResultSet.next()).thenReturn(false);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
    }

    @Test
    void whenCreateManagerShouldReturnManager() throws Exception {
        //given
        when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(TEST_ID);

        //when
        Manager manager = managerDAO.create(TEST_MANAGER);

        //then
        assertEquals(TEST_MANAGER, manager);
    }

    @Test
    void whenCreateManagerShouldThrowExceptionIfIdWasNotGenerated() throws Exception {
        //given
        when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);

        //when
        final Executable executable = () -> managerDAO.create(TEST_MANAGER);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenCreateManagerShouldThrowException() throws Exception {
        //given
        when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> managerDAO.create(TEST_MANAGER);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenReadManagerFromDbByIdThenReturnIt() throws Exception {
        //given
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getLong("id")).thenReturn(TEST_ID);

        //when
        Manager manager = managerDAO.get(TEST_ID);

        //then
        assertEquals(TEST_ID, manager.getId());
    }

    @Test
    void whenReadAbsentManagerFromDbByIdThenReturnNull() throws Exception {
        //given
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(false);

        //when
        Manager manager = managerDAO.get(TEST_ID);

        //then
        assertNull(manager);
    }

    @Test
    void whenReadManagerFromDbByIdThenThrowExceptionOnPreparingStatement() throws Exception {
        //given
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> managerDAO.get(TEST_ID);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenReadAllManagersFromDbThenReturnNonEmptyList() throws Exception {
        //given
        when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);

        //when
        List<Manager> list = managerDAO.getAll();

        //then
        assertFalse(list.isEmpty());
    }

    @Test
    void whenReadAllManagersFromDbThenThrowException() throws Exception {
        //given
        when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> managerDAO.getAll();

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenUpdateManagerInDbThenReturnTrue() throws Exception {
        //given
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        //when
        boolean updated = managerDAO.update(TEST_MANAGER);

        //then
        assertTrue(updated);
    }

    @Test
    void whenUpdateManagerInDbThenReturnFalse() throws Exception {
        //given
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        //when
        boolean updated = managerDAO.update(TEST_MANAGER);

        //then
        assertFalse(updated);
    }

    @Test
    void whenUpdateManagerInDbThenThrowException() throws Exception {
        //given
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> managerDAO.update(TEST_MANAGER);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenDeleteManagerFromDbThenReturnTrue() throws Exception {
        //given
        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        //when
        boolean deleted = managerDAO.delete(TEST_ID);

        //then
        assertTrue(deleted);
    }

    @Test
    void whenDeleteManagerFromDbThenReturnFalse() throws Exception {
        //given
        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        //when
        boolean deleted = managerDAO.delete(TEST_ID);

        //then
        assertFalse(deleted);
    }

    @Test
    void whenDeleteManagerFromDbThenThrowException() throws Exception {
        //given
        when(mockConnection.prepareStatement(DELETE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> managerDAO.delete(TEST_ID);

        //then
        assertThrows(DAOException.class, executable);
    }
}