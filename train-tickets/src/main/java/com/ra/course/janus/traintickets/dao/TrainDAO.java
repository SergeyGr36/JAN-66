package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.dao.IJdbcDao;
import com.ra.course.janus.traintickets.entity.Train;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrainDAO implements IJdbcDao<Train> {

    private final DataSource dataSource;

    private final String INSERT_TRAIN = "INSERT into TRAINS (id, name, quantity_plases, free_plases) values (?, ?, ?, ?)";
    private final String SELECT_TRAIN_ID = "SELECT * FROM TRAINS WHERE id = ?";
    private final String UPDATE_TRAIN = "UPDATE TRAINS SET name = ?, quantity_plases = ?, free_plases = ? WHERE id = ?";
    private final String DELETE_TRAIN = "DELETE * FROM TRAINS WHERE id = ?";
    private final String SELECT_TRAIN_ALL = "SELECT * FROM TRAINS";

    public TrainDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Train save(Train train) {
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            PreparedStatement trainStatement = connection.prepareStatement(INSERT_TRAIN);
            trainStatement.setLong(1,train.getId());
            trainStatement.setString(2,train.getName());
            trainStatement.setInt(3,train.getQuanyityPlaces());
            trainStatement.setInt(4,train.getFreePlaces());
            trainStatement.executeUpdate();
            connection.commit();
        }catch (SQLException e){
            throw new IllegalArgumentException();
        }
        return train;
    }

    @Override
    public boolean update(Long id, Train train) {
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(UPDATE_TRAIN);
            ps.setLong(1,id);
            ps.setString(2,train.getName());
            ps.setInt(3,train.getQuanyityPlaces());
            ps.setInt(4,train.getFreePlaces());
            ps.executeUpdate();
            connection.commit();
            return true;//TODO...
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Long id) {
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(DELETE_TRAIN);
            ps.setLong(1,id);
            ps.execute();
            connection.commit();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return false;//TODO...
    }

    @Override
    public Train findById(Long id) {
        Train train = new Train();
        try(Connection connection = dataSource.getConnection()){
            try (Statement statement = connection.createStatement()) {
                try(ResultSet resultSet = statement.executeQuery(SELECT_TRAIN_ID)) {
                    train.setId(resultSet.getLong(1));
                    train.setName(resultSet.getString(2));
                    train.setQuanyityPlaces(resultSet.getInt(3));
                    train.setFreePlaces(resultSet.getInt(4));
                }
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return train;
    }

    @Override
    public List<Train> findAll() {
        ArrayList <Train> trainsList = new ArrayList<>();
        Train train = new Train();
        try(Connection connection = dataSource.getConnection()){
            try(Statement statement = connection.createStatement()){
                try(ResultSet resultSet = statement.executeQuery(SELECT_TRAIN_ALL)){
                    while (resultSet.next()){
                        train.setId(resultSet.getLong(1));
                        train.setName(resultSet.getString(2));
                        train.setQuanyityPlaces(resultSet.getInt(3));
                        train.setFreePlaces(resultSet.getInt(4));
                        trainsList.add(train);
                    }
                }
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return trainsList;
    }
}
