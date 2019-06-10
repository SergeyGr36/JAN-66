package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.MainSpringConfig;
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MainSpringConfig.class)
public class UserJdbcDAOIntegrationTest {




    private static final String SQL_SCRIPT_FILE_NAME = "src/test/resources/sql_scripts/create_users_table.sql";

    private static final User TEST_USER = new User(null, "testname", "mail", "passwd");

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserJdbcDAO userDAO;

    @BeforeEach
    public void setUp() throws SQLException, IOException {
        createTableUsers();
        clearTableUsers();
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

    private String readScriptFile() throws IOException {
        return String.join("", Files.readAllLines(Paths.get(SQL_SCRIPT_FILE_NAME)));
    }

    private void executeScript(String script) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            try (Statement statement = conn.createStatement()) {
                statement.execute(script);
            }
        }
    }

    private void createTableUsers() throws IOException, SQLException {
        executeScript(readScriptFile());
    }

    private void clearTableUsers() throws SQLException {
        executeScript("TRUNCATE TABLE users;");
    }
}