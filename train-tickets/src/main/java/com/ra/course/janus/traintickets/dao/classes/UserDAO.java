package com.ra.course.janus.traintickets.dao.classes;

import com.ra.course.janus.traintickets.dao.api.DAO;
import com.ra.course.janus.traintickets.entity.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAO implements DAO<User> {
    private final DataSource dataSource;

    private final String INSERT_USER = "insert into USERS (name, id) values (?, ?)";
    private final String SELECT_USER = "SELECT * FROM USERS WHERE id = ?";


    public UserDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User save(User user) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement userStatement = connection.prepareStatement(INSERT_USER);
            userStatement.setString(1, user.getName());
            userStatement.setLong(2, user.getId());
            userStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        return user;
    }

    @Override
    public User update(Long id, User item) {
        return null;
    }

    @Override
    public User delete(Long id) {
        return null;
    }

    @Override
    public User findById(Long id) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement userStatement = conn.prepareStatement(SELECT_USER);
            userStatement.setLong(1, id);
            User user = new User();
            ResultSet rs = userStatement.executeQuery();
            if (rs.next()) {
                user.setId(id);
                user.setName(rs.getString("USERNAME"));
            }

            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> findAll() {
        return null;
    }
}
