package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    private User user;
    private UserDAO userDAO;
    private Connection mockConn;
    private PreparedStatement mockPrepSt;
    private ResultSet mockResSet;

    @BeforeEach
    void setUp() throws SQLException {
        final DataSource mockDataSource = mock(DataSource.class);
        userDAO = new UserDAO(mockDataSource);
        mockConn = mock(Connection.class);
        mockPrepSt = mock(PreparedStatement.class);
        mockResSet = mock(ResultSet.class);
        when(mockDataSource.getConnection()).thenReturn(mockConn);
    }


    // 1 Happy path with non-null AutoCloseable
    // 2 Happy path with null AutoCloseable
    // 3 Throws on write
    // 4 Throws on close
    // 5 Throws on write and close
    // 6 Throws in resource specification (the with part, e.g. constructor call)
    // 7 Throws in try block but AutoCloseable is null

    // Test save-------------------------------------------------------

    // 1
    @Test
    void saveUserHappyPath() throws SQLException {
        when(mockConn.prepareStatement(SAVE_USER)).thenReturn(mockPrepSt);
        when(mockPrepSt.getGeneratedKeys()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(true);
        when(mockResSet.getLong("id")).thenReturn(USER_ID);
        user = new User(ZERO_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
        user = userDAO.save(user);
        assertNotSame(TEST_USER, user);
        assertEquals(TEST_USER, user);
    }

    // 2


    // 3
    @Test
    void saveUserThrowsOnExecute() throws SQLException {
        user = new User(ZERO_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
        when(mockConn.prepareStatement(SAVE_USER)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeUpdate();
        assertThrows(RuntimeException.class, () -> userDAO.save(user));
    }

    // 4
    @Test
    void saveUserThrowsOnClose() throws SQLException {
        when(mockConn.prepareStatement(SAVE_USER)).thenReturn(mockPrepSt);
        when(mockPrepSt.getGeneratedKeys()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(true);
        when(mockResSet.getLong("id")).thenReturn(USER_ID);
        user = new User(ZERO_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
        doThrow(new SQLException()).when(mockConn).close();
        assertThrows(RuntimeException.class, () -> userDAO.save(user));
    }

    // 5
    @Test
    void saveUserThrowsOnExecuteAndClose() throws SQLException {
        user = new User(ZERO_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
        when(mockConn.prepareStatement(SAVE_USER)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeUpdate();
        doThrow(new SQLException()).when(mockConn).close();
        assertThrows(RuntimeException.class, () -> userDAO.save(user));
    }

    @Test
    void saveUserFailToGenerateKey() throws SQLException {
        user = new User(ZERO_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
        when(mockConn.prepareStatement(SAVE_USER)).thenReturn(mockPrepSt);
        when(mockPrepSt.getGeneratedKeys()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(false);
        assertThrows(RuntimeException.class, () -> userDAO.save(user));
    }

    // Test update-----------------------------------------------------

    @Test
    void updateUserHappyPath() throws SQLException {
        when(mockConn.prepareStatement(UPDATE_USER)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeUpdate()).thenReturn(1);
        user = new User(USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
        assertTrue(userDAO.update(USER_ID, user));
    }

    @Test
    void updateUserNotFound() throws SQLException {
        when(mockConn.prepareStatement(UPDATE_USER)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeUpdate()).thenReturn(0);
        user = new User(USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
        assertFalse(userDAO.update(USER_ID, user));
    }

    @Test
    void updateUserThrowsOnExecute() throws SQLException {
        when(mockConn.prepareStatement(UPDATE_USER)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeUpdate();
        user = new User(USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
        assertThrows(RuntimeException.class, () -> userDAO.update(USER_ID, user));
    }

    @Test
    void updateUserThrowsOnClose() throws SQLException {
        when(mockConn.prepareStatement(UPDATE_USER)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockConn).close();
        user = new User(USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
        assertThrows(RuntimeException.class, () -> userDAO.update(USER_ID, user));
    }


    @Test
    void updateUserThrowsOnExecuteAndClose() throws SQLException {
        when(mockConn.prepareStatement(UPDATE_USER)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeUpdate();
        doThrow(new SQLException()).when(mockConn).close();
        user = new User(USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
        assertThrows(RuntimeException.class, () -> userDAO.update(USER_ID, user));
    }

    // Test delete-----------------------------------------------------

    @Test
    void deleteUserHappyPath() throws SQLException {
        when(mockConn.prepareStatement(DELETE_USER)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeUpdate()).thenReturn(1);
        assertTrue(userDAO.delete(USER_ID));
    }

    @Test
    void deleteUserNotFound() throws SQLException {
        when(mockConn.prepareStatement(DELETE_USER)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeUpdate()).thenReturn(0);
        assertFalse(userDAO.delete(USER_ID));
    }


    @Test
    void deleteUserThrowsOnExecute() throws SQLException {
        when(mockConn.prepareStatement(DELETE_USER)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeUpdate();
        assertThrows(RuntimeException.class, () -> userDAO.delete(USER_ID));
    }

    @Test
    void deleteUserThrowsOnExecuteAndClose() throws SQLException {
        when(mockConn.prepareStatement(DELETE_USER)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeUpdate();
        doThrow(new SQLException()).when(mockConn).close();
        assertThrows(RuntimeException.class, () -> userDAO.delete(USER_ID));
    }

    // Test findById-------------------------------------------------------

    @Test
    void findUserByIdHappyPath() throws SQLException {
        when(mockConn.prepareStatement(FIND_BY_ID)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeQuery()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(true);
        mockMapUser(mockResSet);
        user = userDAO.findById(USER_ID);
        assertEquals(TEST_USER, user);
    }

    @Test
    void findUserByIdNotFoundReturnNull() throws SQLException {
        when(mockConn.prepareStatement(FIND_BY_ID)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeQuery()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(false);
        mockMapUser(mockResSet);
        user = userDAO.findById(USER_ID);
        assertNull(user);
    }

    @Test
    void findUserByIdHappyPathThrowsOnResSetClose() throws SQLException {
        when(mockConn.prepareStatement(FIND_BY_ID)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeQuery()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(true);
        mockMapUser(mockResSet);
        doThrow(new SQLException()).when(mockResSet).close();
        assertThrows(RuntimeException.class, () -> userDAO.findById(USER_ID));
    }

    @Test
    void findUserByIdThrowsOnExecute() throws SQLException {
        when(mockConn.prepareStatement(FIND_BY_ID)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeUpdate();
        assertThrows(RuntimeException.class, () -> userDAO.findById(USER_ID));
    }

    @Test
    void findUserByIdHappyPathThrowsOnConnClose() throws SQLException {
        when(mockConn.prepareStatement(FIND_BY_ID)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeQuery()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(true);
        mockMapUser(mockResSet);
        doThrow(new SQLException()).when(mockConn).close();
        assertThrows(RuntimeException.class, () -> userDAO.findById(USER_ID));
    }

    // Test findAll----------------------------------------------------

    @Test
    void findAllHappyPath() throws SQLException {
        when(mockConn.prepareStatement(FIND_ALL)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeQuery()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(true).thenReturn(false);
        when(mockResSet.getLong("id")).thenReturn(USER_ID);
        when(mockResSet.getString("name")).thenReturn(USER_NAME);
        when(mockResSet.getString("email")).thenReturn(USER_EMAIL);
        when(mockResSet.getString("password")).thenReturn(USER_PASSWORD);
        List<User> users = userDAO.findAll();
        assertTrue(users.size() == 1);
        assertTrue(users.contains(TEST_USER));
    }

    private void mockMapUser(ResultSet mockRS) throws SQLException {
        when(mockRS.getLong("id")).thenReturn(USER_ID);
        when(mockRS.getString("name")).thenReturn(USER_NAME);
        when(mockRS.getString("email")).thenReturn(USER_EMAIL);
        when(mockRS.getString("password")).thenReturn(USER_PASSWORD);
    }
}