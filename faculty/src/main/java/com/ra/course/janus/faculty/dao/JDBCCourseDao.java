package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.entity.Course;

import java.sql.*;
import java.util.List;

import java.util.ArrayList;

import com.ra.course.janus.faculty.exception.DaoException;
import org.apache.log4j.Logger;

import javax.sql.DataSource;

public class JDBCCourseDao implements CourseDao {

    private static final String INSERT_SQL = "INSERT INTO COURSE ( CODE, DESCRIPTION) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE COURSE SET CODE=?,DESCRIPTION=? WHERE COURSE_TID=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM COURSE";
    private static final String SELECT_ONE_SQL = "SELECT * FROM COURSE WHERE COURSE_TID = ?";
    private static final String DELETE_SQL = "DELETE FROM COURSE WHERE COURSE_TID=?";

    private static final String INSERT_ERR = "Error inserting Course";
    private static final String INSERT_ERR_EGT_ID = "Error inserting Course - could not get TID";
    private static final String UPDATE_ERR = "Error updating Course";
    private static final String DELETE_ERR = "Error deleting Course";
    private static final String FIND_ERR = "Error finding Course";

    private final static Logger LOGGER = Logger.getLogger(CourseDao.class);

    transient private final DataSource dataSource;

    public JDBCCourseDao(final DataSource dataSource) {

        this.dataSource = dataSource;
    }

    @Override
    public Course insert(final Course course) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
             ps.setString(1, course.getCode());
             ps.setString(2, course.getDescription());
             ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys();) {
                if (generatedKeys.next()) {
                    final long id = generatedKeys.getLong(1);
                    return new Course(id, course.getCode(), course.getDescription());
                } else {
                    LOGGER.error(INSERT_ERR_EGT_ID);
                    throw new DaoException(INSERT_ERR_EGT_ID);
                }
            }
        } catch (SQLException e) {
            LOGGER.error(INSERT_ERR, e);
            throw new DaoException(INSERT_ERR, e);
        }
    }


    @Override
    public boolean update(final Course course) {
        try {
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

                ps.setString(1, course.getCode());
                ps.setString(2, course.getDescription());
                ps.setLong(3, course.getTid());
                return ps.executeUpdate() > 0 ? true : false;

            }
        } catch (SQLException e) {
            LOGGER.error(UPDATE_ERR, e);
            throw new DaoException(UPDATE_ERR, e);
        }
    }

    @Override
    public boolean delete(final Course course) {
        try {
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {
                ps.setLong(1, course.getTid());
                return ps.executeUpdate() > 0 ? true : false;
            }
        } catch (SQLException e) {
            LOGGER.error(DELETE_ERR, e);
            throw new DaoException(DELETE_ERR, e);
        }
    }

    @Override
    @SuppressWarnings("PMD.CloseResource")
    public Course selectById(final long tid) {
        try {
            final Connection conn = dataSource.getConnection();
            final PreparedStatement ps = conn.prepareStatement(SELECT_ONE_SQL);
            ps.setLong(1, tid);
            final ResultSet rs = ps.executeQuery();
            try {
                if (rs.next()) {
                    return toCourse(rs);
                } else {
                    return null;
                }
            } finally {
                rs.close();
            }
        } catch (SQLException e) {
            LOGGER.error(FIND_ERR, e);
            throw new DaoException(FIND_ERR, e);
        }
    }

    @Override
    public List<Course> select() {
        try {
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
                 ResultSet rs = ps.executeQuery()) {

                final List<Course> courses = new ArrayList<>();

                while (rs.next()) {
                    courses.add(toCourse(rs));
                }
                return courses;
            }
        } catch (SQLException e) {
            LOGGER.error(FIND_ERR, e);
            throw new DaoException(FIND_ERR, e);
        }
    }


    private Course toCourse(final ResultSet rs) throws SQLException{
        final Course result  = new Course();
        result.setTid(rs.getLong("COURSE_TID"));
        result.setCode(rs.getString("CODE"));
        result.setDescription(rs.getString("DESCRIPTION"));
        return result;
    }

}
