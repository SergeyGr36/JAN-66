package com.ra.course.janus.traintickets.dao;
import com.ra.course.janus.traintickets.entity.Train;
import com.ra.course.janus.traintickets.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.ra.course.janus.traintickets.exception.ErrorMessages.*;

public class TrainDAO implements IJdbcDao<Train> {

    private final transient DataSource dataSource;
    private static final Logger LOG = LoggerFactory.getLogger(TrainDAO.class.getName());
    private static final String INSERT_TRAIN = "INSERT into TRAINS (NAME, SEATING, FREE_SEATS) values (?, ?, ?)";
    private static final String SELECT_TRAIN_ID = "SELECT ID, NAME, SEATING, FREE_SEATS FROM TRAINS WHERE ID = ?";
    private static final String UPDATE_TRAIN = "UPDATE TRAINS SET NAME = ?, SEATING = ?, FREE_SEATS = ? WHERE ID = ?";
    private static final String DELETE_TRAIN = "DELETE FROM TRAINS WHERE ID = ?";
    private static final String SELECT_TRAIN_ALL = "SELECT ID, NAME, SEATING, FREE_SEATS FROM TRAINS";

    public  TrainDAO(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Train save(final Train train) {
        try(Connection connection = dataSource.getConnection()){
            try(PreparedStatement pr = connection.prepareStatement(INSERT_TRAIN)){
                trainPrSt(pr,train);
                pr.executeUpdate();
                try(ResultSet rs = pr.getGeneratedKeys()){
                    if (rs.next()){
                        return new Train(rs.getLong(1),train.getName(),
                                train.getSeating(),train.getFreeSeats());
                    }else {
                        LOG.info("Exception in SAVE");
                         throw new DAOException("Train is not created");
                    }
                }
            }
        }catch (SQLException e){
           LOG.error(SAVE_FAILED.getMessage(),e);
            throw new DAOException(e);
        }
    }

    @Override
    public boolean update(final Long id, final Train train) {
        try(Connection connection = dataSource.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(UPDATE_TRAIN)){
                trainPrSt(ps,train);
                ps.setLong(4,id);
                return  ps.executeUpdate() == 1;
            }
        }catch (SQLException e){
            LOG.error(UPDATE_FAILED.getMessage(),e);
            throw new DAOException(e);
        }
    }

    @Override
    public boolean delete(final Long id) {
        try(Connection connection = dataSource.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(DELETE_TRAIN)){
                ps.setLong(1,id);
                return ps.executeUpdate() > 0;
            }
        }catch (SQLException e){
            LOG.error(DELETE_FAILED.getMessage(),e);
            throw new DAOException(e);
        }
    }

    @Override
    public Train findById(final Long id) {
        try(Connection connection = dataSource.getConnection()){
            try(PreparedStatement pr = connection.prepareStatement(SELECT_TRAIN_ID)){
                pr.setLong(1,id);
                try(ResultSet resultSet = pr.executeQuery()){
                    if (resultSet.next()){
                        return createTrain(resultSet);
                    }else {
                        LOG.info("Exception: Train didn't find ");
                        throw new DAOException("This id didn't find");
                    }
                }
            }
        }catch (SQLException e){
            LOG.error(FIND_FAILED.getMessage(),e);
            throw new DAOException(e);
        }
    }

    @Override
    public List<Train> findAll() {
        final  ArrayList <Train> trainsList = new ArrayList<>();
        try(Connection connection = dataSource.getConnection()){
            try(PreparedStatement pr = connection.prepareStatement(SELECT_TRAIN_ALL)){
                try(ResultSet resultSet = pr.executeQuery()){
                    while (resultSet.next()){
                        trainsList.add(createTrain(resultSet));
                    }
                }
            }return trainsList;
        }catch (SQLException e){
            LOG.error(FINDALL_FAILED.getMessage(),e);
            throw new DAOException(e);
        }
    }

    private Train createTrain(final ResultSet rs)throws SQLException{
    return new Train(rs.getLong(1), rs.getString(2),
            rs.getInt(3),rs.getInt(4));
    }

    private void trainPrSt(final PreparedStatement pr,final Train train)throws SQLException{
        pr.setString(1,train.getName());
        pr.setInt(2,train.getSeating());
        pr.setInt(3,train.getFreeSeats());
    }
}

