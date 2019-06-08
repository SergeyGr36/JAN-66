import com.ra.course.janus.faculty.dao.JDBCDaoStudent;
import com.ra.course.janus.faculty.entity.Student;
import com.ra.course.janus.faculty.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JDBCDaoStudentTest  {
    private static final String INSERT_SQL = "INSERT INTO STUDENT (CODE,DESCRIPTION) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE STUDENT SET CODE=?,DESCRIPTION=? WHERE ID=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM STUDENT";
    private static final String SELECT_ONE_SQL = "SELECT * FROM STUDENT WHERE ID= ?";
    private static final String DELETE_SQL = "DELETE FROM STUDENT WHERE ID=?";

 /*   private static final String INSERT_ERR = "Error inserting Student";
    private static final String UPDATE_ERR = "Error updating student";
    private static final String DELETE_ERR = "Error deleting Student";
    private static final String FIND_ERR = "Error finding Student";*/

    private static DataSource mockDataSource =  mock(DataSource.class);
    private static Connection mockConnection =  mock(Connection.class);
    private static PreparedStatement mockPreparedStatement;
    private static ResultSet mockResultSet;
    private static ResultSet mockGeneratedKeys;
    private static Student student;
    private static JDBCDaoStudent daoStudent;




    @BeforeEach
    public void before()throws SQLException {
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        mockGeneratedKeys =  Mockito.mock(ResultSet.class);
        daoStudent = new JDBCDaoStudent(mockDataSource);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
          }


    @Test
    void insertWhenKeysWereNotGenerated () throws SQLException {

        when(mockConnection.prepareStatement(INSERT_SQL,Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn( mockGeneratedKeys);
        when(mockGeneratedKeys.next()).thenReturn(false);

        assertThrows(DaoException.class, () -> {
            daoStudent.insert(new Student()) ;
        });
    }


    @Test
    void insert() throws SQLException {
        when(mockConnection.prepareStatement(INSERT_SQL,Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.getLong(1)).thenReturn(1L);
        assertEquals(1, daoStudent.insert(new Student()).getId());
    }

    @Test
    void insertWhenException() throws SQLException {
        when(mockConnection.prepareStatement(INSERT_SQL,Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException ("Test"));

        assertThrows(DaoException.class, () -> {
            daoStudent.insert(new Student()) ;
        });
    }

    @Test
    void updateWhenExists() throws SQLException {
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockResultSet.getLong(1)).thenReturn(2L);
        assertTrue(daoStudent.update(new Student(1, "C", "D")));
    }

    @Test
    void updateWhenNotExists() throws SQLException {
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        when(mockResultSet.getInt(1)).thenReturn(1);
        Student p = new Student();
        p.setId(-1);
        assertTrue(!daoStudent.update(p));
    }
    @Test
    void updateWhenException() throws SQLException {
        when(mockConnection.prepareStatement(INSERT_SQL,Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException ("Test"));

        assertThrows(DaoException.class, () -> {
            daoStudent.insert(new Student()) ;
        });
    }

    @Test
    void deleteWhenExists() throws SQLException {
        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        Student p = new Student();
        assertEquals(true, daoStudent.delete(p));
    }

    @Test
    void deleteWhenNotExists() throws SQLException {
        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        Student p= new Student();
        assertEquals(false, daoStudent.delete(p));

    }

    @Test
    void deleteWhenException() throws SQLException {
        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException ("Test"));
        assertThrows(DaoException.class, () -> {
            daoStudent.delete(new Student()) ;
        });

    }



    @Test
    void findByStudentIdWhenFound() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("ID")).thenReturn(1L);
        assertEquals(1, daoStudent.findByStudentId(1).getId());
    }


    @Test
    void findByStudentIdWhenNotFound() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        assertNull(daoStudent.findByStudentId(1));
    }


    @Test
    void findByStudentIdWhenException() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException ("Test"));
        assertThrows(DaoException.class, () -> {
            daoStudent.findByStudentId((1)) ;
        });
    }
    @Test
    void findAll()throws SQLException {
        when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        List<Student> mockList = Mockito.mock(ArrayList.class);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockList.add(new Student())).thenReturn(true);
        assertNotNull(daoStudent.findAll());
    }

    @Test
    void findAllWhenException() throws SQLException {
        when(mockConnection.prepareStatement(INSERT_SQL,Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException ("Test"));

        assertThrows(DaoException.class, () -> {
            daoStudent.insert(new Student()) ;
        });
    }
}
