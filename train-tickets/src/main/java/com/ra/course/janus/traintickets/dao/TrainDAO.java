package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.dao.IJdbcDao;
import com.ra.course.janus.traintickets.entity.Train;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrainDAO implements IJdbcDao<Train> {

    private final DataSource dataSource;

    private final String INSERT_TRAIN = "INSERT into TRAINS (ID, NAME, QUANTYTI_PLASES, FREE_PLASES) values (?, ?, ?, ?)";
    private final String SELECT_TRAIN_ID = "SELECT ID, NAME, QUANTYTI_PLASES, FREE_PLASES FROM TRAINS WHERE id = ";
    private final String UPDATE_TRAIN = "UPDATE TRAINS SET NAME = ?, QUANTYTI_PLASES = ?, FREE_PLASES = ? WHERE ID = ?";
    private final String DELETE_TRAIN = "DELETE * FROM TRAINS WHERE id = ?";
    private final String SELECT_TRAIN_ALL = "SELECT ID, NAME, QUANTYTI_PLASES, FREE_PLASES FROM TRAINS";

    public TrainDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Train save(Train train) {
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            try(PreparedStatement pr = connection.prepareStatement(INSERT_TRAIN)){
                pr.setLong(1,train.getId());
                pr.setString(2,train.getName());
                pr.setInt(3,train.getQuanyityPlaces());
                pr.setInt(4,train.getFreePlaces());
                pr.executeUpdate();
                connection.commit();
                return train;
            }
        }catch (SQLException e){
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean update(Long id, Train train) {
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            try(PreparedStatement ps = connection.prepareStatement(UPDATE_TRAIN)){
                ps.setString(1,train.getName());
                ps.setInt(2,train.getQuanyityPlaces());
                ps.setInt(3,train.getFreePlaces());
                ps.setLong(4,id);
                int resultUp = ps.executeUpdate();
                connection.commit();
                return  resultUp == 1;
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Long id) {
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            try(PreparedStatement ps = connection.prepareStatement(DELETE_TRAIN)){
                ps.setLong(1,id);
               int resultUP = ps.executeUpdate();
                connection.commit();
                return resultUP == 1;
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Train findById(Long id) {
        Train train = new Train();
        try(Connection connection = dataSource.getConnection()){
            try(Statement statement = connection.createStatement()){
                try(ResultSet resultSet = statement.executeQuery(SELECT_TRAIN_ID + id)){
                    while (resultSet.next()){
                        train.setId(resultSet.getLong(1));
                        train.setName(resultSet.getString(2));
                        train.setQuanyityPlaces(resultSet.getInt(3));
                        train.setFreePlaces(resultSet.getInt(4));
                    }
                }
            }
            return train;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Train> findAll() {
        ArrayList <Train> trainsList = new ArrayList<>();
        try(Connection connection = dataSource.getConnection()){
            try(Statement statement = connection.createStatement()){
                try(ResultSet resultSet = statement.executeQuery(SELECT_TRAIN_ALL)){
                    while (resultSet.next()){
                        Train train = new Train();
                        train.setId(resultSet.getLong(1));
                        train.setName(resultSet.getString(2));
                        train.setQuanyityPlaces(resultSet.getInt(3));
                        train.setFreePlaces(resultSet.getInt(4));
                        trainsList.add(train);
                    }return trainsList;
                }
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}

