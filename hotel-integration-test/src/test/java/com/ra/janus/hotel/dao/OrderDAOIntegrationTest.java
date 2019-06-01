package com.ra.janus.hotel.dao;

import com.ra.janus.hotel.configuration.ConnectionUtils;
import com.ra.janus.hotel.entity.Order;
import com.ra.janus.hotel.enums.StatusOrder;
import com.ra.janus.hotel.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderDAOIntegrationTest {

    private OrderDAO orderDAO;
    private Order order;

    @BeforeEach
    public void init() throws SQLException {
        DataSource dataSource = ConnectionUtils.getDefaultDataSource();
        orderDAO = new OrderDAO(dataSource);
        dataSource.getConnection().createStatement().executeUpdate("delete from T_ORDER");
        order = orderDAO.save(new Order(1L, 1L, null, null, StatusOrder.NEW, null, null, 1L));
    }

    @Test
    void whenCallSaveThenReturnOrder() throws DaoException {
        assertNotNull(orderDAO.save(order));
    }

    @Test
    void whenCallUpdateThenReturnOrder() throws DaoException {
       order.setStatus(StatusOrder.BOOKED);
       assertNotNull(orderDAO.update(order));
    }

    @Test
    void whenCallDeleteThenDeletedOneRecord() {
        assertDoesNotThrow (() -> orderDAO.delete(order.getId()));
    }

    @Test
    void whenCallDeleteThenNotDeletedRecord() {
        assertEquals(orderDAO.delete(9L), 0);
    }

    @Test
    void whenCallFindByIdThenReturnOrder() throws DaoException {
        assertEquals(orderDAO.findById(order.getId()), order);
    }

    @Test
    void whenCallFindByIdThenReturnNull() throws DaoException {
        assertNull(orderDAO.findById(9L));
    }

    @Test
    void whenCallFindAllThenReturnListOrder() throws DaoException {
        orderDAO.save(new Order(2L, 1L, null, null, StatusOrder.NEW, null, null, 1L));
        List<Order> orders = orderDAO.findAll();
        assertEquals(orders.size(), 2);
    }

}
