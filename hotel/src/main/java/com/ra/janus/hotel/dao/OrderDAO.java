
package com.ra.janus.hotel.dao;

import com.ra.janus.hotel.entity.Order;
import com.ra.janus.hotel.enums.Query;
import com.ra.janus.hotel.enums.StatusOrder;
import com.ra.janus.hotel.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO implements GenericDAO<Order> {

    private static final Logger LOG = LogManager.getLogger(OrderDAO.class);

    private final transient DataSource dataSource;


    public OrderDAO(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Order save(final Order order) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(Query.INSERT_ORDER.get(), Statement.RETURN_GENERATED_KEYS)) {
            setValueStatementInsert(statement, order);
            if (statement.executeUpdate() == 0) {
                LOG.error(Query.MSG_ERR_SAVE.get());
                throw new DaoException(Query.MSG_ERR_SAVE.get());
            } else {
                try (ResultSet genKey = statement.getGeneratedKeys()) {
                    genKey.next();
                    order.setId(genKey.getLong(1));
                    return order;
                }
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public Order update(final Order order) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(Query.UPDATE_ORDER_BY_ID.get())) {
            setValueStatementUpdate(statement, order);
            if (statement.executeUpdate() == 0) {
                LOG.error(Query.MSG_ERR_UPDATE.get());
                throw new DaoException(Query.MSG_ERR_UPDATE.get());
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
             PreparedStatement statement = connection.prepareStatement(Query.DELETE_ORDER_BY_ID.get())) {
            statement.setLong(1, id);
            return statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public Order findById(final long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(Query.SELECT_ORDER_BY_ID.get())) {
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
             PreparedStatement statement = connection.prepareStatement(Query.SELECT_ALL_ORDERS.get())){
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

    private void setValueStatementInsert(final PreparedStatement statement, final Order order) throws SQLException {
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
    }

    private void setValueStatementUpdate(final PreparedStatement statement, final Order order) throws SQLException {
        setValueStatementInsert(statement, order);
        statement.setLong(9, order.getId());
    }

}
