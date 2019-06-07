package com.ra.course.janus.faculty.dao;
import com.ra.course.janus.faculty.DaoException;
import com.ra.course.janus.faculty.entity.Student;
import org.apache.log4j.Logger;
import javax.sql.DataSource;
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
transient private final DataSource dataSource;


    public JDBCDaoStudent(final DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public Student insert(Student student) {
        try (Connection conn  = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1,student.getCode());
            ps.setString(2,student.getDescription());
            ps.executeUpdate();
            try(ResultSet studentId = ps.getGeneratedKeys()){
                studentId.next();
                student.setId(studentId.getInt(1));
                return student;
            }
        } catch (SQLException e) {
            LOGGER.error(INSERT_ERR, e);
            throw new DaoException(INSERT_ERR, e);
        }

    }

@Override
@SuppressWarnings("PMD.CloseResource")
    public Student findByStudentId(final int id) {
        try (Connection conn = dataSource.getConnection();
            final PreparedStatement ps = conn.prepareStatement(SELECT_ONE_SQL)) {
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
            LOGGER.error(INSERT_ERR, e);
            throw new DaoException(INSERT_ERR, e);
        }
    }

    @Override
    public List<Student> findAll() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            List<Student> students = new ArrayList<>();
            while (rs.next()) {
                students.add(toStudent(rs));
            }
            return students;
        } catch (SQLException e) {
            LOGGER.error(INSERT_ERR, e);
            throw new DaoException(INSERT_ERR, e);
        }
    }


    @Override
    public boolean update(final Student student) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setString(1,student.getCode());
            ps.setString(2,student.getDescription());
            ps.setInt(1,student.getId());
            return ps.executeUpdate() > 0 ? true : false;
        } catch (SQLException e) {
            LOGGER.error(INSERT_ERR, e);
            throw new DaoException(INSERT_ERR, e);
        }

    }

    @Override
    public boolean delete(Student student) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {
            ps.setInt(1, student.getId());
           return ps.executeUpdate()>0 ? true:false;
        } catch (SQLException e) {
            LOGGER.error(INSERT_ERR, e);
            throw new DaoException(INSERT_ERR, e);
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

