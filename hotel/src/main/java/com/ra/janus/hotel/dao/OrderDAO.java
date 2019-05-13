package com.ra.janus.hotel.dao;

import com.ra.janus.hotel.entity.Order;
import com.ra.janus.hotel.enums.StatusOrder;
import com.ra.janus.hotel.exception.DaoException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class OrderDAO implements IEntityDAO<Order> {

    private final DataSource dataSource;

    private final String selectById = "select * from Order o where o.id = ?";

    public OrderDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Order save(final Order order) throws DaoException {
        return null;
    }

    @Override
    public Order update(final Order order) throws DaoException {
        return null;
    }

    @Override
    public void delete(long id) throws DaoException {

    }

    @Override
    public Order findById(long id) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(selectById);) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Order order = new Order(id,
                        null,
                        null,
                        resultSet.getDate("data_in"),
                        resultSet.getDate("data_out"),
                        StatusOrder.valueOf("status"),
                        resultSet.getDate("date_create"),
                        resultSet.getDate("date_update"),
                        null);
                return order;
            } else {
                throw new DaoException("record not find");
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Order findByParam(Object... params) throws DaoException {
        return null;
    }

    @Override
    public List<Order> findListByParam(Object... params) throws DaoException {
        return null;
    }
}
