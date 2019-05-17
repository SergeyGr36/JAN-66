package com.ra.janus.hotel.dao;

import com.ra.janus.hotel.entity.TypeRoom;
import com.ra.janus.hotel.exception.DaoException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TypeRoomDAO implements IEntityDAO<TypeRoom>{

    private final transient DataSource dataSource;

    private final static String INSERT = "INSERT INTO TYPE_ROOM (ID, COUNT_PLACES, PRISE, DESCRIPTION, CLASS_OF_ROOM) VALUES (?, ?, ?, ?, ?)";
    private final static String UPDATE = "UPDATE TYPE_ROOM SET COUNT_PLACES = ?, PRISE = ?, DESCRIPTION = ?, CLASS_OF_ROOM = ? WHERE ID = ?";
    private final static String DELETE = "DELETE FROM TYPE+ROOM WHERE ID = ?";
    private final static String SELECT_BY_ID = "SELECT * FROM TYPE_ROOM WHERE ID = ?";
    private final static String SELECT_ALL = "SELECT * FROM TYPE_ROOM";

    public TypeRoomDAO(final DataSource dataSource) {
        this.dataSource = dataSource; //H2ConnectionFactory.getInstance().getDataSource();
    }

    @Override
    public TypeRoom save(TypeRoom typeRoom) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            setValueStatement(statement, typeRoom);
            if (statement.executeUpdate() == 0) {
                throw new DaoException("record not saved");
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
        return typeRoom;
    }

    @Override
    public TypeRoom update(TypeRoom typeRoom) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            setValueStatement(statement, typeRoom);
            if (statement.executeUpdate() == 0){
                throw new DaoException("record not update");
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
        return typeRoom;
    }

    @Override
    public void delete(long id) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setLong(1, id);
            if (statement.executeUpdate() == 0){
                throw new DaoException("record not deleted");
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public TypeRoom findById(long id) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID)){
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery();){
                if (resultSet.next()) {
                    return typeRoomRS(resultSet);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public List<TypeRoom> findAll() throws DaoException {
        final List<TypeRoom> orders = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL)){
            try (ResultSet resultSet = statement.executeQuery();){
                while (resultSet.next()) {
                    orders.add(typeRoomRS(resultSet));
                }
            }
            return orders;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    private TypeRoom typeRoomRS(final ResultSet resultSet) throws SQLException {
        return new TypeRoom(resultSet.getLong("ID"),
                resultSet.getInt("COUNT_PLACE"),
                resultSet.getInt("PRISE"),
                resultSet.getString("DESCRIPTION"),
                resultSet.getString("CLASS_OF_ROOM"));
    }

    private void setValueStatement(final PreparedStatement statement, final TypeRoom typeRoom) throws SQLException {
        statement.setLong(1, typeRoom.getId());
        statement.setInt(2, typeRoom.getCountPlaces());
        statement.setInt(3, typeRoom.getPrise());
        statement.setString(4, typeRoom.getDescription());
        statement.setString(5, typeRoom.getClassOfRoom());
    }
}
