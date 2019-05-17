package com.ra.course.janus.traintickets.dao.classes;

import com.ra.course.janus.traintickets.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class UserDAOTest {

    private static final Long USER_ID = 15L;
    private static final String USER_NAME = "test_user";
    private static final DataSource DATA_SOURCE = DataSourceFactory.H2_IN_MEMORY.getDataSource();

    private User user;

    @BeforeAll
    public static void setupH2Schema() {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            Statement st = connection.createStatement();
            st.execute("create table users(id long, name varchar(44))");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setup() {
        user = new User();
        user.setId(USER_ID);
        user.setName(USER_NAME);
    }

    @Test
    void save() {
        UserDAO userDAO = new UserDAO(DATA_SOURCE);
        userDAO.save(user);
        ResultSet rs;
        try (Connection connection = DATA_SOURCE.getConnection()) {
            Statement st = connection.createStatement();
            rs = st.executeQuery("SELECT * FROM USERS");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
    }
}