package com.ra.janus.hotel.dao;

import com.ra.janus.hotel.entity.Order;
import com.ra.janus.hotel.enums.Query;
import com.ra.janus.hotel.enums.StatusOrder;
import com.ra.janus.hotel.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class OrderDAOTestMock {

    private OrderDAO orderDAO;

    @Mock private DataSource mockDataSource;
    @Mock private Connection mockConnection;
    @Mock private PreparedStatement mockStatement;
    @Mock private ResultSet resultSet;

    @BeforeEach
    private void init() throws SQLException {
        MockitoAnnotations.initMocks(this);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        orderDAO = new OrderDAO(mockDataSource);
    }

    @Test
    void whenCallSaveThenReturnOrder() throws SQLException, DaoException {
        when(mockConnection.prepareStatement(Query.INSERT_ORDER.get(), Statement.RETURN_GENERATED_KEYS)).thenReturn(mockStatement);
        when(mockStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(1L);
        when(mockStatement.executeUpdate()).thenReturn(1);
        assertNotNull(orderDAO.save(new Order(1L, 1L, null, null, StatusOrder.NEW, null, null, 1L)));
    }

    @Test
    void whenCallSaveAndSavedZeroRecordsThrowException() throws SQLException {
        when(mockConnection.prepareStatement(Query.INSERT_ORDER.get(), Statement.RETURN_GENERATED_KEYS)).thenReturn(mockStatement);
        when(mockStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(1L);
        when(mockStatement.executeUpdate()).thenReturn(0);
        assertThrows(DaoException.class, () -> orderDAO.save(new Order(1L, 1L, null, null, StatusOrder.NEW, null, null, null)));
    }

    @Test
    void whenCallSaveThrowException() throws SQLException {
        when(mockConnection.prepareStatement(Query.INSERT_ORDER.get(), Statement.RETURN_GENERATED_KEYS)).thenReturn(mockStatement);
        when(mockStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(1L);
        when(mockStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> orderDAO.save(new Order(1L, 1L, null, null, StatusOrder.NEW, null, null, null)));
    }

    @Test
    void whenCallUpdateThenReturnOrder() throws SQLException, DaoException {
        when(mockConnection.prepareStatement(Query.UPDATE_ORDER_BY_ID.get())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);
        assertNotNull(orderDAO.update(new Order()));
    }

    @Test
    void whenCallUpdateThenUpdatedZeroRecordsAndThrowException() throws SQLException {
        when(mockConnection.prepareStatement(Query.UPDATE_ORDER_BY_ID.get())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(0);
        assertThrows(DaoException.class, () -> orderDAO.update(new Order()));
    }

    @Test
    void whenCallUpdateThrowException() throws SQLException {
        when(mockConnection.prepareStatement(Query.UPDATE_ORDER_BY_ID.get())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> orderDAO.update(new Order()));
    }

    @Test
    void whenCallDeleteThenDeletedOneRecord() throws SQLException {
        when(mockConnection.prepareStatement(Query.DELETE_ORDER_BY_ID.get())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);
        assertDoesNotThrow (() -> orderDAO.delete(1L));
    }

    @Test
    void whenCallDeleteThenNotDeletedRecord() throws SQLException {
        when(mockConnection.prepareStatement(Query.DELETE_ORDER_BY_ID.get())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(0);
        assertEquals(orderDAO.delete(1L), 0);
    }

    @Test
    void whenCallDeleteThenThrowException() throws SQLException {
        when(mockConnection.prepareStatement(Query.DELETE_ORDER_BY_ID.get())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> orderDAO.delete(1L));
    }

    @Test
    void whenCallFindByIdThenReturnOrder() throws SQLException, DaoException {
        final long id = 1L;
        when(mockConnection.prepareStatement(Query.SELECT_ORDER_BY_ID.get())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(id);
        when(resultSet.getString("status")).thenReturn("NEW");
        Order order = orderDAO.findById(id);
        assertEquals(order.getId(), id);
    }

    @Test
    void whenCallFindByIdThenReturnNull() throws SQLException, DaoException {
        final long id = 1L;
        when(mockConnection.prepareStatement(Query.SELECT_ORDER_BY_ID.get())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        assertNull(orderDAO.findById(id));
    }

    @Test
    void whenCallFindByIdThenReturnException() throws SQLException {
        final long id = 1L;
        when(mockConnection.prepareStatement(Query.SELECT_ORDER_BY_ID.get())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> orderDAO.findById(id));
    }

    @Test
    void whenCallFindAllThenReturnListOrder() throws SQLException, DaoException {
        final long id = 1L;
        when(mockConnection.prepareStatement(Query.SELECT_ALL_ORDERS.get())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(id);
        List<Order> orders = orderDAO.findAll();
        assertEquals(orders.size(), 2);
    }

    @Test
    void whenCallFindAllThenReturnEmptyList() throws SQLException, DaoException {
        final long id = 1L;
        when(mockConnection.prepareStatement(Query.SELECT_ALL_ORDERS.get())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        assertEquals(orderDAO.findAll().size(), 0);
    }

    @Test
    void whenCallFindAllThenReturnException() throws SQLException {
        final long id = 1L;
        when(mockConnection.prepareStatement(Query.SELECT_ALL_ORDERS.get())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> orderDAO.findAll());
    }
}
