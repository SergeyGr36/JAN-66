package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.entity.User;
import com.ra.course.janus.traintickets.exception.DAOException;
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
import static org.mockito.Mockito.when;

class UserDAOMockTest {

    private static final String SAVE_USER = "insert into USERS (name,email,password) values (?,?,?)";
    private static final String UPDATE_USER = "update USERS set name=?, email=?, password=? WHERE id=?";
    private static final String DELETE_USER= "delete from USERS where id=?";
    private static final String FIND_BY_ID = "select * from USERS where id=?";
    private static final String FIND_ALL = "select * from USERS";

    private static final int COL_INDEX_ID = 1;
    private static final int COL_INDEX_NAME = 2;
    private static final int COL_INDEX_EMAIL = 3;
    private static final int COL_INDEX_PASSWD = 4;

    private static final Long   USER_ID = 100L;
    private static final String USER_NAME = "test_name";
    private static final String USER_EMAIL = "test_name123@gmail.com";
    private static final String USER_PASSWORD = "password";
    private static final User TEST_USER = new User(USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);

    private static final DataSource MOCK_DATA_SOURCE = mock(DataSource.class);
    private User user;
    private UserDAO userDAO;
    private Connection mockConn;
    private PreparedStatement mockPrepSt;
    private ResultSet mockResSet;

    @BeforeEach
    void setUp() throws SQLException {
        userDAO = new UserDAO(MOCK_DATA_SOURCE);

        mockConn = mock(Connection.class);
        when(MOCK_DATA_SOURCE.getConnection()).thenReturn(mockConn);

        mockPrepSt = mock(PreparedStatement.class);
        mockResSet = mock(ResultSet.class);

        when(mockPrepSt.executeQuery()).thenReturn(mockResSet);
    }

    // Test save-------------------------------------------------------

    @Test
    void saveWhenUserSavedOkThenNewUserWithGeneratedIdReturned() throws SQLException {
        when(mockConn.prepareStatement(SAVE_USER)).thenReturn(mockPrepSt);
        when(mockPrepSt.getGeneratedKeys()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(true);
        when(mockResSet.getLong(COL_INDEX_ID)).thenReturn(USER_ID);
        user = new User(null, USER_NAME, USER_EMAIL, USER_PASSWORD);
        user = userDAO.save(user);
        assertNotSame(TEST_USER, user);
        assertEquals(TEST_USER, user);
    }

    @Test
    void saveUserWhenThrowsSQLExceptionOnExecuteUpdateThenThrowsDAOException() throws SQLException {

        when(mockConn.prepareStatement(SAVE_USER)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeUpdate();
        assertThrows(DAOException.class, () -> userDAO.save(TEST_USER));
    }

    @Test
    void saveUserWhenFailToReadGeneratedKeyThenThrowsDAOException() throws SQLException {

        when(mockConn.prepareStatement(SAVE_USER)).thenReturn(mockPrepSt);
        when(mockPrepSt.getGeneratedKeys()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(false);
        assertThrows(DAOException.class, () -> userDAO.save(TEST_USER));
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
    void findUserByIdWhenThrowsOnExecuteStmtThenThrowsDAOException() throws SQLException {
        when(mockConn.prepareStatement(FIND_BY_ID)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeQuery();
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
    public void findAllUsersWhenThrowsOnExecuteQueryThenThrowsDAOException() throws SQLException {
        when(mockConn.prepareStatement(FIND_ALL)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeQuery();
        assertThrows(DAOException.class, () ->  userDAO.findAll());
    }

    //-----------------------------------------------------------------

    private void mockMapUser(ResultSet mockRS) throws SQLException {
        when(mockRS.getLong(COL_INDEX_ID)).thenReturn(USER_ID);
        when(mockRS.getString(COL_INDEX_NAME)).thenReturn(USER_NAME);
        when(mockRS.getString(COL_INDEX_EMAIL)).thenReturn(USER_EMAIL);
        when(mockRS.getString(COL_INDEX_PASSWD)).thenReturn(USER_PASSWORD);
    }
}