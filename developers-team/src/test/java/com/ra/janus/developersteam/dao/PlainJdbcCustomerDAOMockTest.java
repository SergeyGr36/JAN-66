package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Customer;
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

class PlainJdbcCustomerDAOMockTest {
    private static final String INSERT_SQL = "INSERT INTO customers (name, address, phone) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE customers SET name=?,address=?,phone=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM customers";
    private static final String SELECT_ONE_SQL = "SELECT * FROM customers WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM customers WHERE id=?";
    private static final long TEST_ID = 1L;
    private static final Customer TEST_CUSTOMER = new Customer(TEST_ID, "John", "Home", "911");

    private DataSource mockDataSource = mock(DataSource.class);

    private PlainJdbcCustomerDAO customerDAO;
    private Connection mockConnection = mock(Connection.class);
    private PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
    private ResultSet mockResultSet = mock(ResultSet.class);

    @BeforeEach
    void before() throws Exception {
        customerDAO = new PlainJdbcCustomerDAO(mockDataSource);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockResultSet.next()).thenReturn(false);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
    }

    @Test
    void whenCreateCustomerShouldReturnCustomer() throws Exception {
        //given
        when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(TEST_ID);

        //when
        Customer customer = customerDAO.create(TEST_CUSTOMER);

        //then
        assertEquals(TEST_CUSTOMER, customer);
    }

    @Test
    void whenCreateCustomerShouldThrowExceptionIfIdWasNotGenerated() throws Exception {
        //given
        when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);

        //when
        final Executable executable = () -> customerDAO.create(TEST_CUSTOMER);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenCreateCustomerShouldThrowException() throws Exception {
        //given
        when(mockConnection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException());

        //when
        final Executable executable = () -> customerDAO.create(TEST_CUSTOMER);

        //then
        assertThrows(DAOException.class, executable);
    }

    //==============================

    @Test
    void whenReadCustomerFromDbByIdThenReturnIt() throws Exception {
        //given
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getLong("id")).thenReturn(TEST_ID);

        //when
        Customer customer = customerDAO.get(TEST_ID);

        //then
        assertEquals(TEST_ID, customer.getId());
    }

    @Test
    void whenReadAbsentCustomerFromDbByIdThenReturnNull() throws Exception {
        //given
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(false);

        //when
        Customer customer = customerDAO.get(TEST_ID);

        //then
        assertNull(customer);
    }

    @Test
    void whenReadCustomerFromDbByIdThenThrowExceptionOnPreparingStatement() throws Exception {
        //given
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> customerDAO.get(TEST_ID);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenReadAllCustomersFromDbThenReturnNonEmptyList() throws Exception {
        //given
        when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);

        //when
        List<Customer> list = customerDAO.getAll();

        //then
        assertFalse(list.isEmpty());
    }

    @Test
    void whenReadAllCustomersFromDbThenThrowException() throws Exception {
        //given
        when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> customerDAO.getAll();

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenUpdateCustomerInDbThenReturnTrue() throws Exception {
        //given
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        //when
        boolean updated = customerDAO.update(TEST_CUSTOMER);

        //then
        assertTrue(updated);
    }

    @Test
    void whenUpdateCustomerInDbThenReturnFalse() throws Exception {
        //given
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        //when
        boolean updated = customerDAO.update(TEST_CUSTOMER);

        //then
        assertFalse(updated);
    }

    @Test
    void whenUpdateCustomerInDbThenThrowException() throws Exception {
        //given
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> customerDAO.update(TEST_CUSTOMER);

        //then
        assertThrows(DAOException.class, executable);
    }

    @Test
    void whenDeleteCustomerFromDbThenReturnTrue() throws Exception {
        //given
        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        //when
        boolean deleted = customerDAO.delete(TEST_ID);

        //then
        assertTrue(deleted);
    }

    @Test
    void whenDeleteCustomerFromDbThenReturnFalse() throws Exception {
        //given
        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        //when
        boolean deleted = customerDAO.delete(TEST_ID);

        //then
        assertFalse(deleted);
    }

    @Test
    void whenDeleteCustomerFromDbThenThrowException() throws Exception {
        //given
        when(mockConnection.prepareStatement(DELETE_SQL)).thenThrow(new SQLException());

        //when
        final Executable executable = () -> customerDAO.delete(TEST_ID);

        //then
        assertThrows(DAOException.class, executable);
    }
}