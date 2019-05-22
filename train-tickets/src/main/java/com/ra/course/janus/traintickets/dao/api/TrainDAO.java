package com.ra.course.janus.traintickets.dao.api;

import com.ra.course.janus.traintickets.entity.Train;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class TrainDAO implements DAO<Train> {

    private final DataSource dataSource;

    private final String INSERT_TRAIN = "INSERT into TRAINS (id, name, quantity_plases, free_plases) values (?, ?, ?, ?)";
    private final String SELECT_TRAIN = "SELECT * FROM TRAINS WHERE id = ?";
    private final String UPDATE_TRAIN = "UPDATE TRAINS SET name = ? WHERE id = ?";
    private final String DELETE_TRAIN = "DELETE * FROM TRAINS WHERE id = ?";

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
            trainStatement.setInt(3,train.getQuantityOfPlacesInTrain());
            trainStatement.setInt(4,train.getNumberOfFreePlacesInTheTrain());
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
            ps.setString(1,train.getName());
            ps.setLong(2,train.getId());
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
        return null;
    }

    @Override
    public List<Train> findAll() {
        return null;
    }
}
