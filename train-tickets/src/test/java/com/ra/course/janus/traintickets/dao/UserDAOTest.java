package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class UserDAOTest {

    private static final String SAVE_USER = "insert into USERS (name,email,password) values (?,?,?)";
    private static final String UPDATE_USER = "update USERS set (name,email,password) values (?,?,?) WHERE id=?";
    private static final String DELETE_USER= "delete from USERS where id=?";
    private static final String FIND_BY_ID = "select * from USERS where id=?";
    private static final String FIND_ALL = "select * from USERS";
    private static final Long   ZERO_ID = 0L;
    private static final Long   USER_ID = 100L;
    private static final String USER_NAME = "test_name";
    private static final String USER_EMAIL = "test_name123@gmail.com";
    private static final String USER_PASSWORD = "password";
    private static final User TEST_USER = new User(USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);

    User user;
    UserDAO userDAO;
    DataSource mockDataSource;
    Connection conn;
    PreparedStatement ps;
    ResultSet rs;

    @BeforeEach
    void setUp() throws SQLException {
        mockDataSource = mock(DataSource.class);
        userDAO = new UserDAO(mockDataSource);
        conn = mock(Connection.class);
        ps = mock(PreparedStatement.class);
        rs = mock(ResultSet.class);
        when(mockDataSource.getConnection()).thenReturn(conn);
    }


    // 1 Happy path with non-null AutoCloseable
    // 2 Happy path with null AutoCloseable
    // 3 Throws on write
    // 4 Throws on close
    // 5 Throws on write and close
    // 6 Throws in resource specification (the with part, e.g. constructor call)
    // 7 Throws in try block but AutoCloseable is null

    // testSave()-------------

    // 1
    @Test
    void saveHappyPathNonNullConn() throws SQLException {
        when(conn.prepareStatement(SAVE_USER)).thenReturn(ps);
        when(ps.getGeneratedKeys()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getLong("id")).thenReturn(USER_ID);
        user = new User(ZERO_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
        user = userDAO.save(user);
        assertTrue(TEST_USER != user);
        assertEquals(TEST_USER, user);
    }

    // 2


    // 3
    @Test
    void saveThrowsOnExecute() throws SQLException {
        user = new User(ZERO_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
        when(conn.prepareStatement(SAVE_USER)).thenReturn(ps);
        when(ps.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(RuntimeException.class, () -> userDAO.save(user));
    }

    // 4
    @Test
    void saveThrowsOnClose() throws SQLException {
        when(conn.prepareStatement(SAVE_USER)).thenReturn(ps);
        when(ps.getGeneratedKeys()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getLong("id")).thenReturn(USER_ID);
        user = new User(ZERO_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
        doThrow(new SQLException()).when(conn).close();
        assertThrows(RuntimeException.class, () -> userDAO.save(user));

    }

    // 5
    @Test
    void saveThrowsOnExecuteAndClose() throws SQLException {
        user = new User(ZERO_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
        when(conn.prepareStatement(SAVE_USER)).thenReturn(ps);
        when(ps.executeUpdate()).thenThrow(SQLException.class);
        doThrow(new SQLException()).when(conn).close();
        assertThrows(RuntimeException.class, () -> userDAO.save(user));
    }

    @Test
    void saveUserFailToGenerateKey() throws SQLException {
        user = new User(ZERO_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
        when(conn.prepareStatement(SAVE_USER)).thenReturn(ps);
        when(ps.getGeneratedKeys()).thenReturn(rs);
        when(rs.next()).thenReturn(false);
        assertNull(userDAO.save(user));
    }

    @Test
    void saveNullUserThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(null));
    }

    // testUpdate--------------
    @Test
    void update() {
    }
    @Test
    void updateNullUserThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(anyLong(), null));
    }

    // testUpdate--------------
//    @Test
//    void update() {
//        assertTrue(userDAO.update(anyLong(), user));
//    }

    @Test
    void delete() {
        assertTrue(userDAO.delete(anyLong()));
    }

    @Test
    void findById() {
        assertNotNull(userDAO.findById(anyLong()));
    }

    @Test
    void findAll() {
        assertNotNull(userDAO.findAll());
    }
}