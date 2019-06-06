package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Bill;
import com.ra.janus.developersteam.exception.DAOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlainJdbcBillDAOMockTest {
    private static final String INSERT_SQL = "INSERT INTO bills (docDate) VALUES (?)";
    private static final String UPDATE_SQL = "UPDATE bills SET docDate=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM bills";
    private static final String SELECT_ONE_SQL = "SELECT * FROM bills WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM bills WHERE id=?";
    private static final long TEST_ID = 1L;
    private static final Bill TEST_BILL = new Bill(TEST_ID, new Date(System.currentTimeMillis()));

    private JdbcTemplate template = mock(JdbcTemplate.class);

    private PlainJdbcBillDAO billDAO;

//    @BeforeEach
//    void before() throws Exception {
//        billDAO = new PlainJdbcBillDAO(template);
//        when(mockDataSource.getConnection()).thenReturn(mockConnection);
//        when(mockResultSet.next()).thenReturn(false);
//        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
//        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
//    }
//
//    @Test
//    void whenCreateBillShouldReturnBill() throws Exception {
//        //given
//        when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
//        when(mockResultSet.next()).thenReturn(true);
//        when(mockResultSet.getLong(1)).thenReturn(TEST_ID);
//
//        //when
//        Bill bill = billDAO.create(TEST_BILL);
//
//        //then
//        assertEquals(TEST_BILL, bill);
//    }
//
//    @Test
//    void whenCreateBillShouldThrowExceptionIfIdWasNotGenerated() throws Exception {
//        //given
//        when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
//
//        //when
//        final Executable executable = () -> billDAO.create(TEST_BILL);
//
//        //then
//        assertThrows(DAOException.class, executable);
//    }
//
//    @Test
//    void whenCreateBillShouldThrowException() throws Exception {
//        //given
//        when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
//        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException());
//
//        //when
//        final Executable executable = () -> billDAO.create(TEST_BILL);
//
//        //then
//        assertThrows(DAOException.class, executable);
//    }
//
//    //==============================
//
//    @Test
//    void whenReadBillFromDbByIdThenReturnIt() throws Exception {
//        //given
//        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
//        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
//        when(mockResultSet.getLong("id")).thenReturn(TEST_ID);
//
//        //when
//        Bill bill = billDAO.get(TEST_ID);
//
//        //then
//        assertEquals(TEST_ID, bill.getId());
//    }
//
//    @Test
//    void whenReadAbsentBillFromDbByIdThenReturnNull() throws Exception {
//        //given
//        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
//        when(mockResultSet.next()).thenReturn(false);
//
//        //when
//        Bill bill = billDAO.get(TEST_ID);
//
//        //then
//        assertNull(bill);
//    }
//
//    @Test
//    void whenReadBillFromDbByIdThenThrowExceptionOnPreparingStatement() throws Exception {
//        //given
//        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenThrow(new SQLException());
//
//        //when
//        final Executable executable = () -> billDAO.get(TEST_ID);
//
//        //then
//        assertThrows(DAOException.class, executable);
//    }
//
//    @Test
//    void whenReadAllBillsFromDbThenReturnNonEmptyList() throws Exception {
//        //given
//        when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenReturn(mockPreparedStatement);
//        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
//
//        //when
//        List<Bill> list = billDAO.getAll();
//
//        //then
//        assertFalse(list.isEmpty());
//    }
//
//    @Test
//    void whenReadAllBillsFromDbThenThrowException() throws Exception {
//        //given
//        when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenThrow(new SQLException());
//
//        //when
//        final Executable executable = () -> billDAO.getAll();
//
//        //then
//        assertThrows(DAOException.class, executable);
//    }
//
//    @Test
//    void whenUpdateBillInDbThenReturnTrue() throws Exception {
//        //given
//        when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
//        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
//
//        //when
//        boolean updated = billDAO.update(TEST_BILL);
//
//        //then
//        assertTrue(updated);
//    }
//
//    @Test
//    void whenUpdateBillInDbThenReturnFalse() throws Exception {
//        //given
//        when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
//        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
//
//        //when
//        boolean updated = billDAO.update(TEST_BILL);
//
//        //then
//        assertFalse(updated);
//    }
//
//    @Test
//    void whenUpdateBillInDbThenThrowException() throws Exception {
//        //given
//        when(mockConnection.prepareStatement(UPDATE_SQL)).thenThrow(new SQLException());
//
//        //when
//        final Executable executable = () -> billDAO.update(TEST_BILL);
//
//        //then
//        assertThrows(DAOException.class, executable);
//    }
//
//    @Test
//    void whenDeleteBillFromDbThenReturnTrue() throws Exception {
//        //given
//        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
//        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
//
//        //when
//        boolean deleted = billDAO.delete(TEST_ID);
//
//        //then
//        assertTrue(deleted);
//    }
//
//    @Test
//    void whenDeleteBillFromDbThenReturnFalse() throws Exception {
//        //given
//        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
//        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
//
//        //when
//        boolean deleted = billDAO.delete(TEST_ID);
//
//        //then
//        assertFalse(deleted);
//    }
//
//    @Test
//    void whenDeleteBillFromDbThenThrowException() throws Exception {
//        //given
//        when(mockConnection.prepareStatement(DELETE_SQL)).thenThrow(new SQLException());
//
//        //when
//        final Executable executable = () -> billDAO.delete(TEST_ID);
//
//        //then
//        assertThrows(DAOException.class, executable);
//    }
}