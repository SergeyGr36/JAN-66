package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Project;
import com.ra.janus.developersteam.entity.Qualification;
import com.ra.janus.developersteam.exception.DAOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcQualificationDaoTest {

    private static final String INSERT_SQL = "INSERT INTO qualifications (name, responsibility) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE qualifications SET name=?,responsibility=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM qualifications";
    private static final String SELECT_ONE_SQL = "SELECT * FROM qualifications WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM qualifications WHERE id=?";

    private DataSource mockDataSource;
    private BaseDao<Qualification> qualificationDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void before() throws Exception {
        mockDataSource = Mockito.mock(DataSource.class);
        qualificationDAO = new JdbcQualificationDao(mockDataSource);

        mockConnection = Mockito.mock(Connection.class);
        Mockito.when(mockDataSource.getConnection()).thenReturn(mockConnection);

        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    void whenCreateQualificationShouldReturnQualification() throws Exception {
        //given
        long testId = 1L;
        int columnIdIndex = 1;
        Qualification testQualification = new Qualification(testId);
        Mockito.when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(true);
        Mockito.when(mockResultSet.getLong(columnIdIndex)).thenReturn(testId);

        //when
        Qualification qualification = qualificationDAO.create(testQualification);

        //then
        assertEquals(testQualification, qualification);
    }

    @Test
    void whenCreateQualificationShouldThrowExceptionIfIdWasNotGenerated() throws Exception {
        //given
        long testId = 1L;
        Qualification testQualification = new Qualification(testId);
        Mockito.when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(false);

        //when
        final Executable executable = () -> qualificationDAO.create(testQualification);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenCreateQualificationShouldThrowException() throws Exception {
        //given
        long testId = 1L;
        Qualification testQualification = new Qualification(testId);
        Mockito.when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> qualificationDAO.create(testQualification);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenGetQualificationFromDbByIdThenReturnIt() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(mockResultSet.getLong("id")).thenReturn(testId);

        //when
        Qualification qualification = qualificationDAO.get(testId);

        //then
        assertEquals(testId, qualification.getId());
    }

    @Test
    void whenGetAbsentQualificationFromDbByIdThenReturnNull() throws Exception {
        //given
        long testId = 1L;
        Qualification expectedQualification = null;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockResultSet.next()).thenReturn(false);

        //when
        Qualification qualification = qualificationDAO.get(testId);

        //then
        assertEquals(expectedQualification, qualification);
    }

    @Test
    void whenGetQualificationFromDbByIdThenThrowExceptionOnGettingConnection() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockDataSource.getConnection()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> qualificationDAO.get(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenGetQualificationFromDbByIdThenThrowExceptionOnPreparingStatement() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> qualificationDAO.get(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenGetQualificationFromDbByIdThenThrowExceptionOnExecutingOfQuery() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> qualificationDAO.get(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenGetQualificationFromDbByIdThenThrowExceptionOnIteratingOverResultSet() throws Exception {
        //given
        long testId = 1L;
        final int testParametherIndex = 1;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> qualificationDAO.get(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenGetAllQualificationsFromDbThenReturnNonEmptyList() throws Exception {
        //given
        Mockito.when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(false);

        //when
        List<Qualification> list = qualificationDAO.getAll();

        //then
        assertFalse(list.isEmpty());
    }

    @Test
    void whenGetAllQualificationsFromDbThenThrowException() throws Exception {
        //given
        Mockito.when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> qualificationDAO.getAll();

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenUpdateQualificationInDbThenReturnTrue() throws Exception {
        //given
        long testId = 1L;
        int testCount = 1;
        Qualification testQualification = new Qualification(testId);
        Mockito.when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean updated = qualificationDAO.update(testQualification);

        //then
        assertEquals(true, updated);
    }

    @Test
    void whenUpdateQualificationInDbThenReturnFalse() throws Exception {
        //given
        long testId = 1L;
        int testCount = 0;
        Qualification testQualification = new Qualification(testId);
        Mockito.when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean updated = qualificationDAO.update(testQualification);

        //then
        assertEquals(false, updated);
    }

    @Test
    void whenUpdateQualificationInDbThenThrowException() throws Exception {
        //given
        long testId = 1L;
        Qualification testQualification = new Qualification(testId);
        Mockito.when(mockConnection.prepareStatement(UPDATE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> qualificationDAO.update(testQualification);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenDeleteQualificationFromDbThenReturnTrue()throws Exception  {
        //given
        long testId = 1L;
        int testCount = 1;
        Mockito.when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean deleted = qualificationDAO.delete(testId);

        //then
        assertEquals(true, deleted);
    }

    @Test
    void whenDeleteQualificationFromDbThenReturnFalse()throws Exception  {
        //given
        long testId = 1L;
        int testCount = 0;
        Mockito.when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean deleted = qualificationDAO.delete(testId);

        //then
        assertEquals(false, deleted);
    }

    @Test
    void whenDeleteQualificationFromDbThenThrowException()throws Exception  {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(DELETE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> qualificationDAO.delete(testId);

        //then
        assertThrows(DAOException.class, executable);
    }
}
