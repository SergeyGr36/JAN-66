package com.ra.janus.hotel.dao;

import com.ra.janus.hotel.entity.TypeRoom;
import com.ra.janus.hotel.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TypeRoomDAO implements IEntityDAO<TypeRoom>{

    private final transient DataSource dataSource;

    private static final Logger LOGGER = LogManager.getLogger(TypeRoomDAO.class);
    private static final String SAVE_ERR_MSG = "record not saved";
    private static final String UPDATE_ERR_MSG = "record not updated";
    private static final String DELETE_ERR_MSG = "record not deleted";

    private final static String INSERT_BY_ID = "INSERT INTO TYPE_ROOM (COUNT_PLACES, PRISE, DESCRIPTION, CLASS_OF_ROOM, ID) VALUES (?, ?, ?, ?, ?)";
    private final static String UPDATE_BY_ID = "UPDATE TYPE_ROOM SET COUNT_PLACES = ?, PRISE = ?, DESCRIPTION = ?, CLASS_OF_ROOM = ? WHERE ID = ?";
    private final static String DELETE_BY_ID = "DELETE FROM TYPE_ROOM WHERE ID = ?";
    private final static String SELECT_BY_ID = "SELECT * FROM TYPE_ROOM WHERE ID = ?";
    private final static String SELECT_ALL = "SELECT * FROM TYPE_ROOM";

    public TypeRoomDAO(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public TypeRoom save(final TypeRoom typeRoom) throws DaoException {
        return saveUpdate(typeRoom, INSERT_BY_ID, SAVE_ERR_MSG);
    }

    @Override
    public TypeRoom update(final TypeRoom typeRoom) throws DaoException {
        return saveUpdate(typeRoom, UPDATE_BY_ID, UPDATE_ERR_MSG);
    }

    @Override
    public void delete(final long id) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID)) {
            statement.setLong(1, id);
            if (statement.executeUpdate() == 0){
                LOGGER.error(DELETE_ERR_MSG);
                throw new DaoException(DELETE_ERR_MSG);
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public TypeRoom findById(final long id) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID)) {
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
    public List<TypeRoom> findAll() throws DaoException {
        final List<TypeRoom> typeRoom = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL)){
            try (ResultSet resultSet = statement.executeQuery()){
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

    private TypeRoom saveUpdate(final TypeRoom typeRoom, final String insertById, final String saveErrMsg) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertById)) {
            typeRoomSTM(statement, typeRoom);
            if (statement.executeUpdate() == 0) {
                LOGGER.error(saveErrMsg);
                throw new DaoException(saveErrMsg);
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DaoException(e.getMessage(), e);
        }
        return typeRoom;
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
        statement.setLong(5, typeRoom.getId());}
}