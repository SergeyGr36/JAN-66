package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.entity.User;
import com.ra.course.janus.traintickets.exception.DAOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserJdbcDAOMockTest {

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
    private UserJdbcDAO userDAO;
    private Connection mockConn;
    private PreparedStatement mockPrepSt;
    private ResultSet mockResSet;

    private SimpleJdbcInsert mockJdbcInsert;
    private SqlParameterSource mockSqlParamSource;
    SqlParameterSource beanPropSqlParamSour;

    @BeforeEach
    public void setUp() throws SQLException {
        userDAO = new UserJdbcDAO(MOCK_DATA_SOURCE);

        mockConn = mock(Connection.class);
        when(MOCK_DATA_SOURCE.getConnection()).thenReturn(mockConn);

        mockPrepSt = mock(PreparedStatement.class);
        mockResSet = mock(ResultSet.class);

        when(mockPrepSt.executeQuery()).thenReturn(mockResSet);

        mockJdbcInsert = mock(SimpleJdbcInsert.class);
        mockSqlParamSource = mock(SqlParameterSource.class);

        beanPropSqlParamSour = new BeanPropertySqlParameterSource(TEST_USER);
    }

    // Test save-------------------------------------------------------

    @Test
    public void whenTheObjectSuccessfullySaved() {
        when(mockJdbcInsert.executeAndReturnKey(mockSqlParamSource))
                .thenReturn(1L);

        User actualUser = userDAO.save(TEST_USER);

        assertEquals(TEST_USER, actualUser);
    }


    @Test
    public void saveWhenUserSavedOkThenNewUserReturnedWithMappingOk() throws SQLException {
        // given
        when(mockConn.prepareStatement(SAVE_USER)).thenReturn(mockPrepSt);
        when(mockPrepSt.getGeneratedKeys()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(true);
        when(mockResSet.getLong(COL_INDEX_ID)).thenReturn(USER_ID);
        //when
        user = userDAO.save(TEST_USER);
        // then
        assertEquals(TEST_USER, user);
    }

    @Test
    public void saveUserWhenThrowsSQLExceptionThenThrowsDAOException() throws SQLException {
        // when
        when(mockConn.prepareStatement(SAVE_USER)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeUpdate();
        // then
        assertThrows(DAOException.class, () -> userDAO.save(TEST_USER));
    }

    @Test
    public void saveUserWhenFailToReadGeneratedKeyThenThrowsDAOException() throws SQLException {
        // when
        when(mockConn.prepareStatement(SAVE_USER)).thenReturn(mockPrepSt);
        when(mockPrepSt.getGeneratedKeys()).thenReturn(mockResSet);
        when(mockResSet.next()).thenReturn(false);
        //then
        assertThrows(DAOException.class, () -> userDAO.save(TEST_USER));
    }

    // Test update-----------------------------------------------------

    @Test
    public void updateWhenUserWithProvidedIdUpdateOkThenReturnTrue() throws SQLException {
        // when
        when(mockConn.prepareStatement(UPDATE_USER)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeUpdate()).thenReturn(1);
        // then
        assertTrue(userDAO.update(TEST_USER));
    }

    @Test
    public void updateWhenUserWithProvidedIdNotFoundThenReturnFalse() throws SQLException {
        // when
        when(mockConn.prepareStatement(UPDATE_USER)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeUpdate()).thenReturn(0);
        //then
        assertFalse(userDAO.update(TEST_USER));
    }

    @Test
    public void updateUserWhenThrowsSQLExceptionThenThrowsDAOException() throws SQLException {
        // when
        when(mockConn.prepareStatement(UPDATE_USER)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeUpdate();
        // then
        assertThrows(DAOException.class, () -> userDAO.update(TEST_USER));
    }

    // Test delete-----------------------------------------------------

    @Test
    public void deleteWhenUserWithProvidedIdDeleteOkThenReturnTrue() throws SQLException {
        // when
        when(mockConn.prepareStatement(DELETE_USER)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeUpdate()).thenReturn(1);
        // then
        assertTrue(userDAO.delete(USER_ID));
    }

    @Test
    public void deleteWhenUserWithProvidedIdNotFoundThenReturnFalse() throws SQLException {
        // when
        when(mockConn.prepareStatement(DELETE_USER)).thenReturn(mockPrepSt);
        when(mockPrepSt.executeUpdate()).thenReturn(0);
        // then
        assertFalse(userDAO.delete(USER_ID));
    }

    @Test
    public void deleteUserWhenThrowsSQLExceptionThenThrowsDAOException() throws SQLException {
        //when
        when(mockConn.prepareStatement(DELETE_USER)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeUpdate();
        // then
        assertThrows(DAOException.class, () -> userDAO.delete(USER_ID));
    }

    // Test findById-------------------------------------------------------

    @Test
    public void findByIdWhenUserWithProvidedIdFoundOkThenReturnUser() throws SQLException {
        // given
        when(mockConn.prepareStatement(FIND_BY_ID)).thenReturn(mockPrepSt);
        when(mockResSet.next()).thenReturn(true);
        mockMapUser(mockResSet);
        // when
        user = userDAO.findById(USER_ID);
        // then
        assertEquals(TEST_USER, user);
    }

    @Test
    public void findByIdWhenUserWithProvidedIdNotFoundThenReturnNull() throws SQLException {
        // given
        when(mockConn.prepareStatement(FIND_BY_ID)).thenReturn(mockPrepSt);
        when(mockResSet.next()).thenReturn(false);
        mockMapUser(mockResSet);
        // when
        user = userDAO.findById(USER_ID);
        // then
        assertNull(user);
    }


    @Test
    public void findUserByIdWhenThrowsSQLExceptionThenThrowsDAOException() throws SQLException {
        // when
        when(mockConn.prepareStatement(FIND_BY_ID)).thenReturn(mockPrepSt);
        doThrow(new SQLException()).when(mockPrepSt).executeQuery();
        // then
        assertThrows(DAOException.class, () -> userDAO.findById(USER_ID));
    }

    // Test findAll----------------------------------------------------

    @Test
    public void findAllUsersWhenFoundOneUserThenReturnsListWithOneUser() throws SQLException {
        // given
        when(mockConn.prepareStatement(FIND_ALL)).thenReturn(mockPrepSt);
        when(mockResSet.next()).thenReturn(true).thenReturn(false);
        mockMapUser(mockResSet);
        List<User> expectedUsers = Collections.singletonList(TEST_USER);
        // when
        List<User> users = userDAO.findAll();
        //then
        assertEquals(expectedUsers, users);
    }

    @Test
    public void findAllUsersWhenNoUsersFoundThenReturnsEmptyList() throws SQLException {
        // given
        when(mockConn.prepareStatement(FIND_ALL)).thenReturn(mockPrepSt);
        when(mockResSet.next()).thenReturn(false);
        // when
        List<User> users = userDAO.findAll();
        // then
        assertTrue(users.size() == 0);
    }

    @Test
    public void findAllUsersWhenThrowsSQLExceptionThenThrowsDAOException() throws SQLException {
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