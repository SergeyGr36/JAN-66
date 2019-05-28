package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.entity.Course;

import java.sql.*;
import java.util.List;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class CourseDaoJdbc implements CourseDao {

    private static final String INSERT_SQL = "INSERT INTO COURSE ( CODE, DESCRIPTION) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE COURSE SET CODE=?,DESCRIPTION=? WHERE COURSE_TID=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM COURSE";
    private static final String SELECT_ONE_SQL = "SELECT * FROM COURSE WHERE COURSE_TID = ?";
    private static final String DELETE_SQL = "DELETE FROM COURSE WHERE COURSE_TID=?";

    private final static Logger LOGGER = Logger.getLogger(CourseDao.class);


    private transient final Connection connection;

    public CourseDaoJdbc(final Connection connection) {
        this.connection = connection;
    }

    @Override
    public Course insert(final Course course) throws SQLException {
        LOGGER.debug("Going to insert");
        try (PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, course.getCode());
            ps.setString(2, course.getDescription());
            ps.executeUpdate();

            try (ResultSet courseTid = ps.getGeneratedKeys()) {
                courseTid.next();
                course.setTid(courseTid.getLong(1));
                return course;
            }
        }
    }

    @Override
    public Course update(final Course course) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, course.getCode());
            ps.setString(2, course.getDescription());
            ps.setLong(3, course.getTid());
            return ps.executeUpdate() > 0 ? course : null;

        }

    }

    @Override
    public boolean delete(final Course course) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(DELETE_SQL)) {
            ps.setLong(1, course.getTid());
            return ps.executeUpdate() > 0 ? true : false;
        }
    }

   /* @Override
    public Course findByTidld(final long tid) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_ONE_SQL)) {
            ps.setLong(1, tid);
            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return toCourse(rs);
                } else {
                    return null;
                }
            }
        }
    }
*/
    @Override
    public Course findByTid(final long tid) throws SQLException {
        try  {
            final PreparedStatement ps = connection.prepareStatement(SELECT_ONE_SQL);
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
           // if (LOGGER.isEnabledFor(LOGGER.getLevel())) {

                LOGGER.debug("SQL Exception during findByTid - ");
            //}
            throw e;
        }

    }

    @Override
    public List<Course> findAll() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            final List<Course> courses = new ArrayList<>();

            while (rs.next()) {
                courses.add(toCourse(rs));
            }
            return courses;
        }
    }


    private Course toCourse(final ResultSet rs) throws SQLException {
        final Course result  = new Course();
        result.setTid(rs.getLong("COURSE_TID"));
        result.setCode(rs.getString("CODE"));
        result.setDescription(rs.getString("DESCRIPTION"));
        return result;
    }

}
