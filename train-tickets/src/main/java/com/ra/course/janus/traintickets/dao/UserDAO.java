package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.dao.api.DAO;
import com.ra.course.janus.traintickets.entity.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements DAO<User> {
    private final DataSource dataSource;

    private static final String SAVE_USER = "insert into USERS (name,email,password) values (?,?,?)";
    private static final String UPDATE_USER = "update USERS set (name,email,password) values (?,?,?) WHERE id=?";
    private static final String DELETE_USER= "delete from USERS where id=?";
    private static final String FIND_BY_ID = "select * from USERS where id=?";
    private static final String FIND_ALL = "select * from USERS";


    public UserDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Long save(User user) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement saveStatement = connection.prepareStatement(SAVE_USER);
            fromUser(saveStatement, user);
            saveStatement.executeUpdate();
            ResultSet generatedKeys = saveStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                final long id = generatedKeys.getLong(1);
                return id;
            } else {
                return -1L;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Long id, User user) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement updateStatement = connection.prepareStatement(UPDATE_USER);
            fromUser(updateStatement, user);
            updateStatement.setLong(4, id);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement deleteStatement = connection.prepareStatement(DELETE_USER);
            deleteStatement.setLong(1, id);
            deleteStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

        @Override
    public User findById(Long id) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement userStatement = conn.prepareStatement(FIND_BY_ID);
            userStatement.setLong(4, id);
            ResultSet rs = userStatement.executeQuery();
            return toUser(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList();
        User user;
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement userStatement = conn.prepareStatement(FIND_ALL);
            ResultSet rs = userStatement.executeQuery();
            while ((user = toUser(rs)) != null) {
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    private void fromUser(PreparedStatement statement, User user) {
        try {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException("failed to prepare statment", e);
        }
    }

    private User toUser(ResultSet rs) throws SQLException {
        User user = new User();
        if (rs.next()) {
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            return user;
        } else {
            return null;
        }
    }
}

