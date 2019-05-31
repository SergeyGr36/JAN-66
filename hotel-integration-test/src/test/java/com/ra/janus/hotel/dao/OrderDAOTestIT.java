package com.ra.janus.hotel.dao;

import com.ra.janus.hotel.configuration.H2ConnectionUtils;
import com.ra.janus.hotel.entity.Order;
import com.ra.janus.hotel.enums.StatusOrder;
import com.ra.janus.hotel.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.*;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderDAOTestIT {

    private OrderDAO orderDAO;

    @BeforeEach
    private void init() throws SQLException {
        DataSource dataSource = H2ConnectionUtils.getDefaultDataSource();
        orderDAO = new OrderDAO(dataSource);
        dataSource.getConnection().createStatement().executeUpdate("delete from T_ORDER");
    }

    @Test
    void whenCallSaveThenReturnOrder() throws DaoException {
        Order order = new Order();
        order.setIdClient(1L);
        order.setStatus(StatusOrder.NEW);
        assertNotNull(orderDAO.save(order));
    }

    @Test
    void whenCallUpdateThenReturnOrder() throws DaoException {
        Order order = orderDAO.save(new Order(1L, 1L, null, null, StatusOrder.NEW, null, null, null));
        order.setStatus(StatusOrder.BOOKED);
        assertNotNull(orderDAO.update(order));
    }

    @Test
    void whenCallUpdateThenUpdatedZeroRecordsAndThrowException() {
        Order order = new Order();
        assertThrows(DaoException.class, () -> orderDAO.update(order));
    }

    @Test
    void whenCallUpdateThrowException() {
        Order order = new Order();
        assertThrows(DaoException.class, () -> orderDAO.update(order));
    }

    @Test
    void whenCallDeleteThenDeletedOneRecord() {
        Order order = orderDAO.save(new Order(1L, 1L, null, null, StatusOrder.NEW, null, null, null));
        assertDoesNotThrow (() -> orderDAO.delete(order.getId()));
    }

    @Test
    void whenCallDeleteThenNotDeletedRecord() {
        assertEquals(orderDAO.delete(9L), 0);
    }

    @Test
    void whenCallFindByIdThenReturnOrder() throws DaoException {
        Order order = orderDAO.save(new Order(1L, 1L, null, null, StatusOrder.NEW, null, null, null));
        assertEquals(orderDAO.findById(order.getId()).hashCode(), order.hashCode());
    }

    @Test
    void whenCallFindByIdThenReturnNull() throws DaoException {
        final long id = 9L;
        assertNull(orderDAO.findById(id));
    }

    @Test
    void whenCallFindAllThenReturnListOrder() throws DaoException {
        orderDAO.save(new Order(1L, 1L, null, null, StatusOrder.NEW, null, null, null));
        orderDAO.save(new Order(2L, 1L, null, null, StatusOrder.NEW, null, null, null));
        List<Order> orders = orderDAO.findAll();
        assertEquals(orders.size(), 2);
    }

}
