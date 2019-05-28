package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.entity.Train;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrainDAO implements IJdbcDao<Train> {

    private final DataSource dataSource;

    private static final String INSERT_TRAIN = "INSERT into TRAINS (ID, NAME, SEATING, FREE_SEATS) values (?, ?, ?, ?)";
    private static final String SELECT_TRAIN_ID = "SELECT ID, NAME, SEATING, FREE_SEATS FROM TRAINS WHERE ID = ?";
    private static final String UPDATE_TRAIN = "UPDATE TRAINS SET NAME = ?, SEATING = ?, FREE_SEATS = ? WHERE ID = ?";
    private static final String DELETE_TRAIN = "DELETE ID, NAME, SEATING, FREE_SEATS FROM TRAINS WHERE ID = ?";
    private static final String SELECT_TRAIN_ALL = "SELECT ID, NAME, SEATING, FREE_SEATS FROM TRAINS";

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
                pr.setInt(3,train.getSeating());
                pr.setInt(4,train.getFreeSeats());
                pr.executeUpdate();
                connection.commit();
                return train;
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Long id, Train train) {
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            try(PreparedStatement ps = connection.prepareStatement(UPDATE_TRAIN)){
                ps.setString(1,train.getName());
                ps.setInt(2,train.getSeating());
                ps.setInt(3,train.getFreeSeats());
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
            try(PreparedStatement pr = connection.prepareStatement(SELECT_TRAIN_ID)){
                pr.setLong(1,id);
                try(ResultSet resultSet = pr.executeQuery()){
                    if (resultSet.next()){
                        train.setId(resultSet.getLong(1));
                        train.setName(resultSet.getString(2));
                        train.setSeating(resultSet.getInt(3));
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
            try(PreparedStatement pr = connection.prepareStatement(SELECT_TRAIN_ALL)){
                try(ResultSet resultSet = pr.executeQuery()){
                    if (resultSet.next()){
                        Train train = new Train();
                        train.setId(resultSet.getLong(1));
                        train.setName(resultSet.getString(2));
                        train.setSeating(resultSet.getInt(3));
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

