package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.entity.Course;
import com.ra.course.janus.faculty.exception.DaoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CourseDaoJdbcTest {

    private static final String INSERT_SQL = "INSERT INTO COURSE ( CODE, DESCRIPTION) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE COURSE SET CODE=?,DESCRIPTION=? WHERE COURSE_TID=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM COURSE";
    private static final String SELECT_ONE_SQL = "SELECT * FROM COURSE WHERE COURSE_TID = ?";
    private static final String DELETE_SQL = "DELETE FROM COURSE WHERE COURSE_TID=?";


    //private static Course mockCourse;

    private static DataSource mockDataSource =  mock(DataSource.class);
    private static Connection mockConnection =  mock(Connection.class);
    private static PreparedStatement mockPreparedStatement;
    private static ResultSet mockResultSet;
    private static ResultSet mockGeneratedKeys;
    private static CourseDaoJdbc courseDao;


    @BeforeEach
    public void before()throws SQLException {
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        mockGeneratedKeys =  mock(ResultSet.class);
        courseDao = new CourseDaoJdbc(mockDataSource);

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
            courseDao.insert(new Course()) ;
        });
    }

    @Test
    public void insert() throws SQLException {

        when(mockConnection.prepareStatement(INSERT_SQL,Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.getLong(1)).thenReturn(1L);

        assertEquals(1, courseDao.insert(new Course()).getTid());
    }

    @Test
    public void insertWhenException() throws SQLException {
        when(mockConnection.prepareStatement(INSERT_SQL,Statement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException ("Test"));

        assertThrows(DaoException.class, () -> {
            courseDao.insert(new Course()) ;
        });
    }

    @Test
    public void updateWhenExists() throws SQLException {
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockResultSet.getLong(1)).thenReturn(2L);

        assertTrue(courseDao.update(new Course(1,"C","D")));
    }

    @Test
    public void updateWhenNotExists() throws SQLException {
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        when(mockResultSet.getLong(1)).thenReturn(1L);
        Course m = new Course();
        m.setTid(-1);
        assertTrue(!courseDao.update(m));
    }

    @Test
    public void deleteWhenExists() throws SQLException {
        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        Course m = new Course();
        assertEquals(true, courseDao.delete(m));
    }

    @Test
    public void deleteWhenNotExists() throws SQLException {
        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        Course m = new Course();
        assertEquals(false, courseDao.delete(m));

    }

    @Test
    public void deleteWhenException() throws SQLException {
        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException ("Test"));
        assertThrows(DaoException.class, () -> {
            courseDao.delete(new Course()) ;
        });

    }



    @Test
    public void findByTidWhenFound() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("COURSE_TID")).thenReturn(1L);
        assertEquals(1, courseDao.findByTid(1).getTid());
    }


    @Test
    void findByTidWhenNotFound() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        assertNull(courseDao.findByTid(1));
    }


    @Test
    public void findByTidWhenException() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException ("Test"));
        assertThrows(DaoException.class, () -> {
            courseDao.findByTid(1) ;
        });
    }


    @Test
    public void findAll()throws SQLException {
        when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        List<Course> mockList = Mockito.mock(ArrayList.class);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockList.add(new Course())).thenReturn(true);
        assertNotNull(courseDao.findAll());
    }
}
