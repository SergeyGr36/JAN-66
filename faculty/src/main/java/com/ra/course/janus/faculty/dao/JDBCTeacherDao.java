package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.entity.Teacher;
import com.ra.course.janus.faculty.exception.DaoException;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class JDBCTeacherDao implements GenericDao<Teacher> {
    private final static Logger LOGGER = Logger.getLogger(JDBCTeacherDao.class);

    private static final String INSERT_TEACHER = "INSERT INTO TEACHER (ID, NAME, COURSE) VALUES (?, ?, ?)";
    private static final String UPDATE_TEACHER = "UPDATE TEACHER SET NAME = ?, COURSE = ? WHERE ID = ?";
    private static final String SELECT_TEACHER = "SELECT * FROM TEACHER";
    private static final String SELECT_BY_ID = "SELECT * FROM TEACHER WHERE ID = ?";
    private static final String DELETE_TEACHER = "DELETE FROM TEACHER WHERE ID = ?";

    transient private final DataSource dataSource;

    JDBCTeacherDao(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @SuppressWarnings("PMD")
    Teacher toTeacher(final ResultSet resultSet) throws SQLException {
        return new Teacher(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getString("course"));
    }

    private void objectStatement(final PreparedStatement preparedStatement, final Teacher teacher) throws SQLException {
        preparedStatement.setLong(1, teacher.getId());
        preparedStatement.setString(2, teacher.getName());
        preparedStatement.setString(3, teacher.getCourse());
    }

    @Override
    public Teacher insert(final Teacher teacher) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TEACHER);
            objectStatement(preparedStatement, teacher);
            preparedStatement.executeUpdate();
            return new Teacher(teacher);
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(final Teacher teacher) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TEACHER);
            preparedStatement.setString(1, teacher.getName());
            preparedStatement.setString(2, teacher.getCourse());
            preparedStatement.setLong(3, teacher.getId());
            final int rowsCount = preparedStatement.executeUpdate();
            return rowsCount > 0;
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    @SuppressWarnings("PMD.CloseResource")
    public List<Teacher> select() {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TEACHER);
            final ResultSet resultSet = preparedStatement.executeQuery();
            final List<Teacher> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(toTeacher(resultSet));
            }
            return list;
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public Teacher selectById(final long id) {
        try (Connection connection = dataSource.getConnection()){
            final PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID);
            preparedStatement.setLong(1, id);
            final ResultSet resultSet = preparedStatement.executeQuery();
            try {
                if (resultSet.next()) {
                    return new Teacher(toTeacher(resultSet));
                } else {
                    return null;
                }
            } finally {
                preparedStatement.close();
                resultSet.close();
            }
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(final long id) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TEACHER);
            preparedStatement.setLong(1, id);
            final int rowsCount = preparedStatement.executeUpdate();
            return rowsCount != 0;
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new DaoException(e);
        }
    }
}
