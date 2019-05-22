package com.ra.hotel.dao;

import com.ra.hotel.exceptions.DaoException;
import com.ra.hotel.entity.Order;
import com.ra.hotel.entity.enums.StatusOrder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO implements GenericDao<Order> {

    private final transient DataSource dataSource;

    public OrderDAO(final DataSource dataSource) {
        this.dataSource = dataSource; //H2ConnectionFactory.getInstance().getDataSource();
    }

    @Override
    public Order save(final Order order) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(Query.ORDER_INSERT)) {
            setValueStatement(statement, order);
            if (statement.executeUpdate() == 0) {
                throw new DaoException("record not saved");
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
        return order;
    }

    @Override
    public Order update(final Order order) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(Query.ORDER_UPDATE_BY_ID)) {
            if (statement.executeUpdate() == 0) {
                throw new DaoException("record not updated");
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
        return order;
    }

    @Override
    public int delete(final Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(Query.ORDER_DELETE_BY_ID)) {
            statement.setLong(1, id);
           return statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public Order findById(final Long id)  {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(Query.ORDER_SELECT_BY_ID)){
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery();){
                if (resultSet.next()) {
                    return orderOfResultSet(resultSet);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public List<Order> findAll() throws DaoException {
        final List<Order> orders = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(Query.ORDER_SELECT_ALL)){
            try (ResultSet resultSet = statement.executeQuery();){
                while (resultSet.next()) {
                    orders.add(orderOfResultSet(resultSet));
                }
            }
            return orders;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    private Order orderOfResultSet(final ResultSet resultSet) throws SQLException {
        return new Order(resultSet.getLong("id"),
                null,
                null,
                resultSet.getDate("data_in"),
                resultSet.getDate("data_out"),
                StatusOrder.valueOf(resultSet.getString("status")),
                resultSet.getDate("date_create"),
                resultSet.getDate("date_update"),
                null);
    }

    private void setValueStatement(final PreparedStatement statement, final Order order) throws SQLException {
        //statement.setLong(1, order.getClient().getId());
        //statement.setLong(2, order.getTypeRoom().getId());
        statement.setDate(3, order.getDateIn());
        statement.setDate(4, order.getDateOut());
        statement.setString(5, order.getStatus().name());
        statement.setDate(6, order.getDateCreate());
        statement.setDate(7, order.getDateUpdate());
        //statement.setLong(8, order.getRoom().getId());
        statement.setLong(9, order.getId());
    }
}
