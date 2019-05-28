package com.ra.janus.hotel.dao;

import com.ra.janus.hotel.database.H2ConnectionFactory;
import com.ra.janus.hotel.entity.Order;
import com.ra.janus.hotel.enums.StatusOrder;
import com.ra.janus.hotel.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.*;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderDAOTestIT {

    private OrderDAO orderDAO;

    @BeforeEach
    private void init() throws IOException, SQLException {
        DataSource dataSource = H2ConnectionFactory.getInstance().getDataSource();
        createDB(dataSource);
        orderDAO = new OrderDAO(dataSource);
    }

    private void createDB(final DataSource dataSource) throws SQLException, IOException {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        File file = new File(ClassLoader.getSystemResource("create_databases.sql").getFile());
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file, Charset.defaultCharset()));
        String sqlScript;
        while ((sqlScript = bufferedReader.readLine()) != null) {
            statement.execute(sqlScript);
        }
        bufferedReader.close();
        statement.close();
        connection.close();
    }

    @Test
    void whenCallSaveThenReturnOrder() throws DaoException {
        Order order = new Order();
        order.setId(3L);
        order.setIdClient(1L);
        order.setStatus(StatusOrder.NEW);
        assertNotNull(orderDAO.save(order));
    }

    @Test
    void whenCallSaveAndSavedZeroRecordsThrowException() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(StatusOrder.NEW);
        assertThrows(DaoException.class, () -> orderDAO.save(order));
    }

    @Test
    void whenCallUpdateThenReturnOrder() throws DaoException {
        Order order = new Order();
        order.setId(1L);
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
        assertDoesNotThrow (() -> orderDAO.delete(1L));
    }

    @Test
    void whenCallDeleteThenNotDeletedRecord() {
        assertEquals(orderDAO.delete(9L), 0);
    }

    @Test
    void whenCallFindByIdThenReturnOrder() throws DaoException {
        final long id = 1L;
        Order order = orderDAO.findById(id);
        assertEquals(order.getId(), id);
    }

    @Test
    void whenCallFindByIdThenReturnNull() throws DaoException {
        final long id = 9L;
        assertNull(orderDAO.findById(id));
    }

    @Test
    void whenCallFindAllThenReturnListOrder() throws DaoException {
        List<Order> orders = orderDAO.findAll();
        assertEquals(orders.size(), 2);
    }

}
