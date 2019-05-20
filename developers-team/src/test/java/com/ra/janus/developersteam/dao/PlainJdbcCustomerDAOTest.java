package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Customer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

class PlainJdbcCustomerDAOTest {
    private static final String INSERT_SQL = "INSERT INTO customers (name, address, phone) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE customers SET name=?,address=?,phone=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM customers";
    private static final String SELECT_ONE_SQL = "SELECT * FROM customers WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM customers WHERE id=?";
    private DataSource mockDataSource;

    private PlainJdbcCustomerDAO customerDAO;
    @BeforeEach
    public void before(){
        mockDataSource = Mockito.mock(DataSource.class);
        customerDAO = new PlainJdbcCustomerDAO(mockDataSource);
    }
    @Test
    void create() {
    }

    @Test
    void shouldReadCustomerFromDbById() throws Exception {
        long testId = 1L;
        Connection connection = Mockito.mock(Connection.class);
        PreparedStatement statement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(mockDataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(SELECT_ONE_SQL)).thenReturn(statement);
        Mockito.when(statement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getLong("id")).thenReturn(testId);

        Customer customer = customerDAO.read(testId);
        assertEquals(testId, customer.getId());
    }

    @Test
    void readAll() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}