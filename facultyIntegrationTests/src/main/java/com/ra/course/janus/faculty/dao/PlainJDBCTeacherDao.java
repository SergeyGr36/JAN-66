package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.entity.Teacher;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class PlainJDBCTeacherDao implements TeacherDao<Teacher> {
    private final static Logger logger = Logger.getLogger(PlainJDBCTeacherDao.class);

    private static final String INSERT = "INSERT INTO TEACHER (ID, NAME, COURSE) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE TEACHER SET NAME = ?, COURSE = ? WHERE ID = ?";
    private static final String SELECT = "SELECT * FROM TEACHER";
    private static final String DELETE = "DELETE FROM TEACHER WHERE ID = ?";

    private final DataSource dataSource;

    public PlainJDBCTeacherDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Teacher toTeacher(ResultSet resultSet) throws SQLException {
        return new Teacher(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getString("course"));
    }

    private void objectStatement(PreparedStatement preparedStatement, Teacher teacher) throws SQLException {
        preparedStatement.setLong(1, teacher.getId());
        preparedStatement.setString(2, teacher.getName());
        preparedStatement.setString(3, teacher.getCourse());
    }

    @Override
    public Teacher insert(Teacher teacher) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT);
            objectStatement(preparedStatement, teacher);
            preparedStatement.executeUpdate();
            return new Teacher(teacher);
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Teacher teacher) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, teacher.getName());
            preparedStatement.setString(2, teacher.getCourse());
            preparedStatement.setLong(3, teacher.getId());
            final int rowsCount = preparedStatement.executeUpdate();
            return rowsCount != 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Teacher> select() {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Teacher> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(toTeacher(resultSet));
            }
            return list;
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(long id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, id);
            int rowsCount = preparedStatement.executeUpdate();
            return rowsCount != 0;
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }
}
