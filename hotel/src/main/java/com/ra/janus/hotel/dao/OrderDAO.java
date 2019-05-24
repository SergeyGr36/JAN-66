package com.ra.janus.hotel.dao;

import com.ra.janus.hotel.entity.Order;
import com.ra.janus.hotel.enums.StatusOrder;
import com.ra.janus.hotel.exception.DaoException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO implements IEntityDAO<Order> {

    private final transient DataSource dataSource;

    private final static String SELECT_BY_ID = "select * from T_ORDER where ID = ?";
    private final static String SELECT_ALL = "select * from T_ORDER";
    private final static String DELETE_BY_ID = "delete from T_ORDER where ID = ?";
    private final static String INSERT = "insert into T_ORDER (ID_CLIENT, ID_TYPE_ROOM, DATE_IN, DATE_OUT, STATUS, DATE_CREATE, DATE_UPDATE, ID_ROOM, ID) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final static String UPDATE_BY_ID = "update T_ORDER set ID_CLIENT = ?, ID_TYPE_ROOM = ?, DATE_IN = ?, DATE_OUT = ?, STATUS = ?, DATE_CREATE = ?, DATE_UPDATE = ?, ID_ROOM = ? where ID = ?";

    public OrderDAO(final DataSource dataSource) {
        this.dataSource = dataSource; //H2ConnectionFactory.getInstance().getDataSource();
    }

    @Override
    public Order save(final Order order) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
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
    public Order update(final Order order) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_BY_ID)) {
            if (statement.executeUpdate() == 0) {
                throw new DaoException("record not updated");
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
        return order;
    }

    @Override
    public void delete(final long id) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID)) {
            statement.setLong(1, id);
            if (statement.executeUpdate() == 0) {
                throw new DaoException("record not deleted");
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public Order findById(final long id) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID)){
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
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL)){
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
