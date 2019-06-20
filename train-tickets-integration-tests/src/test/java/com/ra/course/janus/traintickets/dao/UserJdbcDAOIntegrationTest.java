package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.configuration.MainSpringConfig;
import com.ra.course.janus.traintickets.entity.User;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MainSpringConfig.class)
@TestPropertySource("classpath:test_config.properties")
@Sql("classpath:sql_scripts/create_users_table.sql")
public class UserJdbcDAOIntegrationTest {

    private static final User TEST_USER = new User(null, "testname", "mail", "passwd");

    @Autowired
    private UserJdbcDAO userDAO;

    // Test saveUser---------------------------------------------------

    @Test
    public void saveUserWhenOkThenIdIsGenerated() {
        // when
        final User savedUser = userDAO.save(TEST_USER);
        // then
        assertNotNull(savedUser.getId());
    }

    // Test updateUser-------------------------------------------------

    @Test
    public void updateUserWhenOkThenIsUpdated() {
        // given
        final Long id = userDAO.save(TEST_USER).getId();
        final User newUser = new User(id, "new_name", "new_mail", "new_pass");
        // when
        userDAO.update(newUser);
        final User updatedUser = userDAO.findById(id);
        // then
        assertEquals(newUser, updatedUser);
    }

    // Test deleteUser-------------------------------------------------

    @Test
    public void deleteUserWhenOkThenUserIsDeleted() {
        // given
        final Long id = userDAO.save(TEST_USER).getId();
        // when
        userDAO.delete(id);
        // then
        assertThrows(EmptyResultDataAccessException.class, () -> userDAO.findById(id));
    }

    // Test findUserById-------------------------------------------------

    @Test
    public void findUserByIdWhenOkThenReturnUser() {
        // given
        final User savedUser = userDAO.save(TEST_USER);
        // when
        final User foundUser = userDAO.findById(savedUser.getId());
        // then
        assertEquals(savedUser, foundUser);
    }

    // Test findAllUsers-------------------------------------------------

    @Test
    public void findAllUsersWhenOkThenReturnListWithUsers() {
        // given
        final User savedUser = userDAO.save(TEST_USER);
        final User user1 = new User(null, "aaa", "nnn", "ttt");
        final User savedUser1 = userDAO.save(user1);
        List<User> expectedUsers = Arrays.asList(savedUser, savedUser1);
        // when
        List<User> actualUsers = userDAO.findAll();
        // then
        assertEquals(expectedUsers, actualUsers);
    }
}