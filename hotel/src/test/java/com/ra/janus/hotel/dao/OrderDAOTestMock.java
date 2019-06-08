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

    private final static long ONE = 1L;
    private Order order = new Order(1L, 1L, null, null, StatusOrder.NEW, null, null, 1L);

    @BeforeEach
    public void init() throws SQLException {
        MockitoAnnotations.initMocks(this);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        orderDAO = new OrderDAO(mockDataSource);
    }

    @Test
    public void whenCallSaveThenReturnOrder() throws SQLException, DaoException {
        //when
        when(mockConnection.prepareStatement(Query.INSERT_ORDER.get(), Statement.RETURN_GENERATED_KEYS)).thenReturn(mockStatement);
        when(mockStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(ONE);
        when(mockStatement.executeUpdate()).thenReturn(1);

        //then
        assertNotNull(orderDAO.save(order));
    }

    @Test
    public void whenCallSaveAndSavedZeroRecordsThrowException() throws SQLException {
        //when
        when(mockConnection.prepareStatement(Query.INSERT_ORDER.get(), Statement.RETURN_GENERATED_KEYS)).thenReturn(mockStatement);
        when(mockStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(ONE);
        when(mockStatement.executeUpdate()).thenReturn(0);

        //then
        assertThrows(DaoException.class, () -> orderDAO.save(order));
    }

    @Test
    public void whenCallSaveThrowException() throws SQLException {
        //when
        when(mockConnection.prepareStatement(Query.INSERT_ORDER.get(), Statement.RETURN_GENERATED_KEYS)).thenReturn(mockStatement);
        when(mockStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(1L);
        when(mockStatement.executeUpdate()).thenThrow(SQLException.class);

        //then
        assertThrows(DaoException.class, () -> orderDAO.save(order));
    }

    @Test
    public void whenCallUpdateThenReturnOrder() throws SQLException, DaoException {
        //when
        when(mockConnection.prepareStatement(Query.UPDATE_ORDER_BY_ID.get())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        //then
        assertNotNull(orderDAO.update(order));
    }

    @Test
    public void whenCallUpdateThenUpdatedZeroRecordsAndThrowException() throws SQLException {
        //when
        when(mockConnection.prepareStatement(Query.UPDATE_ORDER_BY_ID.get())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(0);

        //then
        assertThrows(DaoException.class, () -> orderDAO.update(order));
    }

    @Test
    public void whenCallUpdateThrowException() throws SQLException {
        //when
        when(mockConnection.prepareStatement(Query.UPDATE_ORDER_BY_ID.get())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenThrow(SQLException.class);

        //then
        assertThrows(DaoException.class, () -> orderDAO.update(new Order()));
    }

    @Test
    public void whenCallDeleteThenDeletedOneRecord() throws SQLException {
        //when
        when(mockConnection.prepareStatement(Query.DELETE_ORDER_BY_ID.get())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        //then
        assertDoesNotThrow (() -> orderDAO.delete(1L));
    }

    @Test
    public void whenCallDeleteThenNotDeletedRecord() throws SQLException {
        //when
        when(mockConnection.prepareStatement(Query.DELETE_ORDER_BY_ID.get())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(0);

        //then
        assertEquals(orderDAO.delete(1L), 0);
    }

    @Test
    public void whenCallDeleteThenThrowException() throws SQLException {
        //when
        when(mockConnection.prepareStatement(Query.DELETE_ORDER_BY_ID.get())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenThrow(SQLException.class);

        //then
        assertThrows(DaoException.class, () -> orderDAO.delete(1L));
    }

    @Test
    public void whenCallFindByIdThenReturnOrder() throws SQLException, DaoException {
        //when
        when(mockConnection.prepareStatement(Query.SELECT_ORDER_BY_ID.get())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(ONE);
        when(resultSet.getString("status")).thenReturn("NEW");

        //call test method
        Order order = orderDAO.findById(ONE);

        //then
        assertEquals(order.getId(), ONE);
    }

    @Test
    public void whenCallFindByIdThenReturnNull() throws SQLException, DaoException {
        //when
        when(mockConnection.prepareStatement(Query.SELECT_ORDER_BY_ID.get())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        //then
        assertNull(orderDAO.findById(ONE));
    }

    @Test
    public void whenCallFindByIdThenReturnException() throws SQLException {
        //when
        when(mockConnection.prepareStatement(Query.SELECT_ORDER_BY_ID.get())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenThrow(SQLException.class);

        //then
        assertThrows(DaoException.class, () -> orderDAO.findById(ONE));
    }

    @Test
    public void whenCallFindAllThenReturnListOrder() throws SQLException, DaoException {
        //when
        when(mockConnection.prepareStatement(Query.SELECT_ALL_ORDERS.get())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(ONE);

        //call test mothod
        List<Order> orders = orderDAO.findAll();

        //then
        assertEquals(orders.size(), 2);
    }

    @Test
    public void whenCallFindAllThenReturnEmptyList() throws SQLException, DaoException {
        //when
        when(mockConnection.prepareStatement(Query.SELECT_ALL_ORDERS.get())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        //then
        assertEquals(orderDAO.findAll().size(), 0);
    }

    @Test
    public void whenCallFindAllThenReturnException() throws SQLException {
        //when
        when(mockConnection.prepareStatement(Query.SELECT_ALL_ORDERS.get())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenThrow(SQLException.class);

        //then
        assertThrows(DaoException.class, () -> orderDAO.findAll());
    }
}
