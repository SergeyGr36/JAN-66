package com.ra.janus.hotel.dao;

import com.ra.janus.hotel.configuration.ConnectionUtils;
import com.ra.janus.hotel.entity.Room;
import com.ra.janus.hotel.enums.Query;
import com.ra.janus.hotel.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO implements GenericDAO<Room> {

    private final transient DataSource dataSource;

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionUtils.class);

    public RoomDAO(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Room save(final Room room) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(Query.ADD_ROOM.get(), Statement.RETURN_GENERATED_KEYS)) {
            getStatement(ps, room);
            ps.execute();
            try (
                    ResultSet generatedKeys = ps.getGeneratedKeys()) {
                generatedKeys.next();
                room.setId(generatedKeys.getLong(1));
            }
            return room;
        } catch (SQLException e) {
            LOGGER.info(e.getMessage());
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public Room update(final Room room) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(Query.UPDATE_ROOM.get())) {
            getStatement(ps, room);
            ps.setLong(4, room.getId());
            ps.executeUpdate();
            return findById(room.getId());
        } catch (SQLException e) {
            LOGGER.info(e.getMessage());
            throw new DaoException(e.getMessage(),e);
        }
    }

    @Override
    public int delete(final long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(Query.REMOVE_ROOM.get())) {
            ps.setLong(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.info(e.getMessage());
            throw new DaoException(e.getMessage(),e);
        }
    }

    @Override
    public Room findById(final long id) {
        try (Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(Query.SELECT_ROOM.get())) {
            ps.setLong(1, id);
            final Room room;
            try (ResultSet resultSet = ps.executeQuery()) {
                resultSet.next();
                room = toRoom(resultSet);
            }
            return room;
        } catch (final SQLException e) {
            LOGGER.info(e.getMessage());
            throw new DaoException(e.getMessage(),e);
        }
    }

    @Override
    public List<Room> findAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(Query.SELECT_ALL_ROOMS.get())) {
            final List<Room> clientList = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    clientList.add(toRoom(rs));
                }
            }
            return clientList;
        } catch (SQLException e) {
            LOGGER.info(e.getMessage());
            throw new DaoException(e.getMessage(),e);
        }
    }

    private PreparedStatement getStatement(final PreparedStatement prepare, final Room room) throws SQLException {
        prepare.setString(1, room.getNum());
        prepare.setLong(2, room.getIdTypeRoom());
        prepare.setString(3, room.getDescription());
        return prepare;
    }

    private Room toRoom(final ResultSet rs) throws SQLException {
        return new Room(rs.getLong("id"), rs.getString("num"), rs.getLong("type_id"),
                rs.getNString("description"));
    }
}
