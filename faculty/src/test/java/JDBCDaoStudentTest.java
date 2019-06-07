import com.ra.course.janus.faculty.DaoException;
import com.ra.course.janus.faculty.dao.JDBCDaoStudent;
import com.ra.course.janus.faculty.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JDBCDaoStudentTest  {
    private static final String INSERT_SQL = "INSERT INTO STUDENT ( CODE, DESCRIPTION) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE STUDENT SET CODE=?,DESCRIPTION=? WHERE COURSE_TID=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM STUDENT";
    private static final String SELECT_ONE_SQL = "SELECT * FROM STUDENT WHERE STUDENT_ID = ?";
    private static final String DELETE_SQL = "DELETE FROM STUDENT WHERE STUDENT_ID=?";

    private static Student mockStudent;
    private static JDBCDaoStudent studentDao;
    private static DataSource mockDataSource;
    private static Connection mockConnection;
    private static PreparedStatement mockStatement;
    private static PreparedStatement mockPreparedStatement;
    private static ResultSet mockResultSet;




    @BeforeEach
    public void before()throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        studentDao = new JDBCDaoStudent(mockConnection);
        mockStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);
          }


    @Test
    void insert() throws SQLException {
        when(mockConnection.prepareStatement(INSERT_SQL,Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.getInt(1)).thenReturn(1);
        assertEquals(1, studentDao.insert(new Student()).getId());
    }

    @Test
    void insertWhenException() throws SQLException {
        when(mockConnection.prepareStatement(INSERT_SQL,Statement.RETURN_GENERATED_KEYS)).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenThrow(new SQLException ("Test"));
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.getInt("STUDENT_ID")).thenReturn(1);
        when(mockResultSet.getInt(1)).thenReturn(1);
        assertThrows(DaoException.class, () -> {
            studentDao.insert(new Student()) ;
        });
    }

    @Test
    void updateWhenExists() throws SQLException {
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);
        when(mockResultSet.getInt(1)).thenReturn(2);
        Student p = new Student();
        p.setId(2);
        assertEquals(2, p.getId());
    }

    @Test
    void updateWhenNotExists() throws SQLException {
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(0);
        when(mockResultSet.getInt(1)).thenReturn(1);
        Student p = new Student();
        p.setId(2);
        assertNull(studentDao.update(p));
    }

    @Test
    void deleteWhenExists() throws SQLException {
        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);
        Student p = new Student();
        assertEquals(true, studentDao.delete(p));
    }

    @Test
    void deleteWhenNotExists() throws SQLException {
        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(0);
        Student p= new Student();
        assertEquals(false, studentDao.delete(p));

    }

    @Test
    void deleteWhenException() throws SQLException {
        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenThrow(new SQLException ("Test"));
        assertThrows(DaoException.class, () -> {
            studentDao.delete(new Student()) ;
        });

    }



    @Test
    void findByStudentIdWhenFound() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("STUDENT_ID")).thenReturn(1);
        assertEquals(1, studentDao.findByStudentId(1).getId());
    }


    @Test
    void findByStudentIdWhenNotFound() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        assertNull(studentDao.findByStudentId(1));
    }


    @Test
    void findByStudentIdWhenException() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenThrow(new SQLException ("Test"));
        assertThrows(DaoException.class, () -> {
            studentDao.findByStudentId((1)) ;
        });
    }
    @Test
    void findAll()throws SQLException {
        when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        List<Student> mockList = Mockito.mock(ArrayList.class);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockList.add(mockStudent)).thenReturn(true);
        assertNotNull(studentDao.findAll());
    }
}
