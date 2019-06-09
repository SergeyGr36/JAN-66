package com.ra.course.janus.traintickets.dao;


import com.ra.course.janus.traintickets.entity.Admin;
import com.ra.course.janus.traintickets.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.ra.course.janus.traintickets.exception.ErrorMessages.*;

public class AdminJdbcDao implements IJdbcDao<Admin> {

    private static final String SAVE_SQL = "INSERT INTO ADMIN (NAME, LASTNAME, PASSWORD) VALUES(?,?,?)";
    private static final String UPDATE_SQL = "UPDATE ADMIN SET NAME=?,LASTNAME=?,PASSWORD=? WHERE ID=?";
    private static final String DELETE_SQL = "DELETE FROM ADMIN WHERE ID=? ";
    private static final String SELECT_ALL = "SELECT * FROM ADMIN";
    private static final String SELECT_BY_ID = "SELECT * FROM ADMIN WHERE ID=?";

    private static final int COLUM_ID = 1;
    private static final int COLUM_NAME = 2;
    private static final int COLUM_L_NAME = 3;
    private static final int COLUM_PASSWORD = 4;

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminJdbcDao.class.getName());

    private final transient DataSource dataSource;

    public AdminJdbcDao(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Admin save(final Admin item) {

        try(Connection connection = dataSource.getConnection();
            PreparedStatement prepSt = connection.prepareStatement(SAVE_SQL)) {
            prepareStatementOperations(prepSt, item);
            prepSt.executeUpdate();
                try (ResultSet generatedKeys = prepSt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return new Admin(generatedKeys.getLong(COLUM_ID), item.getName(),
                                item.getLastName(), item.getPassword());
                    } else {
                        final DAOException e = new DAOException();
                        LOGGER.error(SAVE_FAILED.getMessage(), e);
                        throw e;
                    }
                }

        } catch (SQLException e) {
            LOGGER.error(SAVE_FAILED.getMessage(), e);
            throw new DAOException(e);
           }
    }

    @Override
    public boolean update(final Admin item) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement prepSt = connection.prepareStatement(UPDATE_SQL)){
            prepareStatementOperations(prepSt, item);
            prepSt.setLong(4, item.getId());
            return prepSt.executeUpdate() > 0;
        } catch (SQLException e){
            LOGGER.error(UPDATE_FAILED.getMessage(), e);
            throw new DAOException(e);
        }
    }

    @Override
    public boolean delete(final Long id) {

        try(Connection connection = dataSource.getConnection();
            PreparedStatement prepSt = connection.prepareStatement(DELETE_SQL)){
            prepSt.setLong(1, id);

            return prepSt.executeUpdate()>0;

        } catch (SQLException e){
            LOGGER.error(DELETE_FAILED.getMessage(), e);
            throw new DAOException(e);
        }
    }

    @Override
    public Admin findById(final Long id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement prepSt = connection.prepareStatement(SELECT_BY_ID)){
            prepSt.setLong(1, id);
            try (ResultSet resultSet = prepSt.executeQuery()) {
                if (resultSet.next()) {
                    return createObject(resultSet);
                } else {
                    final DAOException e = new DAOException();
                    LOGGER.error(FIND_FAILED.getMessage(), e);
                    throw e;
                }
            }
        } catch (SQLException e){
            LOGGER.info(FIND_FAILED.getMessage(), e);
            throw new DAOException(e);
        }
    }

    @Override
    public List<Admin> findAll() {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement prepSt = connection.prepareStatement(SELECT_ALL);
            ResultSet resultSet = prepSt.executeQuery()){
            final List <Admin> adminList = new ArrayList<>();
            while (resultSet.next()){
                adminList.add(createObject(resultSet));
            }
            return adminList;

        } catch (SQLException e){
            LOGGER.info(FINDALL_FAILED.getMessage(), e);
            throw new DAOException(e);
        }
    }

    private Admin createObject (final ResultSet resultSet) throws SQLException {
        return new Admin(resultSet.getLong(COLUM_ID),
                resultSet.getString(COLUM_NAME),
                resultSet.getString(COLUM_L_NAME),
                resultSet.getString(COLUM_PASSWORD));
    }

    public void prepareStatementOperations(final PreparedStatement ps, final Admin item) throws SQLException {
        ps.setString(1, item.getName());
        ps.setString(2, item.getLastName());
        ps.setString(3, item.getPassword());
    }
}
