package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.entity.Train;
import com.ra.course.janus.traintickets.exception.DAOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainJdbcDaoMockTest {

    private static final String INSERT_TRAIN = "INSERT into TRAINS (NAME, SEATING, FREE_SEATS) values (?, ?, ?)";
    private static final String SELECT_TRAIN_ID = "SELECT ID, NAME, SEATING, FREE_SEATS FROM TRAINS WHERE ID = ?";
    private static final String UPDATE_TRAIN = "UPDATE TRAINS SET NAME = ?, SEATING = ?, FREE_SEATS = ? WHERE ID = ?";
    private static final String DELETE_TRAIN = "DELETE FROM TRAINS WHERE ID = ?";
    private static final String SELECT_TRAIN_ALL = "SELECT ID, NAME, SEATING, FREE_SEATS FROM TRAINS";
    private static final long TRAIN_ID = 10L;
    private static final long TRAIN_TEST_ID = 2L;
    private static final String TRAIN_NAME = "Test Train Name";
    private static final int SEATING = 100;
    private static final int FREE_SEATS = 90;
    private static final Train TEST_TRAIN = new Train(TRAIN_ID,TRAIN_NAME,SEATING,FREE_SEATS);

    private Train train;
    private TrainJdbcDao trainDAO;

    private DataSource mockDataSourse = mock(DataSource.class);
    private Connection mockConn = mock(Connection.class);
    private PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
    private ResultSet mockResultSet = mock(ResultSet.class);

    @BeforeEach
    public void trainTestsInit() throws SQLException {
        trainDAO = new TrainJdbcDao(mockDataSourse);
        when(mockDataSourse.getConnection()).thenReturn(mockConn);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    public void whenTheObjectSuccessfullySaved() throws SQLException {
        when(mockConn.prepareStatement(INSERT_TRAIN)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(TRAIN_ID);

        train = new Train(TRAIN_TEST_ID,TRAIN_NAME,SEATING,FREE_SEATS);
        train = trainDAO.save(train);
        assertNotSame(TEST_TRAIN,train);
    }

    @Test
    public void whenResultSetWithoutMeaningAndAppearsDAOException()throws SQLException{
        when(mockConn.prepareStatement(INSERT_TRAIN)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(false);
        assertThrows(DAOException.class,()->trainDAO.save(TEST_TRAIN));
    }

    @Test
    public void ifThereIsAnExceptionInSave()throws SQLException{
        train = new Train(TRAIN_TEST_ID,TRAIN_NAME,SEATING,FREE_SEATS);
        when(mockConn.prepareStatement(INSERT_TRAIN)).thenReturn(mockPreparedStatement);

        doThrow(new SQLException()).when(mockPreparedStatement).executeUpdate();
        assertThrows(DAOException.class,()->trainDAO.save(train));
    }

    @Test
    public void whenTheObjectWasSuccessfullyUpdated()throws SQLException {
        when(mockConn.prepareStatement(UPDATE_TRAIN)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        train = new Train(TRAIN_ID,TRAIN_NAME,SEATING,FREE_SEATS);

        assertTrue(trainDAO.update(train));
    }

    @Test
    public void whenItemWasNotSuccessfullyUpdated()throws SQLException{
        when(mockConn.prepareStatement(UPDATE_TRAIN)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        train = new Train(TRAIN_ID,TRAIN_NAME,SEATING,FREE_SEATS);
        assertFalse(trainDAO.update(train));
    }

    @Test
    public void ifThereIsAnExceptionInUpdate()throws SQLException{
        when(mockConn.prepareStatement(UPDATE_TRAIN)).thenReturn(mockPreparedStatement);

        doThrow(new SQLException()).when(mockPreparedStatement).executeUpdate();
        doThrow(new SQLException()).when(mockConn).close();

        train = new Train(TRAIN_ID,TRAIN_NAME,SEATING,FREE_SEATS);
        assertThrows(DAOException.class,()->trainDAO.update(train));
    }

    @Test
    public void whenTheObjectWasSuccessfullyDelete() throws SQLException{
        when(mockConn.prepareStatement(DELETE_TRAIN)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        assertTrue(trainDAO.delete(TRAIN_ID));
    }

    @Test
    public void whenItemWasNotSuccessfullyDelete()throws SQLException{
        when(mockConn.prepareStatement(DELETE_TRAIN)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        assertFalse(trainDAO.delete(TRAIN_ID));
    }

    @Test
    public void ifThereIsAnExceptionInDelete()throws SQLException{
        when(mockConn.prepareStatement(DELETE_TRAIN)).thenReturn(mockPreparedStatement);

        doThrow(new SQLException()).when(mockPreparedStatement).executeUpdate();
        doThrow(new SQLException()).when(mockConn).close();

        assertThrows(DAOException.class,()->trainDAO.delete(TRAIN_ID));
    }

    @Test
    public void whenItemWasSuccessfullySelect()throws SQLException {
        when(mockConn.prepareStatement(SELECT_TRAIN_ID)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true);
        mockMapTrain(mockResultSet);

        train = trainDAO.findById(TRAIN_ID);

        assertEquals(TEST_TRAIN, train);
    }

    @Test
    public void whenItemWasNotSuccessfullySelect()throws SQLException{
        when(mockConn.prepareStatement(SELECT_TRAIN_ID)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(false);
        mockMapTrain(mockResultSet);

        doThrow(new SQLException()).when(mockConn).close();

        assertThrows(DAOException.class, () -> trainDAO.findById(TRAIN_ID));
    }

    @Test
    public void ifThereIsAnExceptionInFindById()throws SQLException{
        when(mockConn.prepareStatement(SELECT_TRAIN_ID)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true);
        mockMapTrain(mockResultSet);

        doThrow(new SQLException()).when(mockConn).close();

        assertThrows(DAOException.class, () -> trainDAO.findById(TRAIN_ID));
    }

    @Test
    public void whenItemWasSuccessfullySelectFindAll() throws SQLException {
        when(mockConn.prepareStatement(SELECT_TRAIN_ALL)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        mockMapTrain(mockResultSet);

        List<Train> trainList = trainDAO.findAll();

        assertTrue(trainList.size() == 1);
    }

    @Test
    public void whenItemWasNotSuccessfullySelectFindAll() throws SQLException{
        when(mockConn.prepareStatement(SELECT_TRAIN_ALL)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(false);
        mockMapTrain(mockResultSet);

        List<Train> trainList = trainDAO.findAll();

        assertTrue(trainList.size() == 0);
    }

    @Test
    public void ifThereIsAnExceptionInFindAll()throws SQLException{
        when(mockConn.prepareStatement(SELECT_TRAIN_ALL)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);

        doThrow(new SQLException()).when(mockConn).close();

        assertThrows(DAOException.class, () -> trainDAO.findAll());
    }

    private void mockMapTrain(ResultSet mockRS) throws SQLException {
        when(mockRS.getLong(1)).thenReturn(TRAIN_ID);
        when(mockRS.getString(2)).thenReturn(TRAIN_NAME);
        when(mockRS.getInt(3)).thenReturn(SEATING);
        when(mockRS.getInt(4)).thenReturn(FREE_SEATS);
    }
}