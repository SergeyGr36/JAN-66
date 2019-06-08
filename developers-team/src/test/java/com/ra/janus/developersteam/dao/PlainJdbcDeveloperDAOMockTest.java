package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Developer;
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

class PlainJdbcDeveloperDAOMockTest {
    private static final String INSERT_SQL = "INSERT INTO developers (name) VALUES (?)";
    private static final String UPDATE_SQL = "UPDATE developers SET name=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM developers";
    private static final String SELECT_ONE_SQL = "SELECT * FROM developers WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM developers WHERE id=?";
    private static final long TEST_ID = 1L;
    private static final Developer TEST_DEVELOPER = new Developer(TEST_ID, "John Doe");

    private DataSource mockDataSource = mock(DataSource.class);

    private PlainJdbcDeveloperDAO developerDAO;
    private Connection mockConnection = mock(Connection.class);
    private PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
    private ResultSet mockResultSet = mock(ResultSet.class);

    @BeforeEach
    void before() throws Exception {
        developerDAO = new PlainJdbcDeveloperDAO(mockDataSource);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockResultSet.next()).thenReturn(false);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
    }

    @Test
    void whenCreateDeveloperShouldReturnDeveloper() throws Exception {
        //given
        when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(TEST_ID);

        //when
        Developer developer = developerDAO.create(TEST_DEVELOPER);

        //then
        assertEquals(TEST_DEVELOPER, developer);
    }

    @Test
    void whenCreateDeveloperShouldThrowExceptionIfIdWasNotGenerated() throws Exception {
        //given
        when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);

        //when
        final Executable executable = () -> developerDAO.create(TEST_DEVELOPER);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenCreateDeveloperShouldThrowException() throws Exception {
        //given
        when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> developerDAO.create(TEST_DEVELOPER);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenReadDeveloperFromDbByIdThenReturnIt() throws Exception {
        //given
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getLong("id")).thenReturn(TEST_ID);

        //when
        Developer developer = developerDAO.get(TEST_ID);

        //then
        assertEquals(TEST_ID, developer.getId());
    }

    @Test
    void whenReadAbsentDeveloperFromDbByIdThenReturnNull() throws Exception {
        //given
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(false);

        //when
        Developer developer = developerDAO.get(TEST_ID);

        //then
        assertNull(developer);
    }

    @Test
    void whenReadDeveloperFromDbByIdThenThrowExceptionOnPreparingStatement() throws Exception {
        //given
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> developerDAO.get(TEST_ID);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenReadAllDevelopersFromDbThenReturnNonEmptyList() throws Exception {
        //given
        when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);

        //when
        List<Developer> list = developerDAO.getAll();

        //then
        assertFalse(list.isEmpty());
    }

    @Test
    void whenReadAllDevelopersFromDbThenThrowException() throws Exception {
        //given
        when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> developerDAO.getAll();

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenUpdateDeveloperInDbThenReturnTrue() throws Exception {
        //given
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        //when
        boolean updated = developerDAO.update(TEST_DEVELOPER);

        //then
        assertTrue(updated);
    }

    @Test
    void whenUpdateDeveloperInDbThenReturnFalse() throws Exception {
        //given
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        //when
        boolean updated = developerDAO.update(TEST_DEVELOPER);

        //then
        assertFalse(updated);
    }

    @Test
    void whenUpdateDeveloperInDbThenThrowException() throws Exception {
        //given
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> developerDAO.update(TEST_DEVELOPER);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenDeleteDeveloperFromDbThenReturnTrue() throws Exception {
        //given
        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        //when
        boolean deleted = developerDAO.delete(TEST_ID);

        //then
        assertTrue(deleted);
    }

    @Test
    void whenDeleteDeveloperFromDbThenReturnFalse() throws Exception {
        //given
        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        //when
        boolean deleted = developerDAO.delete(TEST_ID);

        //then
        assertFalse(deleted);
    }

    @Test
    void whenDeleteDeveloperFromDbThenThrowException() throws Exception {
        //given
        when(mockConnection.prepareStatement(DELETE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> developerDAO.delete(TEST_ID);

        //then
        assertThrows(DAOException.class, executable);
    }
}