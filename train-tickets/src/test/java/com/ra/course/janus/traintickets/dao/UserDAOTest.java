package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.entity.User;
import com.ra.course.janus.traintickets.exception.DAOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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
import static org.mockito.Mockito.doAnswer;
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
    void saveUserWhenThrowsSQLExceptionOnExecuteUpdateThenThrowsDAOException() throws SQLException {
        user = new User(ZERO_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
        when(mockConn.prepareStatement(SAVE_USER)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeUpdate();
        assertThrows(DAOException.class, () -> userDAO.save(user));
    }

    @Test
    void saveUserWhenFailToReadGeneratedKeyThenThrowsDAOException() throws SQLException {
        user = new User(ZERO_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
        when(mockConn.prepareStatement(SAVE_USER)).thenReturn(mockPrepSt);
        when(mockPrepSt.getGeneratedKeys()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(false);
        assertThrows(DAOException.class, () -> userDAO.save(user));
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
    void updateUserWhenThrowsOnExecuteStmtThenThrowsDAOException() throws SQLException {
        when(mockConn.prepareStatement(UPDATE_USER)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeUpdate();
        user = new User(USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
        assertThrows(DAOException.class, () -> userDAO.update(USER_ID, user));
    }

    @Test
    void updateUserWhenThrowsOnConnCloseThenThrowsDAOException() throws SQLException {
        when(mockConn.prepareStatement(UPDATE_USER)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockConn).close();
        user = new User(USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
        assertThrows(DAOException.class, () -> userDAO.update(USER_ID, user));
    }


    @Test
    void updateUserWhenThrowsOnExequteStmtAndConnCloseThenThrowsDAOException() throws SQLException {
        when(mockConn.prepareStatement(UPDATE_USER)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeUpdate();
        doThrow(new SQLException()).when(mockConn).close();
        user = new User(USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);
        assertThrows(DAOException.class, () -> userDAO.update(USER_ID, user));
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
    void deleteUserWhenThrowsOnExequteStmtThenThrowsDAOException() throws SQLException {
        when(mockConn.prepareStatement(DELETE_USER)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeUpdate();
        assertThrows(DAOException.class, () -> userDAO.delete(USER_ID));
    }

    @Test
    void deleteUserWhenThrowsOnExequteStmtAndConnCloseThenThrowsDAOException() throws SQLException {
        when(mockConn.prepareStatement(DELETE_USER)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeUpdate();
        doThrow(new SQLException()).when(mockConn).close();
        assertThrows(DAOException.class, () -> userDAO.delete(USER_ID));
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
    void findUserByIdWhenThrowsOnResSetCloseThenThrowsDAOException() throws SQLException {
        when(mockConn.prepareStatement(FIND_BY_ID)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeQuery()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(true);
        mockMapUser(mockResSet);
        doThrow(new SQLException()).when(mockResSet).close();
        assertThrows(DAOException.class, () -> userDAO.findById(USER_ID));
    }

    @Test
    void findUserByIdWhenThrowsOnResSetNextThenThrowsDAOException() throws SQLException {
        when(mockConn.prepareStatement(FIND_BY_ID)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeQuery()).thenReturn(mockResSet);
        doThrow(new SQLException()).when(mockResSet).next();
        assertThrows(DAOException.class, () -> userDAO.findById(USER_ID));
    }

    @Test
    void findUserByIdWhenThrowsOnResSetNextAndCloseThenThrowsDAOException() throws SQLException {
        when(mockConn.prepareStatement(FIND_BY_ID)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeQuery()).thenReturn(mockResSet);
        doThrow(new SQLException()).when(mockResSet).next();
        doThrow(new SQLException()).when(mockResSet).close();
        assertThrows(DAOException.class, () -> userDAO.findById(USER_ID));
    }

    @Test
    void findUserByIdWhenThrowsOnExecuteStmtThenThrowsDAOException() throws SQLException {
        when(mockConn.prepareStatement(FIND_BY_ID)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeQuery();
        assertThrows(DAOException.class, () -> userDAO.findById(USER_ID));
    }

    @Test
    void findUserByIdWhenThrowsOnExecuteStmtAndConnCloseThenThrowsDAOException() throws SQLException {
        when(mockConn.prepareStatement(FIND_BY_ID)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).setLong(anyInt(), anyLong());
        doThrow(new SQLException()).when(mockConn).close();
        assertThrows(DAOException.class, () -> userDAO.findById(USER_ID));
    }

    @Test
    void findUserByIdWhenThrowsOnConnCloseThenThrowsDAOException() throws SQLException {
        when(mockConn.prepareStatement(FIND_BY_ID)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeQuery()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(true);
        mockMapUser(mockResSet);
        doThrow(new SQLException()).when(mockConn).close();
        assertThrows(DAOException.class, () -> userDAO.findById(USER_ID));
    }

    @Test
    void findUserByIdWhenThrowsOnResSetAndConnCloseThenThrowsDAOException() throws SQLException {
        when(mockConn.prepareStatement(FIND_BY_ID)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeQuery()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(true);
        mockMapUser(mockResSet);
        doThrow(new SQLException()).when(mockResSet).close();
        doThrow(new SQLException()).when(mockConn).close();
        assertThrows(DAOException.class, () -> userDAO.findById(USER_ID));
    }

    // Test findAll----------------------------------------------------

    @Test
    void findAllUsersWhenFoundOneUserThenReturnsListWithOneUser() throws SQLException {
        when(mockConn.prepareStatement(FIND_ALL)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeQuery()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(true).thenReturn(false);
        mockMapUser(mockResSet);
        List<User> users = userDAO.findAll();
        assertTrue(users.size() == 1);
        assertTrue(users.contains(TEST_USER));
    }

    @Test
    void findAllUsersWhenNoUsersFoundThenReturnsEmptyList() throws SQLException {
        when(mockConn.prepareStatement(FIND_ALL)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeQuery()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(false);
        List<User> users = userDAO.findAll();
        assertTrue(users.size() == 0);
    }

    @Test
    void findAllUsersWhenThrowsOnConnCloseThenThrowsDAOException() throws SQLException {
        when(mockConn.prepareStatement(FIND_ALL)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeQuery()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(true).thenReturn(false);
        mockMapUser(mockResSet);
        doThrow(new SQLException()).when(mockConn).close();
        assertThrows(DAOException.class, () -> userDAO.findAll());
    }


    @Test
    public void findAllUsersWhenThrowsOnExecuteQueryThenThrowsDAOException() throws SQLException {
        when(mockConn.prepareStatement(FIND_ALL)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeQuery();
        assertThrows(DAOException.class, () ->  userDAO.findAll());
    }

    @Test
    public void findAllUsersWhenThrowsOnResSetNextThenThrowsDAOException() throws SQLException {
        when(mockConn.prepareStatement(FIND_ALL)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeQuery()).thenReturn(mockResSet);
        doThrow(new SQLException()).when(mockResSet).next();
        assertThrows(DAOException.class, () -> userDAO.findAll());
    }

    @Test
    public void findAllUsersWhenThrowsOnResSetNextAndConnCloseThenThrowsDAOException() throws SQLException {
        when(mockConn.prepareStatement(FIND_ALL)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeQuery()).thenReturn(mockResSet);
        doThrow(new SQLException()).when(mockResSet).next();
        doThrow(new SQLException()).when(mockConn).close();
        assertThrows(DAOException.class, () -> userDAO.findAll());
    }

    //-----------------------------------------------------------------

    private void mockMapUser(ResultSet mockRS) throws SQLException {
        when(mockRS.getLong("id")).thenReturn(USER_ID);
        when(mockRS.getString("name")).thenReturn(USER_NAME);
        when(mockRS.getString("email")).thenReturn(USER_EMAIL);
        when(mockRS.getString("password")).thenReturn(USER_PASSWORD);
    }
}