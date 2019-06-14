package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserJdbcDAOMockTest {

    private static final String UPDATE_USER =
            "update USERS set name = :name, email = :email, password = :password WHERE id = :id";
    private static final String DELETE_USER = "delete from USERS where id = :id";
    private static final String FIND_BY_ID = "select * from USERS where id = :id";
    private static final String FIND_ALL = "select * from USERS";

    private static final Long   USER_ID = 100L;
    private static final String USER_NAME = "test_name";
    private static final String USER_EMAIL = "test_name123@gmail.com";
    private static final String USER_PASSWORD = "password";
    private static final User TEST_USER = new User(USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);

    private UserJdbcDAO userDAO;

    private SimpleJdbcInsert mockJdbcInsert;
    private NamedParameterJdbcTemplate mockNamedJdbcTemplate;


    @BeforeEach
    public void setUp() {
        mockJdbcInsert = mock(SimpleJdbcInsert.class);
        mockNamedJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        userDAO = new UserJdbcDAO(mockJdbcInsert, mockNamedJdbcTemplate);
    }

    // Test save-------------------------------------------------------

    @Test
    public void saveWhenUserSavedOkThenNewUserReturnedWithMappingOk() {
        // given
        when(mockJdbcInsert
                .executeAndReturnKey(any(SqlParameterSource.class)))
                .thenReturn(USER_ID);
        // when
        final User actualUser = userDAO.save(TEST_USER);
        // then
        assertEquals(TEST_USER, actualUser);
    }


    // Test update-----------------------------------------------------

    @Test
    public void updateWhenUserWithProvidedIdUpdateOkThenReturnTrue()  {
        // when
        when (mockNamedJdbcTemplate.update(
                anyString(),
                any(BeanPropertySqlParameterSource.class)
        )).thenReturn(1);
        // then
        assertTrue(userDAO.update(TEST_USER));
    }

    @Test
    public void updateWhenUserWithProvidedIdNotFoundThenReturnFalse() {
        // when
        when (mockNamedJdbcTemplate.update(
                anyString(),
                any(BeanPropertySqlParameterSource.class)
        )).thenReturn(0);
        // then
        assertFalse(userDAO.update(TEST_USER));
    }

    // Test delete-----------------------------------------------------

    @Test
    public void deleteWhenUserWithProvidedIdDeleteOkThenReturnTrue()  {
        // when
        when(mockNamedJdbcTemplate.update(
                anyString(),
                any(MapSqlParameterSource.class)
        )).thenReturn(1);
        // then
        assertTrue(userDAO.delete(USER_ID));
    }

    @Test
    public void deleteWhenUserWithProvidedIdNotFoundThenReturnFalse() {
        // when
        when(mockNamedJdbcTemplate.update(
                anyString(),
                any(MapSqlParameterSource.class)
        )).thenReturn(0);
        // then
        assertFalse(userDAO.delete(USER_ID));
    }

    // Test findById-------------------------------------------------------

    @Test
    public void findByIdWhenUserWithProvidedIdFoundOkThenReturnUser() {
        // given
        when(mockNamedJdbcTemplate.queryForObject(
                anyString(),
                any(MapSqlParameterSource.class),
                any(BeanPropertyRowMapper.class)
        )).thenReturn(TEST_USER);
        // when
        final User user = userDAO.findById(USER_ID);
        // then
        assertEquals(TEST_USER, user);
    }

    // Test findAll----------------------------------------------------

    @Test
    public void findAllUsersWhenFoundOneUserThenReturnsListWithOneUser()  {
        // given
        List<User> expectedUsers = Collections.singletonList(TEST_USER);
        when(mockNamedJdbcTemplate.query(
                anyString(),
                any(MapSqlParameterSource.class),
                any(BeanPropertyRowMapper.class)
        )).thenReturn(Collections.singletonList(TEST_USER));
        // when
        List<User> users = userDAO.findAll();
        //then
        assertEquals(expectedUsers, users);
    }
}