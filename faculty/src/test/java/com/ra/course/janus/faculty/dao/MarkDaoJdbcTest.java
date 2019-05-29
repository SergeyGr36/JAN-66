package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.entity.Mark;
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
import org.apache.log4j.Logger;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class MarkDaoJdbcTest {

    private static final String INSERT_SQL = "INSERT INTO MARK ( SCORE, REFERENCE) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE MARK SET SCORE=?,REFERENCE=? WHERE MARK_TID=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM MARK";
    private static final String SELECT_ONE_SQL = "SELECT * FROM MARK WHERE MARK_TID = ?";
    private static final String DELETE_SQL = "DELETE FROM MARK WHERE MARK_TID=?";

    private static Mark mockMark;
    private static MarkDaoJdbc markDao;
    private DataSource mockDataSource;
    private static Connection mockConnection;
    private static PreparedStatement mockStatement;
    private static ResultSet mockResultSet;
    private static Mark mark;
    private static Logger mockLogger;


    @BeforeEach
    public void before()throws SQLException {

       // mockDataSource = Mockito.mock(DataSource.class);
        mockConnection = Mockito.mock(Connection.class);
        markDao = new MarkDaoJdbc(mockConnection);
        mockStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        mockLogger = Mockito.mock(Logger.class);
    }

    @Test
    void insert() throws SQLException {
        when(mockConnection.prepareStatement(INSERT_SQL,Statement.RETURN_GENERATED_KEYS)).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.getLong("MARK_TID")).thenReturn(1L);
        when(mockResultSet.getLong(1)).thenReturn(1L);
        assertEquals(1, markDao.insert(new Mark()).getTid());
    }

    @Test
    void insertWhenException() throws SQLException {
        when(mockConnection.prepareStatement(INSERT_SQL,Statement.RETURN_GENERATED_KEYS)).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenThrow(new SQLException ("Test"));
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.getLong("MARK_TID")).thenReturn(1L);
        when(mockResultSet.getLong(1)).thenReturn(1L);
        // when(mockLogger.getLevel()).thenReturn(Level.ALL);
        assertThrows(DaoException.class, () -> {
            markDao.insert(new Mark()) ;
        });
    }


    @Test
    void updateWhenExists() throws SQLException {
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);
        when(mockResultSet.getLong(1)).thenReturn(2L);
        Mark m = new Mark();
        m.setTid(2);
        assertEquals(2, markDao.update(m).getTid());
    }

    @Test
    void updateWhenNotExists() throws SQLException {
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(0);
        when(mockResultSet.getLong(1)).thenReturn(1L);
        Mark m = new Mark();
        m.setTid(2);
        assertNull(markDao.update(m));
    }

    @Test
    void deleteWhenExists() throws SQLException {
        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);
        Mark m = new Mark();
        assertEquals(true, markDao.delete(m));
    }

    @Test
    void deleteWhenNotExists() throws SQLException {
        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(0);
        Mark m = new Mark();
        assertEquals(false, markDao.delete(m));

    }


    @Test
    void findByTidWhenFound() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("MARK_TID")).thenReturn(1L);
        assertEquals(1, markDao.findByTid(1).getTid());
    }


    @Test
    void findByTidWhenNotFound() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        assertNull(markDao.findByTid(1));
    }


    @Test
    void findByTidWhenException() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenThrow(new SQLException ("Test"));
       // when(mockLogger.getLevel()).thenReturn(Level.ALL);
        assertThrows(SQLException.class, () -> {
            markDao.findByTid(1) ;
        });
    }

        @Test
    void findAll()throws SQLException {
        when(mockConnection.prepareStatement(SELECT_ALL_SQL)).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        List<Mark> mockList = Mockito.mock(ArrayList.class);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockList.add(mockMark)).thenReturn(true);
        assertNotNull(markDao.findAll());
    }
}