package com.ra.course.janus.traintickets.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.ra.course.janus.traintickets.exception.DAOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;


import com.ra.course.janus.traintickets.entity.Invoice;

class InvoiceJdbcDaoMockTest {
    private final String INSERT_INTO = "INSERT INTO invoices (price, attributes) VALUES (?, ?)";
    private final String UPDATE_TABLE = "UPDATE invoices SET price = ?, attributes = ? WHERE id = ?";
    private final String FIND_BY_ID = "SELECT * FROM invoices WHERE id = ?";
    private final String DELETE_BY_ID = "DELETE FROM invoices WHERE id = ?";
    private final String FIND_ALL = "SELECT * FROM invoices";

    private final long ID = 1;
    private Invoice invoice;
    private InvoiceJdbcDao invoiceDao;
    private DataSource mockDataSource;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void before() throws Exception {
        mockDataSource = mock(DataSource.class);
        invoiceDao = new InvoiceJdbcDao(mockDataSource);
        mockConnection = mock(Connection.class);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        mockResultSet = mock(ResultSet.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        invoice = new Invoice();
    }

    @Test
    public void whenInputInvoiceThanSaveThatOneAndReturnWithNewKey() throws SQLException {
        when(mockConnection.prepareStatement(INSERT_INTO)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        Invoice temp = invoiceDao.save(invoice);
        assertEquals(temp.getAttributes(), invoice.getAttributes());
        assertEquals(temp.getId(), invoice.getId());
    }

    @Test
    public void expectDAOExceptionInSaveMethodWhenResultSetIsFalse() throws SQLException {
        when(mockConnection.prepareStatement(INSERT_INTO)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        assertThrows(DAOException.class, () -> invoiceDao.save(invoice));
    }

    @Test
    public void expectDAOExceptionInSaveMethodWhenResultSetCanNotBeGetting() throws SQLException {
        when(mockConnection.prepareStatement(INSERT_INTO)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenThrow(SQLException.class);
        assertThrows(DAOException.class, () -> invoiceDao.save(invoice));
    }

    @Test
    public void testSaveWhenCantCreatePreparedStatementThenCatchException() throws SQLException {
        when(mockConnection.prepareStatement(INSERT_INTO)).thenThrow(new SQLException());
        Executable ex = () -> invoiceDao.save(invoice);
        assertThrows(DAOException.class,ex );

    }

    @Test
    public void testUpdateWhenCantCreatePreparedStatementThenCatchException() throws SQLException {
        when(mockConnection.prepareStatement(UPDATE_TABLE)).thenThrow(new SQLException());
        Executable ex = () -> invoiceDao.update(ID, invoice);
        assertThrows(DAOException.class, ex);
    }
    @Test
    public void testDeleteWhenCantCreatePreparedStatementThenCatchException() throws SQLException {
        when(mockConnection.prepareStatement(DELETE_BY_ID)).thenThrow(new SQLException());
        Executable ex = () -> invoiceDao.delete(ID);
        assertThrows(DAOException.class, ex);
    }
    @Test
    public void testFindByIdWhenCantCreatePreparedStatementThenCatchException() throws SQLException {
        when(mockConnection.prepareStatement(FIND_BY_ID)).thenThrow(new SQLException());
        Executable ex = () -> invoiceDao.findById(ID);
        assertThrows(DAOException.class, ex);
    }
    @Test
    public void testFindAllWhenCantCreateStatementThenCatchException() throws SQLException {
        when(mockConnection.createStatement()).thenThrow(new SQLException());
        Executable ex = () -> invoiceDao.findAll();
        assertThrows(DAOException.class, ex);
    }

    @Test
    public void updateInvoiceByIDThenReturnTrue() throws SQLException {
        when(mockConnection.prepareStatement(UPDATE_TABLE)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        assertTrue(() -> invoiceDao.update(ID, invoice));
    }

    @Test
    public void updateInvoiceByIDThenReturnFalse() throws SQLException {
        when(mockConnection.prepareStatement(UPDATE_TABLE)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        assertFalse(() -> invoiceDao.update(ID, invoice));
    }

    @Test
    public void testDeleteInvoiceFromDBByIdThenReturnTrue() throws SQLException {
        when(mockConnection.prepareStatement(DELETE_BY_ID)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        assertTrue(() -> invoiceDao.delete(ID));
    }

    @Test
    public void testDeleteInvoiceFromDBByIdThenReturnFalse() throws SQLException {
        when(mockConnection.prepareStatement(DELETE_BY_ID)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        assertFalse(() -> invoiceDao.delete(ID));
    }

    @Test
    public void testFindInvoiceInDBByIdThenReturnInvoice() throws SQLException {
        when(mockConnection.prepareStatement(FIND_BY_ID)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        Invoice actual = invoiceDao.findById(ID);
        assertEquals(actual.getId(), invoiceDao.findById(ID).getId());
        assertEquals(actual.getAttributes(), invoiceDao.findById(ID).getAttributes());

    }

    @Test
    public void testFindInvoiceInDBByIdThenThrowException() throws SQLException {
        when(mockConnection.prepareStatement(FIND_BY_ID)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        assertThrows(DAOException.class, () -> invoiceDao.findById(ID));
    }
    @Test
    public void testFindInvoiceInDBByIdThenThrowExceptionForCreatePreparedStatement() throws SQLException {
        when(mockConnection.prepareStatement(FIND_BY_ID)).thenThrow(new SQLException());
        assertThrows(DAOException.class, () -> invoiceDao.findById(ID));
    }

    @Test
    public void testFindAllInvoicesFromDBThenReturnListWithThem() throws SQLException {
        var statement = mock(Statement.class);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(FIND_ALL)).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        List<Invoice> exp = new ArrayList<>();
        exp.add(invoice);
        assertEquals(exp.get(0).getAttributes(), invoiceDao.findAll().get(0).getAttributes());
    }
}
