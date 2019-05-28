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
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

    // Test save-------------------------------------------------------

    @Test
    void saveWhenUserSavedOkThenNewUserWithGeneratedIdReturned() throws SQLException {
        when(mockConn.prepareStatement(SAVE_USER)).thenReturn(mockPrepSt);
        when(mockPrepSt.getGeneratedKeys()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(true);
        when(mockResSet.getLong("id")).thenReturn(USER_ID);
        user = new User(ZERO_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
        user = userDAO.save(user);
        assertNotSame(TEST_USER, user);
        assertEquals(TEST_USER, user);
    }

    @Test
    void saveUserWhenThrowsSQLExceptionOnExecuteUpdateThenThrowsRuntimeException() throws SQLException {
        user = new User(ZERO_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
        when(mockConn.prepareStatement(SAVE_USER)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeUpdate();
        assertThrows(RuntimeException.class, () -> userDAO.save(user));
    }

    @Test
    void saveUserWhenFailToReadGeneratedKeyThenThrowsException() throws SQLException {
        user = new User(ZERO_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
        when(mockConn.prepareStatement(SAVE_USER)).thenReturn(mockPrepSt);
        when(mockPrepSt.getGeneratedKeys()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(false);
        assertThrows(RuntimeException.class, () -> userDAO.save(user));
    }

    // Test update-----------------------------------------------------

    @Test
    void updateWhenUserWithProvidedIdUpdateOkThenReturnTrue() throws SQLException {
        when(mockConn.prepareStatement(UPDATE_USER)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeUpdate()).thenReturn(1);
        user = new User(USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
        assertTrue(userDAO.update(USER_ID, user));
    }

    @Test
    void updateWhenUserWithProvidedIdNotFoundThenReturnFalse() throws SQLException {
        when(mockConn.prepareStatement(UPDATE_USER)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeUpdate()).thenReturn(0);
        user = new User(USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
        assertFalse(userDAO.update(USER_ID, user));
    }

    @Test
    void updateUserWhenThrowsOnExecuteStmtThenThrowsRuntimeException() throws SQLException {
        when(mockConn.prepareStatement(UPDATE_USER)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeUpdate();
        user = new User(USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
        assertThrows(RuntimeException.class, () -> userDAO.update(USER_ID, user));
    }

    @Test
    void updateUserWhenThrowsOnConnCloseThenThrowsRuntimeException() throws SQLException {
        when(mockConn.prepareStatement(UPDATE_USER)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockConn).close();
        user = new User(USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
        assertThrows(RuntimeException.class, () -> userDAO.update(USER_ID, user));
    }


    @Test
    void updateUserWhenThrowsOnExequteStmtAndConnCloseThenThrowsRuntimeException() throws SQLException {
        when(mockConn.prepareStatement(UPDATE_USER)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeUpdate();
        doThrow(new SQLException()).when(mockConn).close();
        user = new User(USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
        assertThrows(RuntimeException.class, () -> userDAO.update(USER_ID, user));
    }

    // Test delete-----------------------------------------------------

    @Test
    void deleteWhenUserWithProvidedIdDeleteOkThenReturnTrue() throws SQLException {
        when(mockConn.prepareStatement(DELETE_USER)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeUpdate()).thenReturn(1);
        assertTrue(userDAO.delete(USER_ID));
    }

    @Test
    void deleteWhenUserWithProvidedIdNotFoundThenReturnFalse() throws SQLException {
        when(mockConn.prepareStatement(DELETE_USER)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeUpdate()).thenReturn(0);
        assertFalse(userDAO.delete(USER_ID));
    }


    @Test
    void deleteUserWhenThrowsOnExequteStmtThenThrowsRuntimeException() throws SQLException {
        when(mockConn.prepareStatement(DELETE_USER)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeUpdate();
        assertThrows(RuntimeException.class, () -> userDAO.delete(USER_ID));
    }

    @Test
    void deleteUserWhenThrowsOnExequteStmtAndConnCloseThenThrowsRuntimeException() throws SQLException {
        when(mockConn.prepareStatement(DELETE_USER)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeUpdate();
        doThrow(new SQLException()).when(mockConn).close();
        assertThrows(RuntimeException.class, () -> userDAO.delete(USER_ID));
    }

    // Test findById-------------------------------------------------------

    @Test
    void findByIdWhenUserWithProvidedIdFoundOkThenReturnUser() throws SQLException {
        when(mockConn.prepareStatement(FIND_BY_ID)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeQuery()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(true);
        mockMapUser(mockResSet);
        user = userDAO.findById(USER_ID);
        assertEquals(TEST_USER, user);
    }

    @Test
    void findByIdWhenUserWithProvidedIdNotFoundThenReturnNull() throws SQLException {
        when(mockConn.prepareStatement(FIND_BY_ID)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeQuery()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(false);
        mockMapUser(mockResSet);
        user = userDAO.findById(USER_ID);
        assertNull(user);
    }

    @Test
    void findUserByIdWhenThrowsOnResSetCloseThenThrowsRuntimeException() throws SQLException {
        when(mockConn.prepareStatement(FIND_BY_ID)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeQuery()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(true);
        mockMapUser(mockResSet);
        doThrow(new SQLException()).when(mockResSet).close();
        assertThrows(RuntimeException.class, () -> userDAO.findById(USER_ID));
    }

    @Test
    void findUserByIdWhenThrowsOnExecuteStmtThenThrowsRuntimeException() throws SQLException {
        when(mockConn.prepareStatement(FIND_BY_ID)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeQuery();
        assertThrows(RuntimeException.class, () -> userDAO.findById(USER_ID));
    }

    @Test
    void findUserByIdWhenThrowsOnExecuteStmtAndConnCloseThenThrowsRuntimeException() throws SQLException {
        when(mockConn.prepareStatement(FIND_BY_ID)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).setLong(anyInt(), anyLong());
        doThrow(new SQLException()).when(mockConn).close();
        assertThrows(RuntimeException.class, () -> userDAO.findById(USER_ID));
    }

    @Test
    void findUserByIdWhenThrowsOnConnCloseThenThrowsRuntimeException() throws SQLException {
        when(mockConn.prepareStatement(FIND_BY_ID)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeQuery()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(true);
        mockMapUser(mockResSet);
        doThrow(new SQLException()).when(mockConn).close();
        assertThrows(RuntimeException.class, () -> userDAO.findById(USER_ID));
    }

    @Test
    void findUserByIdWhenThrowsOnResSetAndConnCloseThenThrowsRuntimeException() throws SQLException {
        when(mockConn.prepareStatement(FIND_BY_ID)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeQuery()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(true);
        mockMapUser(mockResSet);
        doThrow(new SQLException()).when(mockResSet).close();
        doThrow(new SQLException()).when(mockConn).close();
        assertThrows(RuntimeException.class, () -> userDAO.findById(USER_ID));
    }

    @Test
    public void findUserByIdWhenOkDoCloseResources() throws SQLException {
        when(mockConn.prepareStatement(FIND_BY_ID)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeQuery()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(true);
        mockMapUser(mockResSet);
        user = userDAO.findById(USER_ID);
        verify(mockResSet, times(1)).close();
        verify(mockPrepSt, times(1)).close();
        verify(mockConn, times(1)).close();
    }

    @Test
    public void findUserByIdWhenThrowsSQLExceptionDoClosesResources() throws SQLException {
        when(mockConn.prepareStatement(FIND_BY_ID)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeQuery();
        try {
            userDAO.findById(USER_ID);
            verify(mockResSet, times(1)).close();
            verify(mockPrepSt, times(1)).close();
            verify(mockConn, times(1)).close();
        } catch (RuntimeException e) {
        }
    }

    // Test findAll----------------------------------------------------

    @Test
    void findAllWhenFoundOneUserThenReturnsListWithOneUser() throws SQLException {
        when(mockConn.prepareStatement(FIND_ALL)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeQuery()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(true).thenReturn(false);
        mockMapUser(mockResSet);
        List<User> users = userDAO.findAll();
        assertTrue(users.size() == 1);
        assertTrue(users.contains(TEST_USER));
    }

    @Test
    void findAllWhenNoUsersFoundThenReturnsEmptyList() throws SQLException {
        when(mockConn.prepareStatement(FIND_ALL)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeQuery()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(false);
        List<User> users = userDAO.findAll();
        assertTrue(users.size() == 0);
    }

    @Test
    void findAllWhenThrowsOnConnCloseThenThrowsRuntimeException() throws SQLException {
        when(mockConn.prepareStatement(FIND_ALL)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeQuery()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(true).thenReturn(false);
        mockMapUser(mockResSet);
        doThrow(new SQLException()).when(mockConn).close();
        assertThrows(RuntimeException.class, () -> userDAO.findAll());
    }


    @Test
    public void findAllWhenThrowsOnExecuteQueryThenThrowsRuntimeException() throws SQLException {
        when(mockConn.prepareStatement(FIND_ALL)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeQuery();
        assertThrows(RuntimeException.class, () ->  userDAO.findAll());
    }

    @Test
    public void findAllWhenThrowsOnResSetNextThenThrowsRuntimeException() throws SQLException {
        when(mockConn.prepareStatement(FIND_ALL)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeQuery()).thenReturn(mockResSet);
        doThrow(new SQLException()).when(mockResSet).next();
        assertThrows(RuntimeException.class, () -> userDAO.findAll());
    }

    @Test
    public void findAllWhenThrowsOnResSetNextAndConnCloseThenThrowsRuntimeException() throws SQLException {
        when(mockConn.prepareStatement(FIND_ALL)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeQuery()).thenReturn(mockResSet);
        doThrow(new SQLException()).when(mockResSet).next();
        doThrow(new SQLException()).when(mockConn).close();
        assertThrows(RuntimeException.class, () -> userDAO.findAll());
    }

    //-----------------------------------------------------------------

    private void mockMapUser(ResultSet mockRS) throws SQLException {
        when(mockRS.getLong("id")).thenReturn(USER_ID);
        when(mockRS.getString("name")).thenReturn(USER_NAME);
        when(mockRS.getString("email")).thenReturn(USER_EMAIL);
        when(mockRS.getString("password")).thenReturn(USER_PASSWORD);
    }
}