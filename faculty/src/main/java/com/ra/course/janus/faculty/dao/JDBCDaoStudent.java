package com.ra.course.janus.faculty.dao;
import com.ra.course.janus.faculty.entity.Student;
import com.ra.course.janus.faculty.exception.DaoException;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class JDBCDaoStudent implements DaoStudent {
    private static final String INSERT_SQL = "INSERT INTO STUDENT (CODE,DESCRIPTION) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE STUDENT SET CODE=?,DESCRIPTION=?,WHERE STUDENT_ID=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM STUDENT";
    private static final String SELECT_ONE_SQL = "SELECT * FROM STUDENT WHERE ID= ?";
    private static final String DELETE_SQL = "DELETE FROM STUDENT WHERE ID=?";

    private static final String INSERT_ERR = "Error inserting Student";
    private static final String INSERT_ERR_EGT_ID = "Error inserting Student - could not get ID";
    private static final String UPDATE_ERR = "Error updating student";
    private static final String DELETE_ERR = "Error deleting Student";
    private static final String FIND_ERR = "Error finding Student";


    private final static Logger LOGGER = Logger.getLogger(DaoStudent.class);
    transient private final DataSource dataSource;
    public JDBCDaoStudent(final DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public Student insert(final Student student) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, student.getCode());
            ps.setString(2, student.getDescription());
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys();) {
                if (generatedKeys.next()) {
                    final long id = generatedKeys.getLong(1);
                    return new Student(id, student.getCode(), student.getDescription());
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
    public boolean update(final Student student) {
        try{
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

                ps.setString(1, student.getCode());
                ps.setString(2, student.getDescription());
                ps.setLong(1, student.getId());
                return ps.executeUpdate() > 0 ? true : false;
            }
        } catch (SQLException e) {
            LOGGER.error(UPDATE_ERR, e);
            throw new DaoException(UPDATE_ERR, e);
        }
    }
    @Override
    public boolean delete(final Student student) {
        try{
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {
                ps.setLong(1, student.getId());
                return ps.executeUpdate() > 0 ? true : false;
            }

        } catch (SQLException e) {
            LOGGER.error(DELETE_ERR, e);
            throw new DaoException(DELETE_ERR, e);
        }
    }
@Override
@SuppressWarnings("PMD.CloseResource")
    public Student findByStudentId(final long id) {
        try{
            final Connection conn = dataSource.getConnection();
            final PreparedStatement ps = conn.prepareStatement(SELECT_ONE_SQL);
            ps.setLong(1, id);
            final ResultSet rs = ps.executeQuery();
            try{
                if (rs.next()) {
                    return toStudent(rs);
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
    public List<Student> findAll() {
        try{
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {


            final List<Student> students = new ArrayList<>();
            while (rs.next()) {
                students.add(toStudent(rs));
            }
            return students;
        }
        } catch (SQLException e) {
            LOGGER.error(FIND_ERR, e);
            throw new DaoException(FIND_ERR, e);
        }
    }


    private Student toStudent (final ResultSet rs) throws SQLException{
        final Student result = new Student();
        result.setId(rs.getLong("STUDENT_ID"));
        result.setCode(rs.getString("CODE"));
        result.setDescription(rs.getString("DESCRIPTION"));
        return result;
    }
}

