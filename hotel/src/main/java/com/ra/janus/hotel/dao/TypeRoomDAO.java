package com.ra.janus.hotel.dao;

import com.ra.janus.hotel.entity.TypeRoom;
import com.ra.janus.hotel.enums.Query;
import com.ra.janus.hotel.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TypeRoomDAO implements GenericDAO<TypeRoom> {

    private final transient DataSource dataSource;

    private static final Logger LOGGER = LogManager.getLogger(TypeRoomDAO.class);


    public TypeRoomDAO(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public TypeRoom save(final TypeRoom typeRoom) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(Query.INSERT_TYPE_BY_ID.get(), Statement.RETURN_GENERATED_KEYS)) {
            typeRoomSTM(statement, typeRoom);
            if (statement.executeUpdate() == 0) {
                LOGGER.error(Query.SAVE_ERR_MSG.get());
                throw new DaoException(Query.SAVE_ERR_MSG.get());
            } else {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    generatedKeys.next();
                    typeRoom.setId(generatedKeys.getLong(1));
                    return typeRoom;
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public TypeRoom update(final TypeRoom typeRoom) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(Query.UPDATE_TYPE_BY_ID.get())) {
            typeRoomSTM(statement, typeRoom);
            statement.setLong(5, typeRoom.getId());
            if (statement.executeUpdate() == 0) {
                LOGGER.error(Query.UPDATE_ERR_MSG.get());
                throw new DaoException(Query.UPDATE_ERR_MSG.get());
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DaoException(e.getMessage(), e);
        }
        return typeRoom;
    }

    @Override
    public int delete(final long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(Query.DELETE_TYPE_BY_ID.get())) {
            statement.setLong(1, id);
            return statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public TypeRoom findById(final long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(Query.SELECT_TYPE_BY_ID.get())) {
            statement.setLong(1, id);
            final TypeRoom typeRoom;
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    typeRoom = typeRoomRS(resultSet);
                } else {
                    typeRoom = null;
                }
                return typeRoom;
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DaoException(e.getMessage(), e);
        }
    }


    @Override
    public List<TypeRoom> findAll() {
        final List<TypeRoom> typeRoom = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(Query.SELECT_ALL_TYPES.get())) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    typeRoom.add(typeRoomRS(resultSet));
                }
            }
            return typeRoom;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DaoException(e.getMessage(), e);
        }
    }

    private TypeRoom typeRoomRS(final ResultSet resultSet) throws SQLException {
        return new TypeRoom(resultSet.getLong("ID"),
                resultSet.getInt("COUNT_PLACES"),
                resultSet.getInt("PRISE"),
                resultSet.getString("DESCRIPTION"),
                resultSet.getString("CLASS_OF_ROOM"));
    }

    private void typeRoomSTM(final PreparedStatement statement, final TypeRoom typeRoom) throws SQLException {
        statement.setInt(1, typeRoom.getCountPlaces());
        statement.setInt(2, typeRoom.getPrise());
        statement.setString(3, typeRoom.getDescription());
        statement.setString(4, typeRoom.getClassOfRoom());
    }
}
