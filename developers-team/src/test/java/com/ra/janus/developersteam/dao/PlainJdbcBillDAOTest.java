package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Bill;
import com.ra.janus.developersteam.exception.DAOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlainJdbcBillDAOTest {
    private static final String INSERT_SQL = "INSERT INTO bills (docDate) VALUES (?)";
    private static final String UPDATE_SQL = "UPDATE bills SET docDate=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM bills";
    private static final String SELECT_ONE_SQL = "SELECT * FROM bills WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM bills WHERE id=?";
    private DataSource mockDataSource;

    private PlainJdbcBillDAO billDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void before() throws Exception {
        mockDataSource = Mockito.mock(DataSource.class);
        billDAO = new PlainJdbcBillDAO(mockDataSource);

        mockConnection = Mockito.mock(Connection.class);
        Mockito.when(mockDataSource.getConnection()).thenReturn(mockConnection);

        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    void whenCreateBillShouldReturnBill() throws Exception {
        //given
        long testId = 1L;
        int columnIdIndex = 1;
        Date testDate = null;
        Bill testBill = new Bill(testId, testDate);
        Mockito.when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(true);
        Mockito.when(mockResultSet.getLong(columnIdIndex)).thenReturn(testId);

        //when
        Bill bill = billDAO.create(testBill);

        //then
        assertEquals(testBill, bill);
    }

    @Test
    void whenCreateBillShouldThrowExceptionIfIdWasNotGenerated() throws Exception {
        //given
        long testId = 1L;
        Date testDate = null;
        Bill testBill = new Bill(testId, testDate);
        Mockito.when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(false);

        //when
        final Executable executable = () -> billDAO.create(testBill);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenCreateBillShouldThrowException() throws Exception {
        //given
        long testId = 1L;
        Date testDate = null;
        Bill testBill = new Bill(testId, testDate);
        Mockito.when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> billDAO.create(testBill);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenReadBillFromDbByIdThenReturnIt() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(mockResultSet.getLong("id")).thenReturn(testId);

        //when
        Bill bill = billDAO.read(testId);

        //then
        assertEquals(testId, bill.getId());
    }

    @Test
    void whenReadAbsentBillFromDbByIdThenReturnNull() throws Exception {
        //given
        long testId = 1L;
        Bill expectedBill = null;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockResultSet.next()).thenReturn(false);

        //when
        Bill bill = billDAO.read(testId);

        //then
        assertEquals(expectedBill, bill);
    }

    @Test
    void whenReadBillFromDbByIdThenThrowExceptionOnGettingConnection() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockDataSource.getConnection()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> billDAO.read(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenReadBillFromDbByIdThenThrowExceptionOnPreparingStatement() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> billDAO.read(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenReadBillFromDbByIdThenThrowExceptionOnExecutingOfQuery() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> billDAO.read(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenReadBillFromDbByIdThenThrowExceptionOnIteratingOverResultSet() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> billDAO.read(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenReadAllBillsFromDbThenReturnNonEmptyList() throws Exception {
        //given
        Mockito.when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(false);

        //when
        List<Bill> list = billDAO.readAll();

        //then
        assertFalse(list.isEmpty());
    }

    @Test
    void whenReadAllBillsFromDbThenThrowException() throws Exception {
        //given
        Mockito.when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> billDAO.readAll();

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenUpdateBillInDbThenReturnTrue() throws Exception {
        //given
        long testId = 1L;
        int testCount = 1;
        Date testDate = null;
        Bill testBill = new Bill(testId, testDate);
        Mockito.when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean updated = billDAO.update(testBill);

        //then
        assertEquals(true, updated);
    }

    @Test
    void whenUpdateBillInDbThenReturnFalse() throws Exception {
        //given
        long testId = 1L;
        int testCount = 0;
        Date testDate = null;
        Bill testBill = new Bill(testId, testDate);
        Mockito.when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean updated = billDAO.update(testBill);

        //then
        assertEquals(false, updated);
    }

    @Test
    void whenUpdateBillInDbThenThrowException() throws Exception {
        //given
        long testId = 1L;
        Date testDate = null;
        Bill testBill = new Bill(testId, testDate);
        Mockito.when(mockConnection.prepareStatement(UPDATE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> billDAO.update(testBill);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenDeleteBillFromDbThenReturnTrue()throws Exception  {
        //given
        long testId = 1L;
        int testCount = 1;
        Mockito.when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean deleted = billDAO.delete(testId);

        //then
        assertEquals(true, deleted);
    }

    @Test
    void whenDeleteBillFromDbThenReturnFalse()throws Exception  {
        //given
        long testId = 1L;
        int testCount = 0;
        Mockito.when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean deleted = billDAO.delete(testId);

        //then
        assertEquals(false, deleted);
    }

    @Test
    void whenDeleteBillFromDbThenThrowException()throws Exception  {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(DELETE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> billDAO.delete(testId);

        //then
        assertThrows(DAOException.class, executable);
    }
}