package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.entity.Student;
import com.ra.course.janus.faculty.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JDBCDaoStudentTest {
    private static final String INSERT_SQL = "INSERT INTO STUDENT (CODE,DESCRIPTION) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE STUDENT SET CODE=?,DESCRIPTION=? WHERE ID=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM STUDENT";
    private static final String SELECT_ONE_SQL = "SELECT * FROM STUDENT WHERE ID= ?";
    private static final String DELETE_SQL = "DELETE FROM STUDENT WHERE ID=?";

    private static final long ID = 1;

    private static DataSource mockDataSource =  mock(DataSource.class);
    private static Connection mockConnection =  mock(Connection.class);
    private static PreparedStatement mockPreparedStatement;
    private static ResultSet mockResultSet;
    private static ResultSet mockGeneratedKeys;
    private static JDBCDaoStudent daoStudent;


    @BeforeEach
    public void before() throws SQLException {
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        mockGeneratedKeys =  mock(ResultSet.class);
        daoStudent = new JDBCDaoStudent(mockDataSource);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
    }

    @Test
    public void insertWhenKeysWereNotGenerated () throws SQLException {
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
    public void insert() throws SQLException {
        when(mockConnection.prepareStatement(INSERT_SQL,Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.getLong(1)).thenReturn(1L);
        assertEquals(1, daoStudent.insert(new Student()).getId());
    }

    @Test
    public void insertWhenException() throws SQLException {
        when(mockConnection.prepareStatement(INSERT_SQL,Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException ("Test"));

        assertThrows(DaoException.class, () -> {
            daoStudent.insert(new Student()) ;
        });
    }

    @Test
    public void updateWhenExists() throws SQLException {
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockResultSet.getLong(1)).thenReturn(2L);
        assertTrue(daoStudent.update(new Student(1, "C", "D")));
    }

    @Test
    public void updateWhenNotExists() throws SQLException {
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        when(mockResultSet.getInt(1)).thenReturn(1);
        Student p = new Student();
        p.setId(-1);
        assertFalse(daoStudent.update(p));
    }

    @Test
    public void updateWhenException() throws SQLException {
        when(mockConnection.prepareStatement(INSERT_SQL,Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException ("Test"));

        assertThrows(DaoException.class, () -> {
            daoStudent.insert(new Student()) ;
        });
    }

    @Test
    public void deleteWhenExists() throws SQLException {
        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        assertEquals(true, daoStudent.delete(ID));
    }

    @Test
    public void deleteWhenNotExists() throws SQLException {
        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        assertEquals(false, daoStudent.delete(ID));

    }

    @Test
    public void deleteWhenException() throws SQLException {
        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException ("Test"));
        assertThrows(DaoException.class, () -> {
            daoStudent.delete(ID) ;
        });
    }

    @Test
    public void findByStudentIdWhenFound() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("ID")).thenReturn(1L);
        assertEquals(1, daoStudent.selectById(1).getId());
    }


    @Test
    public void findByStudentIdWhenNotFound() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        assertNull(daoStudent.selectById(1));
    }


    @Test
    public void findByStudentIdWhenException() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException ("Test"));
        assertThrows(DaoException.class, () -> {
            daoStudent.selectById((1)) ;
        });
    }
    @Test
    public void findAll()throws SQLException {
        when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        List<Student> mockList = mock(ArrayList.class);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockList.add(new Student())).thenReturn(true);
        assertNotNull(daoStudent.select());
    }

    @Test
    public void findAllWhenException() throws SQLException {
        when(mockConnection.prepareStatement(INSERT_SQL,Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException ("Test"));

        assertThrows(DaoException.class, () -> {
            daoStudent.insert(new Student()) ;
        });
    }
}
