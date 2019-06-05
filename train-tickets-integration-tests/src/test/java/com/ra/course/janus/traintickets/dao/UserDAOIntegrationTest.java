package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.configuration.DataSourceFactory;
import com.ra.course.janus.traintickets.entity.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserDAOIntegrationTest {

    private static final DataSource DATA_SOURCE = DataSourceFactory.DATA_SOURCE.getInstance();
    private static final String SQL_SCRIPT_FILE_NAME = "src/test/resources/sql_scripts/create_users_table.sql";

    private static final User TEST_USER = new User(null, "testname", "mail", "passwd");

    private UserDAO userDAO;

    @BeforeAll
    public static void createUsersTable() throws IOException, SQLException {
        createTableUsers();
    }

    @BeforeEach
    public void setUp() throws SQLException {
        clearTableUsers();
        userDAO = new UserDAO(DATA_SOURCE);
    }

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
        userDAO.update(id, newUser);
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
        assertNull(userDAO.findById(id));
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
        List<User> savedUsers = Collections.singletonList(savedUser);
        // when
        List<User> foundUsers = userDAO.findAll();
        // then
        assertEquals(savedUsers, foundUsers);
    }

    //-----------------------------------------------------------------

    private static String readScriptFile() throws IOException {
        return String.join("", Files.readAllLines(Paths.get(SQL_SCRIPT_FILE_NAME)));
    }

    private static void executeScript(String script) throws SQLException {
        try (Connection conn = DATA_SOURCE.getConnection()) {
            try (Statement statement = conn.createStatement()) {
                statement.execute(script);
            }
        }
    }

    private static void createTableUsers() throws IOException, SQLException {
        executeScript(readScriptFile());
    }

    private static void clearTableUsers() throws SQLException {
        executeScript("TRUNCATE TABLE users;");
    }
}