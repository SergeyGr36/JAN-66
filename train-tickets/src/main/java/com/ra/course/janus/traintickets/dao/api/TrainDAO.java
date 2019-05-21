package com.ra.course.janus.traintickets.dao.api;

import com.ra.course.janus.traintickets.entity.Train;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class TrainDAO implements DAO<Train> {

    private final DataSource dataSource;

    private final String INSERT_TRAIN = "insert into Trains (name, id) values (?, ?)";
    private final String SELECT_TRAIN = "SELECT * FROM USERS WHERE id = ?";

    public TrainDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Train save(Train train) {
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            PreparedStatement trainStatement = connection.prepareStatement(INSERT_TRAIN);
            trainStatement.setString(1,train.getName());
            trainStatement.setLong(2,train.getId());
            trainStatement.executeUpdate();
            connection.commit();
        }catch (SQLException e){
            throw new IllegalArgumentException();
        }
        return train;
    }

    @Override
    public boolean update(Long id, Train item) {
        return false;
    }

    @Override
    public boolean delete(Long id) {
        return false;
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
