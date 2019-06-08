package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Project;
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

class PlainJdbcProjectDAOMockTest {
    private static final String INSERT_SQL = "INSERT INTO projects (name, description, status, eta) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE projects SET name=?,description=?,status=?,eta=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM projects";
    private static final String SELECT_ONE_SQL = "SELECT * FROM projects WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM projects WHERE id=?";
    private static final long TEST_ID = 1L;
    private static final Project TEST_PROJECT = new Project(TEST_ID);

    private DataSource mockDataSource = mock(DataSource.class);

    private PlainJdbcProjectDAO projectDAO;
    private Connection mockConnection = mock(Connection.class);
    private PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
    private ResultSet mockResultSet = mock(ResultSet.class);

    @BeforeEach
    void before() throws Exception {
        projectDAO = new PlainJdbcProjectDAO(mockDataSource);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockResultSet.next()).thenReturn(false);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
    }

    @Test
    void whenCreateProjectShouldReturnProject() throws Exception {
        //given
        when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(TEST_ID);

        //when
        Project project = projectDAO.create(TEST_PROJECT);

        //then
        assertEquals(TEST_PROJECT, project);
    }

    @Test
    void whenCreateProjectShouldThrowExceptionIfIdWasNotGenerated() throws Exception {
        //given
        when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);

        //when
        final Executable executable = () -> projectDAO.create(TEST_PROJECT);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenCreateProjectShouldThrowException() throws Exception {
        //given
        when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> projectDAO.create(TEST_PROJECT);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenReadProjectFromDbByIdThenReturnIt() throws Exception {
        //given
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getLong("id")).thenReturn(TEST_ID);

        //when
        Project project = projectDAO.get(TEST_ID);

        //then
        assertEquals(TEST_ID, project.getId());
    }

    @Test
    void whenReadAbsentProjectFromDbByIdThenReturnNull() throws Exception {
        //given
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(false);

        //when
        Project project = projectDAO.get(TEST_ID);

        //then
        assertNull(project);
    }

    @Test
    void whenReadProjectFromDbByIdThenThrowExceptionOnPreparingStatement() throws Exception {
        //given
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> projectDAO.get(TEST_ID);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenReadAllProjectsFromDbThenReturnNonEmptyList() throws Exception {
        //given
        when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);

        //when
        List<Project> list = projectDAO.getAll();

        //then
        assertFalse(list.isEmpty());
    }

    @Test
    void whenReadAllProjectsFromDbThenThrowException() throws Exception {
        //given
        when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> projectDAO.getAll();

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenUpdateProjectInDbThenReturnTrue() throws Exception {
        //given
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        //when
        boolean updated = projectDAO.update(TEST_PROJECT);

        //then
        assertTrue(updated);
    }

    @Test
    void whenUpdateProjectInDbThenReturnFalse() throws Exception {
        //given
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        //when
        boolean updated = projectDAO.update(TEST_PROJECT);

        //then
        assertFalse(updated);
    }

    @Test
    void whenUpdateProjectInDbThenThrowException() throws Exception {
        //given
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> projectDAO.update(TEST_PROJECT);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenDeleteProjectFromDbThenReturnTrue() throws Exception {
        //given
        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        //when
        boolean deleted = projectDAO.delete(TEST_ID);

        //then
        assertTrue(deleted);
    }

    @Test
    void whenDeleteProjectFromDbThenReturnFalse() throws Exception {
        //given
        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        //when
        boolean deleted = projectDAO.delete(TEST_ID);

        //then
        assertFalse(deleted);
    }

    @Test
    void whenDeleteProjectFromDbThenThrowException() throws Exception {
        //given
        when(mockConnection.prepareStatement(DELETE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> projectDAO.delete(TEST_ID);

        //then
        assertThrows(DAOException.class, executable);
    }
}