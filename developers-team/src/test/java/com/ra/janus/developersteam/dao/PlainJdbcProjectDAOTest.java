package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Project;
import com.ra.janus.developersteam.exception.DAOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlainJdbcProjectDAOTest {

    private static final String INSERT_SQL = "INSERT INTO projects (name, description, status, eta) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE projects SET name=?,description=?,status=?,eta=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM projects";
    private static final String SELECT_ONE_SQL = "SELECT * FROM projects WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM projects WHERE id=?";

    private DataSource mockDataSource;
    private BaseDao<Project> projectDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void before() throws Exception {
        mockDataSource = Mockito.mock(DataSource.class);
        projectDAO = new PlainJdbcProjectDAO(mockDataSource);

        mockConnection = Mockito.mock(Connection.class);
        Mockito.when(mockDataSource.getConnection()).thenReturn(mockConnection);

        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    void whenCreateProjectShouldReturnProject() throws Exception {
        //given
        long testId = 1L;
        int columnIdIndex = 1;
        Project testProject = new Project(testId);
        Mockito.when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(true);
        Mockito.when(mockResultSet.getLong(columnIdIndex)).thenReturn(testId);

        //when
        Project project = projectDAO.create(testProject);

        //then
        assertEquals(testProject, project);
    }

    @Test
    void whenCreateProjectShouldThrowExceptionIfIdWasNotGenerated() throws Exception {
        //given
        long testId = 1L;
        Project testProject = new Project(testId);
        Mockito.when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(false);

        //when
        final Executable executable = () -> projectDAO.create(testProject);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenCreateProjectShouldThrowException() throws Exception {
        //given
        long testId = 1L;
        Project testProject = new Project(testId);
        Mockito.when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> projectDAO.create(testProject);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenGetProjectFromDbByIdThenReturnIt() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(mockResultSet.getLong("id")).thenReturn(testId);

        //when
        Project project = projectDAO.get(testId);

        //then
        assertEquals(testId, project.getId());
    }

    @Test
    void whenGetAbsentProjectFromDbByIdThenReturnNull() throws Exception {
        //given
        long testId = 1L;
        Project expectedProject = null;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockResultSet.next()).thenReturn(false);

        //when
        Project project = projectDAO.get(testId);

        //then
        assertEquals(expectedProject, project);
    }

    @Test
    void whenGetProjectFromDbByIdThenThrowExceptionOnGettingConnection() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockDataSource.getConnection()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> projectDAO.get(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenGetProjectFromDbByIdThenThrowExceptionOnPreparingStatement() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> projectDAO.get(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenGetProjectFromDbByIdThenThrowExceptionOnExecutingOfQuery() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> projectDAO.get(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenGetProjectFromDbByIdThenThrowExceptionOnIteratingOverResultSet() throws Exception {
        //given
        long testId = 1L;
        final int testParametherIndex = 1;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> projectDAO.get(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenGetAllProjectsFromDbThenReturnNonEmptyList() throws Exception {
        //given
        Mockito.when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(false);

        //when
        List<Project> list = projectDAO.getAll();

        //then
        assertFalse(list.isEmpty());
    }

    @Test
    void whenGetAllProjectsFromDbThenThrowException() throws Exception {
        //given
        Mockito.when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> projectDAO.getAll();

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenUpdateProjectInDbThenReturnTrue() throws Exception {
        //given
        long testId = 1L;
        int testCount = 1;
        Project testProject = new Project(testId);
        Mockito.when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean updated = projectDAO.update(testProject);

        //then
        assertEquals(true, updated);
    }

    @Test
    void whenUpdateProjectInDbThenReturnFalse() throws Exception {
        //given
        long testId = 1L;
        int testCount = 0;
        Project testProject = new Project(testId);
        Mockito.when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean updated = projectDAO.update(testProject);

        //then
        assertEquals(false, updated);
    }

    @Test
    void whenUpdateProjectInDbThenThrowException() throws Exception {
        //given
        long testId = 1L;
        Project testProject = new Project(testId);
        Mockito.when(mockConnection.prepareStatement(UPDATE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> projectDAO.update(testProject);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenDeleteProjectFromDbThenReturnTrue()throws Exception  {
        //given
        long testId = 1L;
        int testCount = 1;
        Mockito.when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean deleted = projectDAO.delete(testId);

        //then
        assertEquals(true, deleted);
    }

    @Test
    void whenDeleteProjectFromDbThenReturnFalse()throws Exception  {
        //given
        long testId = 1L;
        int testCount = 0;
        Mockito.when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean deleted = projectDAO.delete(testId);

        //then
        assertEquals(false, deleted);
    }

    @Test
    void whenDeleteProjectFromDbThenThrowException()throws Exception  {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(DELETE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> projectDAO.delete(testId);

        //then
        assertThrows(DAOException.class, executable);
    }
}
