package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.entity.Admin;
import com.ra.course.janus.traintickets.exception.DAOException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JdbcAdminDaoTest {
    private static final String SAVE_SQL = "INCERT INTO ADMIN (NAME, LASTNAME, PASSWORD) VALUES(?,?,?)";
    private static final String UPDATE_SQL = "UPDATE ADMIN SET NAME=?,LASTNAME=?,PASSWORD=? WHERE ID=?";
    private static final String DELETE_SQL = "DELETE FROM ADMIN WHERE ID=? ";
    private static final String SELECT_ALL = "SELECT * FROM ADMIN";
    private static final String SELECT_BY_ID = "SELECT FROM ADMIN WHERE ID=?";

    private static final long ADMIN_ID = 2L;
    private static final String ADMIN_NAME = "Roman";
    private static final String ADMIN_LASTNAME = "Hreits";
    private static final String PASSWORD = "romanHreits";

    private JdbcAdminDao adminDao;
    private DataSource mockDataSource;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;


    @BeforeEach
    void before() throws SQLException {
        mockDataSource = mock(DataSource.class);
        adminDao = new JdbcAdminDao(mockDataSource);
        mockConnection = mock(Connection.class);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
    }

    @Test
    void whenCallSaveThenReturnNewObjectWithAnotherId() throws SQLException {

        String s = "id";
        Admin admin = new Admin(0, ADMIN_NAME, ADMIN_LASTNAME, PASSWORD);
        when(mockConnection.prepareStatement(SAVE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(s)).thenReturn(ADMIN_ID);
        Admin savedAdmin = adminDao.save(admin);
        assertNotSame(savedAdmin, admin);
    }

    @Test
    void whenInMethodSaveExecuteUpdateNoMOreThanZeroThrowException() throws SQLException {

        Admin admin = new Admin(0, ADMIN_NAME, ADMIN_LASTNAME, PASSWORD);
        when(mockConnection.prepareStatement(SAVE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        assertThrows(DAOException.class, ()->adminDao.save(admin));
    }

    @Test
    void whenInMethodSaveResultSetHaveNoGeneratedKeysAndMethodsCloseThrowExceptions() throws SQLException {

        Admin admin = new Admin(0, ADMIN_NAME, ADMIN_LASTNAME, PASSWORD);
        when(mockConnection.prepareStatement(SAVE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        doThrow(new SQLException()).when(mockResultSet).close();
        doThrow(new SQLException()).when(mockPreparedStatement).close();
        doThrow(new SQLException()).when(mockConnection).close();
        assertThrows(DAOException.class, ()->adminDao.save(admin));
    }
    @Test
    void whenCallMethodUpdateThanReturnTrue() throws SQLException {
        long idTest = 5L;
        Admin admin = new Admin(ADMIN_ID, ADMIN_NAME, ADMIN_LASTNAME, PASSWORD);
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        boolean flag = adminDao.update(idTest, admin);
        assertTrue(flag);

    }
    @Test
    void whenCallMethodUpdateThanReturnFalse() throws SQLException {
        long idTest = 5L;
        Admin admin = new Admin(ADMIN_ID, ADMIN_NAME, ADMIN_LASTNAME, PASSWORD);
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        boolean flag = adminDao.update(idTest, admin);
        assertFalse(flag);
    }
    @Test
    void whenCallUpdateInDbConnectionPrepareStatementAndConnectionCloseThrowException() throws SQLException {
        long idTest = 5L;
        Admin admin = new Admin(ADMIN_ID, ADMIN_NAME, ADMIN_LASTNAME, PASSWORD);
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenThrow(new SQLException());
        doThrow(new SQLException()).when(mockConnection).close();
        assertThrows(DAOException.class, ()->adminDao.update(idTest, admin));
    }

    @Test
    void whenCallDeleteObjectFromDbReturnTrue() throws SQLException {

        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        boolean flag = adminDao.delete(ADMIN_ID);
        assertTrue(flag);
    }

    @Test
    void whenCallDeleteObjectFromDbReturnFalse() throws SQLException {

        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        boolean flag = adminDao.delete(ADMIN_ID);
        assertFalse(flag);
    }



    @Test
    void whenCallDeleteObjectFromDbConnectionPrepareStatementAndConnectionCloseThrowException() throws SQLException {

        when(mockConnection.prepareStatement(DELETE_SQL)).thenThrow(new SQLException());
        doThrow(new SQLException()).when(mockConnection).close();
        assertThrows(DAOException.class, ()-> adminDao.delete(ADMIN_ID));
    }

    @Test
    void whenCallMethodFindByIdThanReturnObject() throws SQLException {

        long idTest = 5L;
        Admin admin = new Admin(idTest, ADMIN_NAME, ADMIN_LASTNAME, PASSWORD);
        when(mockConnection.prepareStatement(SELECT_BY_ID)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("NAME")).thenReturn(ADMIN_NAME);
        when(mockResultSet.getString("LASTNAME")).thenReturn(ADMIN_LASTNAME);
        when(mockResultSet.getString("PASSWORD")).thenReturn(PASSWORD);
        when(mockResultSet.getLong("ID")).thenReturn(idTest);
        Admin foundAdmin = adminDao.findById(idTest);
        assertEquals(admin, foundAdmin);
    }
    @Test
    void whenCallMethodFindByIdThanThrowExceptionsThrowException() throws SQLException {
        long idTest = 5L;
        when(mockConnection.prepareStatement(SELECT_BY_ID)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        doThrow(new SQLException()).when(mockResultSet).close();
        doThrow(new SQLException()).when(mockPreparedStatement).close();
        doThrow(new SQLException()).when(mockConnection).close();
        assertThrows(DAOException.class, ()-> adminDao.findById(idTest));
    }

    @Test
    void whenCallMethodFindAllInDbThenReturnNonEmptyList() throws SQLException {

        when(mockConnection.prepareStatement(SELECT_ALL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getLong("ID")).thenReturn(ADMIN_ID);
        when(mockResultSet.getString("NAME")).thenReturn(ADMIN_NAME);
        when(mockResultSet.getString("LASTNAME")).thenReturn(ADMIN_LASTNAME);
        when(mockResultSet.getString("PASSWORD")).thenReturn(PASSWORD);
        List<Admin> list = adminDao.findAll();
        assertFalse(list.isEmpty());
    }

    @Test
    void whenFindAllInDbThenReturnEmptyList() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_ALL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        List<Admin> list = adminDao.findAll();
        assertEquals(Collections.emptyList(), list);
    }
    @Test
    void whenCallMethodFindAllInDbThenThrowExceptions() throws SQLException {

        when(mockConnection.prepareStatement(SELECT_ALL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getLong("ID")).thenReturn(ADMIN_ID);
        when(mockResultSet.getString("NAME")).thenReturn(ADMIN_NAME);
        when(mockResultSet.getString("LASTNAME")).thenReturn(ADMIN_LASTNAME);
        when(mockResultSet.getString("PASSWORD")).thenReturn(PASSWORD);
        doThrow(new SQLException()).when(mockResultSet).close();
        doThrow(new SQLException()).when(mockPreparedStatement).close();
        doThrow(new SQLException()).when(mockConnection).close();
        assertThrows(DAOException.class, ()-> adminDao.findAll());
    }
}