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
public class PlainJDBCTeacherDao implements TeacherDao<Teacher> {
    private final static Logger LOGGER = Logger.getLogger(PlainJDBCTeacherDao.class);

    private static final String INSERT_TEACHER = "INSERT INTO TEACHER (ID, NAME, COURSE) VALUES (?, ?, ?)";
    private static final String UPDATE_TEACHER = "UPDATE TEACHER SET NAME = ?, COURSE = ? WHERE ID = ?";
    private static final String SELECT_TEACHER = "SELECT * FROM TEACHER";
    private static final String DELETE_TEACHER = "DELETE FROM TEACHER WHERE ID = ?";

    private static final String INSERT_ERR = "Error inserting Teacher";
    private static final String UPDATE_ERR = "Error updating Teacher";
    private static final String DELETE_ERR = "Error deleting Teacher";
    private static final String FIND_ERR = "Error finding Teacher";

    transient private final DataSource dataSource;

    public PlainJDBCTeacherDao(final DataSource dataSource) {
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
            throw new DaoException(INSERT_ERR, e);
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
            return rowsCount != 0;
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new DaoException(UPDATE_ERR, e);
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
            throw new DaoException(FIND_ERR, e);
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
            throw new DaoException(DELETE_ERR, e);
        }
    }
}
