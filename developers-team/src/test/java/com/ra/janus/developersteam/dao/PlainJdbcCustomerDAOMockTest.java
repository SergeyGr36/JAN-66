package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Customer;
import com.ra.janus.developersteam.exception.DAOException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

class PlainJdbcCustomerDAOMockTest {
    private static final String INSERT_SQL = "INSERT INTO customers (name, address, phone) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE customers SET name=?,address=?,phone=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM customers";
    private static final String SELECT_ONE_SQL = "SELECT * FROM customers WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM customers WHERE id=?";
    private DataSource mockDataSource;

    private PlainJdbcCustomerDAO customerDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void before() throws Exception {
        mockDataSource = Mockito.mock(DataSource.class);
        customerDAO = new PlainJdbcCustomerDAO(mockDataSource);

        mockConnection = Mockito.mock(Connection.class);
        Mockito.when(mockDataSource.getConnection()).thenReturn(mockConnection);

        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    void whenCreateCustomerShouldReturnCustomer() throws Exception {
        //given
        long testId = 1L;
        int columnIdIndex = 1;
        Customer testCustomer = new Customer(testId, null,null, null);
        Mockito.when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(true);
        Mockito.when(mockResultSet.getLong(columnIdIndex)).thenReturn(testId);

        //when
        Customer customer = customerDAO.create(testCustomer);

        //then
        assertEquals(testCustomer, customer);
    }

    @Test
    void whenCreateCustomerShouldThrowExceptionIfIdWasNotGenerated() throws Exception {
        //given
        long testId = 1L;
        Customer testCustomer = new Customer(testId, null,null, null);
        Mockito.when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(false);

        //when
        final Executable executable = () -> customerDAO.create(testCustomer);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenCreateCustomerShouldThrowException() throws Exception {
        //given
        long testId = 1L;
        Customer testCustomer = new Customer(testId, null,null, null);
        Mockito.when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> customerDAO.create(testCustomer);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenReadCustomerFromDbByIdThenReturnIt() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(mockResultSet.getLong("id")).thenReturn(testId);

        //when
        Customer customer = customerDAO.get(testId);

        //then
        assertEquals(testId, customer.getId());
    }

    @Test
    void whenReadAbsentCustomerFromDbByIdThenReturnNull() throws Exception {
        //given
        long testId = 1L;
        Customer expectedCustomer = null;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockResultSet.next()).thenReturn(false);

        //when
        Customer customer = customerDAO.get(testId);

        //then
        assertEquals(expectedCustomer, customer);
    }

    @Test
    void whenReadCustomerFromDbByIdThenThrowExceptionOnGettingConnection() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockDataSource.getConnection()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> customerDAO.get(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenReadCustomerFromDbByIdThenThrowExceptionOnPreparingStatement() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> customerDAO.get(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenReadCustomerFromDbByIdThenThrowExceptionOnExecutingOfQuery() throws Exception {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> customerDAO.get(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenReadCustomerFromDbByIdThenThrowExceptionOnIteratingOverResultSet() throws Exception {
        //given
        long testId = 1L;
        final int testParametherIndex = 1;
        Mockito.when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> customerDAO.get(testId);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenReadAllCustomersFromDbThenReturnNonEmptyList() throws Exception {
        //given
        Mockito.when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockResultSet.next()).thenReturn(true).thenReturn(false);

        //when
        List<Customer> list = customerDAO.getAll();

        //then
        assertFalse(list.isEmpty());
    }

    @Test
    void whenReadAllCustomersFromDbThenThrowException() throws Exception {
        //given
        Mockito.when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> customerDAO.getAll();

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenUpdateCustomerInDbThenReturnTrue() throws Exception {
        //given
        long testId = 1L;
        int testCount = 1;
        Customer testCustomer = new Customer(testId, null,null, null);
        Mockito.when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean updated = customerDAO.update(testCustomer);

        //then
        assertEquals(true, updated);
    }

    @Test
    void whenUpdateCustomerInDbThenReturnFalse() throws Exception {
        //given
        long testId = 1L;
        int testCount = 0;
        Customer testCustomer = new Customer(testId, null,null, null);
        Mockito.when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean updated = customerDAO.update(testCustomer);

        //then
        assertEquals(false, updated);
    }

    @Test
    void whenUpdateCustomerInDbThenThrowException() throws Exception {
        //given
        long testId = 1L;
        Customer testCustomer = new Customer(testId, null,null, null);
        Mockito.when(mockConnection.prepareStatement(UPDATE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> customerDAO.update(testCustomer);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenDeleteCustomerFromDbThenReturnTrue()throws Exception  {
        //given
        long testId = 1L;
        int testCount = 1;
        Mockito.when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean deleted = customerDAO.delete(testId);

        //then
        assertEquals(true, deleted);
    }

    @Test
    void whenDeleteCustomerFromDbThenReturnFalse()throws Exception  {
        //given
        long testId = 1L;
        int testCount = 0;
        Mockito.when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(testCount);

        //when
        boolean deleted = customerDAO.delete(testId);

        //then
        assertEquals(false, deleted);
    }

    @Test
    void whenDeleteCustomerFromDbThenThrowException()throws Exception  {
        //given
        long testId = 1L;
        Mockito.when(mockConnection.prepareStatement(DELETE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> customerDAO.delete(testId);

        //then
        assertThrows(DAOException.class, executable);
    }
}