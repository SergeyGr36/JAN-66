package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.entity.Train;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainDAOTest {

    private final String INSERT_TRAIN = "INSERT into TRAINS (ID, NAME, SEATING, FREE_SEATS) values (?, ?, ?, ?)";
    private final String SELECT_TRAIN_ID = "SELECT ID, NAME, SEATING, FREE_SEATS FROM TRAINS WHERE id = ";
    private final String UPDATE_TRAIN = "UPDATE TRAINS SET NAME = ?, SEATING = ?, FREE_SEATS = ? WHERE ID = ?";
    private final String DELETE_TRAIN = "DELETE * FROM TRAINS WHERE id = ?";
    private final String SELECT_TRAIN_ALL = "SELECT ID, NAME, SEATING, FREE_SEATS FROM TRAINS";

    private static final long TRAIN_ID = 10L;
    private static final long TRAIN_TEST_ID = 2L;
    private static final String TRAIN_NAME = "Test Train Name";
    private static final int SEATING = 100;
    private static final int FREE_SEATS = 90;
    private static final Train TEST_TRAIN = new Train(TRAIN_ID,TRAIN_NAME,SEATING,FREE_SEATS);


    private Train train;
    private TrainDAO trainDAO;
    private Connection mockConn;
    private Statement mockStatement;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    void setup() throws SQLException {
        final DataSource mockDataSourse = mock(DataSource.class);
        trainDAO = new TrainDAO(mockDataSourse);
        mockConn = mock(Connection.class);
        mockStatement = mock(Statement.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        when(mockDataSourse.getConnection()).thenReturn(mockConn);
    }


    @Test
    void whenTheObjectSuccessfullySaved() throws SQLException {
        when(mockConn.prepareStatement(INSERT_TRAIN)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("ID")).thenReturn(TRAIN_ID);
        train = new Train(TRAIN_TEST_ID,TRAIN_NAME,SEATING,FREE_SEATS);
        train = trainDAO.save(train);
        assertNotSame(TEST_TRAIN,train);
    }

    @Test
    void ifThereIsAnExceptionInSave()throws SQLException{
        train = new Train(TRAIN_TEST_ID,TRAIN_NAME,SEATING,FREE_SEATS);
        when(mockConn.prepareStatement(INSERT_TRAIN)).thenReturn(mockPreparedStatement);
        doThrow(new SQLException()).when(mockPreparedStatement).executeUpdate();
        assertThrows(RuntimeException.class, ()-> trainDAO.save(train));
    }

    @Test
    void whenTheObjectWasSuccessfullyUpdated()throws SQLException {
        when(mockConn.prepareStatement(UPDATE_TRAIN)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        train = new Train(TRAIN_ID,TRAIN_NAME,SEATING,FREE_SEATS);
        assertTrue(trainDAO.update(TRAIN_ID,train));
    }

    @Test
    void whenItemWasNotSuccessfullyUpdated()throws SQLException{
        when(mockConn.prepareStatement(UPDATE_TRAIN)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        train = new Train(TRAIN_ID,TRAIN_NAME,SEATING,FREE_SEATS);
        assertFalse(trainDAO.update(TRAIN_ID,train));
    }

    @Test
    void ifThereIsAnExceptionInUpdate()throws SQLException{
        when(mockConn.prepareStatement(UPDATE_TRAIN)).thenReturn(mockPreparedStatement);
        doThrow(new SQLException()).when(mockPreparedStatement).executeUpdate();
        doThrow(new SQLException()).when(mockConn).close();
        train = new Train(TRAIN_ID,TRAIN_NAME,SEATING,FREE_SEATS);
        assertThrows(RuntimeException.class,()->trainDAO.update(TRAIN_ID,train));
    }

    @Test
    void whenTheObjectWasSuccessfullyDelete() throws SQLException{
        when(mockConn.prepareStatement(DELETE_TRAIN)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        assertTrue(trainDAO.delete(TRAIN_ID));
    }

    @Test
    void whenItemWasNotSuccessfullyDelete()throws SQLException{
        when(mockConn.prepareStatement(DELETE_TRAIN)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        assertFalse(trainDAO.delete(TRAIN_ID));
    }

    @Test
    void ifThereIsAnExceptionInDelete()throws SQLException{
        when(mockConn.prepareStatement(DELETE_TRAIN)).thenReturn(mockPreparedStatement);
        doThrow(new SQLException()).when(mockPreparedStatement).executeUpdate();
        doThrow(new SQLException()).when(mockConn).close();
        assertThrows(RuntimeException.class,()->trainDAO.delete(TRAIN_ID));
    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
    }
}