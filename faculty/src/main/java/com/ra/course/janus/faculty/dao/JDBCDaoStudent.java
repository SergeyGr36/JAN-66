package com.ra.course.janus.faculty.dao;
import com.ra.course.janus.faculty.entity.Student;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class JDBCDaoStudent implements DaoStudent {
    private static final String INSERT_SQL = "INSERT INTO STUDENT (NAME,SURNAME,ID) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE STUDENT SET NAME=?,SURNAME=?,WHERE ID=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM STUDENT";
    private static final String SELECT_ONE_SQL = "SELECT * FROM STUDENT WHERE ID= ?";
    private static final String DELETE_SQL = "DELETE FROM STUDENT WHERE ID=?";
    private static final String INSERT_ERR = "Error inserting student";
    private static final String UPDATE_ERR = "Error updating student";
    private static final String DELETE_ERR = "Error deleting student";
    private static final String FIND_ERR = "Error finding student";


    private final static Logger LOGGER = Logger.getLogger(DaoStudent.class);
 private  transient final Connection connection;
 public JDBCDaoStudent(final Connection connection){
     this.connection = connection;
 }

    @Override
    public Student insert(final Student student) {
     try{
         try (PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)){
                 ps.setString(1, student.getCode());
                 ps.setString(2, student.getDescription());
                 ps.executeUpdate();
                 try (ResultSet studentId = ps.getGeneratedKeys()) {
                     studentId.next();
                     student.setId(studentId.getInt(1));
                     return student;
                 }
             }
        } catch (SQLException e) {
            LOGGER.error(INSERT_ERR, e);
            throw new DaoException(INSERT_ERR, e);
        }
    }

@Override
/*@SuppressWarnings("PMD.CloseResource")*/
    public Student findByStudentId(final int id) {
        try{
            final PreparedStatement ps = connection.prepareStatement(SELECT_ONE_SQL);
            ps.setInt(1, id);
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
        try (PreparedStatement ps = connection.prepareStatement(SELECT_ALL_SQL);
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


    @Override
    public Student update(final Student student) {
     try{
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, student.getCode());
            ps.setString(2, student.getDescription());
            ps.setInt(1, student.getId());
            return ps.executeUpdate() > 0 ? student : null;
        }
        } catch (SQLException e) {
            LOGGER.error(UPDATE_ERR, e);
            throw new DaoException(UPDATE_ERR, e);
        }
    }

    @Override
    public boolean delete(final Student student) {
     try{
        try (PreparedStatement ps = connection.prepareStatement(DELETE_SQL)) {
            ps.setInt(1, student.getId());
            return ps.executeUpdate() > 0 ? true : false;
        }
        } catch (SQLException e) {
            LOGGER.error(DELETE_ERR, e);
            throw new DaoException(DELETE_ERR, e);
        }
    }

    private Student toStudent (final ResultSet rs) throws SQLException{
        final Student result = new Student();
        result.setId(rs.getInt("STUDENT_ID"));
        result.setCode(rs.getString("CODE"));
        result.setDescription(rs.getString("DESCRIPTION"));
        return result;
    }
}

