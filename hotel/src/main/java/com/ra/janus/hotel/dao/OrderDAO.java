
package com.ra.janus.hotel.dao;

import com.ra.janus.hotel.entity.Order;
import com.ra.janus.hotel.enums.StatusOrder;
import com.ra.janus.hotel.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO implements GenericDao<Order> {

    private static final Logger LOG = LogManager.getLogger(OrderDAO.class);

    private final transient DataSource dataSource;

    private final static String MSG_ERR_SAVE = "record not saved";
    private final static String MSG_ERR_UPDATE = "record not updated";

    private final static String SELECT_BY_ID = "select * from T_ORDER where ID = ?";
    private final static String SELECT_ALL = "select * from T_ORDER";
    private final static String DELETE_BY_ID = "delete from T_ORDER where ID = ?";
    private final static String INSERT = "insert into T_ORDER (ID_CLIENT, ID_TYPE_ROOM, DATE_IN, DATE_OUT, STATUS, DATE_CREATE, DATE_UPDATE, ID_ROOM, ID) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final static String UPDATE_BY_ID = "update T_ORDER set ID_CLIENT = ?, ID_TYPE_ROOM = ?, DATE_IN = ?, DATE_OUT = ?, STATUS = ?, DATE_CREATE = ?, DATE_UPDATE = ?, ID_ROOM = ? where ID = ?";

    public OrderDAO(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Order save(final Order order) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            setValueStatement(statement, order);
            if (statement.executeUpdate() == 0) {
                LOG.error(MSG_ERR_SAVE);
                throw new DaoException(MSG_ERR_SAVE);
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
            throw new DaoException(e.getMessage(), e);
        }
        return order;
    }

    @Override
    public Order update(final Order order) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_BY_ID)) {
            setValueStatement(statement, order);
            if (statement.executeUpdate() == 0) {
                LOG.error(MSG_ERR_UPDATE);
                throw new DaoException(MSG_ERR_UPDATE);
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
            throw new DaoException(e.getMessage(), e);
        }
        return order;
    }

    @Override
    public int delete(final long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID)) {
            statement.setLong(1, id);
            return statement.executeUpdate();
/*
            if (statement.executeUpdate() == 0) {
                LOG.error(MSG_ERR_DEL);
                throw new DaoException(MSG_ERR_DEL);
            }
*/
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public Order findById(final long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID)) {
            statement.setLong(1, id);
            final Order order;
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    order = orderOfResultSet(resultSet);
                } else {
                    order = null;
                }
                return order;
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public List<Order> findAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL)){
            final List<Order> orders = new ArrayList<>();
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    orders.add(orderOfResultSet(resultSet));
                }
                resultSet.close();
                return orders;
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
            throw new DaoException(e.getMessage(), e);
        }
    }

    private Order orderOfResultSet(final ResultSet resultSet) throws SQLException {
        final String status = resultSet.getString("status");
        return new Order(resultSet.getLong("id"),
                resultSet.getLong("id_client"),
                resultSet.getLong("id_type_room"),
                resultSet.getDate("date_in"),
                resultSet.getDate("date_out"),
                status == null ? null : StatusOrder.valueOf(status),
                resultSet.getDate("date_create"),
                resultSet.getDate("date_update"),
                resultSet.getLong("id_room"));
    }

    private void setValueStatement(final PreparedStatement statement, final Order order) throws SQLException {
        statement.setLong(1, order.getIdClient());
        statement.setLong(2, order.getIdTypeRoom());
        statement.setDate(3, order.getDateIn());
        statement.setDate(4, order.getDateOut());
        statement.setString(5, order.getStatus().name());
        statement.setDate(6, order.getDateCreate());
        statement.setDate(7, order.getDateUpdate());
        if (order.getIdRoom() == null) {
            statement.setNull(8, Types.BIGINT);
        } else {
            statement.setLong(8, order.getIdRoom());
        }
        statement.setLong(9, order.getId());
    }
}
