package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.configuration.DataSourceFactory;
import com.ra.course.janus.traintickets.entity.User;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;


import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserDAOIntegrationTest {

    private static final DataSource DATA_SOURCE = DataSourceFactory.HIKARY_H2_IN_MEMORY.getDataSource();
    private static final String SQL_SCRIPT_FILE_NAME = "src/test/resources/sql_scripts/create_users_table.sql";

    private static final String SAVE_USER = "insert into USERS (name,email,password) values (?,?,?)";
    private static final String UPDATE_USER = "update USERS set (name,email,password) values (?,?,?) WHERE id=?";
    private static final String DELETE_USER = "delete from USERS where id=?";
    private static final String FIND_BY_ID = "select * from USERS where id=?";
    private static final String FIND_ALL = "select * from USERS";
    private static final Long ZERO_ID = 0L;
    private static final Long USER_ID = 100L;
    private static final String USER_NAME = "test_name";
    private static final String USER_EMAIL = "test_name123@gmail.com";
    private static final String USER_PASSWORD = "password";
    private static final User TEST_USER = new User(USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD);

    private UserDAO userDAO;


    @BeforeEach
    public void setUp() throws IOException, SQLException {
        createUsersTable();
        userDAO = new UserDAO(DATA_SOURCE);
    }

    @Test
    public void saveUserWhenOkThenReturnUserWithGeneratedId() throws IOException, SQLException {
        assertTrue(usersEqualsWithoutID(TEST_USER, userDAO.save(TEST_USER)));
    }

    private static void createUsersTable() throws SQLException, IOException {
        try (Connection conn = DATA_SOURCE.getConnection()) {
            try (FileReader reader = new FileReader(SQL_SCRIPT_FILE_NAME)) {
                RunScript.execute(conn, reader);
            }
       }
    }

    private boolean usersEqualsWithoutID (User user1, User user2) {
        return user1.getName().equals(user2.getName())
                && user1.getEmail().equals(user2.getEmail())
                && user1.getPassword().equals(user2.getPassword());
    }

    private boolean usersAllFieldsEquals (User user1, User user2) {
        return user1.getId().equals(user2.getId())
                && usersEqualsWithoutID(user1, user2);
    }
}