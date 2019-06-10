package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Qualification;
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

class PlainJdbcQualificationDAOMockTest {
    private static final String INSERT_SQL = "INSERT INTO qualifications (name, responsibility) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE qualifications SET name=?,responsibility=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM qualifications";
    private static final String SELECT_ONE_SQL = "SELECT * FROM qualifications WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM qualifications WHERE id=?";
    private static final long TEST_ID = 1L;
    private static final Qualification TEST_QUALIFICATION = new Qualification(TEST_ID, "Web Developer", "Front End");

    private DataSource mockDataSource = mock(DataSource.class);

    private PlainJdbcQualificationDAO qualificationDAO;
    private Connection mockConnection = mock(Connection.class);
    private PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
    private ResultSet mockResultSet = mock(ResultSet.class);

    @BeforeEach
    void before() throws Exception {
        qualificationDAO = new PlainJdbcQualificationDAO(mockDataSource);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockResultSet.next()).thenReturn(false);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
    }

    @Test
    void whenCreateQualificationShouldReturnQualification() throws Exception {
        //given
        when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(TEST_ID);

        //when
        Qualification qualification = qualificationDAO.create(TEST_QUALIFICATION);

        //then
        assertEquals(TEST_QUALIFICATION, qualification);
    }

    @Test
    void whenCreateQualificationShouldThrowExceptionIfIdWasNotGenerated() throws Exception {
        //given
        when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);

        //when
        final Executable executable = () -> qualificationDAO.create(TEST_QUALIFICATION);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenCreateQualificationShouldThrowException() throws Exception {
        //given
        when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> qualificationDAO.create(TEST_QUALIFICATION);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenReadQualificationFromDbByIdThenReturnIt() throws Exception {
        //given
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getLong("id")).thenReturn(TEST_ID);

        //when
        Qualification qualification = qualificationDAO.get(TEST_ID);

        //then
        assertEquals(TEST_ID, qualification.getId());
    }

    @Test
    void whenReadAbsentQualificationFromDbByIdThenReturnNull() throws Exception {
        //given
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(false);

        //when
        Qualification qualification = qualificationDAO.get(TEST_ID);

        //then
        assertNull(qualification);
    }

    @Test
    void whenReadQualificationFromDbByIdThenThrowExceptionOnPreparingStatement() throws Exception {
        //given
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> qualificationDAO.get(TEST_ID);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenReadAllQualificationsFromDbThenReturnNonEmptyList() throws Exception {
        //given
        when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);

        //when
        List<Qualification> list = qualificationDAO.getAll();

        //then
        assertFalse(list.isEmpty());
    }

    @Test
    void whenReadAllQualificationsFromDbThenThrowException() throws Exception {
        //given
        when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> qualificationDAO.getAll();

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenUpdateQualificationInDbThenReturnTrue() throws Exception {
        //given
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        //when
        boolean updated = qualificationDAO.update(TEST_QUALIFICATION);

        //then
        assertTrue(updated);
    }

    @Test
    void whenUpdateQualificationInDbThenReturnFalse() throws Exception {
        //given
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        //when
        boolean updated = qualificationDAO.update(TEST_QUALIFICATION);

        //then
        assertFalse(updated);
    }

    @Test
    void whenUpdateQualificationInDbThenThrowException() throws Exception {
        //given
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> qualificationDAO.update(TEST_QUALIFICATION);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenDeleteQualificationFromDbThenReturnTrue() throws Exception {
        //given
        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        //when
        boolean deleted = qualificationDAO.delete(TEST_ID);

        //then
        assertTrue(deleted);
    }

    @Test
    void whenDeleteQualificationFromDbThenReturnFalse() throws Exception {
        //given
        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        //when
        boolean deleted = qualificationDAO.delete(TEST_ID);

        //then
        assertFalse(deleted);
    }

    @Test
    void whenDeleteQualificationFromDbThenThrowException() throws Exception {
        //given
        when(mockConnection.prepareStatement(DELETE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> qualificationDAO.delete(TEST_ID);

        //then
        assertThrows(DAOException.class, executable);
    }
}