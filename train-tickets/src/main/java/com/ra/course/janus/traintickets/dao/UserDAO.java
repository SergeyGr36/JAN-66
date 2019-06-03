package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.entity.User;
import com.ra.course.janus.traintickets.exception.DAOException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IJdbcDao<User> {
    private final transient DataSource dataSource;

    private static final String SAVE_USER = "insert into USERS (name,email,password) values (?,?,?)";
    private static final String UPDATE_USER = "update USERS set name=?, email=?, password=? WHERE id=?";
    private static final String DELETE_USER = "delete from USERS where id=?";
    private static final String FIND_BY_ID = "select * from USERS where id=?";
    private static final String FIND_ALL = "select * from USERS";

    private static final int COL_IND_ID = 1;
    private static final int COL_IND_NAME = 2;
    private static final int COL_IND_EMAIL = 3;
    private static final int COL_IND_PASSWD = 4;

    public UserDAO(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User save(final User user) {
        try (Connection conn = dataSource.getConnection()) {
             try(PreparedStatement saveStmt = conn.prepareStatement(SAVE_USER)) {
                 prepareStatementWithUser(saveStmt, user);
                 saveStmt.executeUpdate();
                 try (ResultSet generatedKeys = saveStmt.getGeneratedKeys()) {
                     if (generatedKeys.next()) {
                         final long id = generatedKeys.getLong(COL_IND_ID);
                         return new User(
                                 id,
                                 user.getName(),
                                 user.getEmail(),
                                 user.getPassword()
                         );
                     } else {
                         throw new DAOException();
                     }
                 }
             }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public boolean update(final Long id, final User user) {
        try (Connection conn = dataSource.getConnection()) {
             try (PreparedStatement updateStmt = conn.prepareStatement(UPDATE_USER)) {
                 prepareStatementWithUser(updateStmt, user);
                 updateStmt.setLong(4, id);
                 return updateStmt.executeUpdate() > 0;
             }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public boolean delete(final Long id) {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement deleteStmt = conn.prepareStatement(DELETE_USER)) {
                deleteStmt.setLong(1, id);
                return deleteStmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public User findById(final Long id) {
        final User user;
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement findStmt = conn.prepareStatement(FIND_BY_ID)) {
                findStmt.setLong(1, id);
                try (ResultSet rs = findStmt.executeQuery()) {
                    if (rs.next()) {
                        user = toUser(rs);
                    } else {
                        user = null;
                    }
                }
            }
            return user;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public List<User> findAll() {
        try (Connection conn = dataSource.getConnection();
            PreparedStatement userStatement = conn.prepareStatement(FIND_ALL);
            ResultSet rs = userStatement.executeQuery()) {
            final List<User> users = new ArrayList();
            if (rs.next()) {
                users.add(toUser(rs));
            }
            return users;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    private void prepareStatementWithUser(final PreparedStatement ps, final User user) throws SQLException {
        ps.setString(1, user.getName());
        ps.setString(2, user.getEmail());
        ps.setString(3, user.getPassword());
    }

    private User toUser(final ResultSet rs) throws SQLException {
        return new User(
                rs.getLong(COL_IND_ID),
                rs.getString(COL_IND_NAME),
                rs.getString(COL_IND_EMAIL),
                rs.getString(COL_IND_PASSWD)
        );
    }
}