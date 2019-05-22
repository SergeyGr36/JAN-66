package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.dao.interfaces.ManagerDAO;
import com.ra.janus.developersteam.entity.Manager;
import com.ra.janus.developersteam.exception.DAOException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcManagerDAO implements ManagerDAO {

    private static final String SELECT_ALL_SQL = "SELECT * FROM MANAGERS";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM MANAGERS WHERE id = ?";
    private static final String SELECT_BY_MANE_SQL = "SELECT * FROM MANAGERS WHERE NAME = ?";
    private static final String INSERT_SQL = "INSERT INTO MANAGERS (name, email, phone) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE MANAGERS SET name=?,email=?,phone=? WHERE id=?";
    private static final String DELETE_SQL = "DELETE FROM MANAGERS WHERE id=?";

    private DataSource dataSource;

    public JdbcManagerDAO(DataSource dataSource) {

        this.dataSource = dataSource;
    }

    @Override
    public List<Manager> findAll() {

        try (Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
            ResultSet rs = ps.executeQuery()) {

                final List<Manager> managers = new ArrayList<>();
                while (rs.next()) {
                    managers.add(toManager(rs));
                }
                return managers;

        } catch (SQLException ex) {
            throw new DAOException(ex);
        }
    }

    @Override
    public Manager findById(Long id) {

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return toManager(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DAOException(e);
        }

    }

    @Override
    public Manager findByName(String name) {

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_MANE_SQL)) {
            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return toManager(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Manager create(final Manager manager) {

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, manager.getName());
            ps.setString(2, manager.getEmail());
            ps.setString(3, manager.getPhone());
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys();) {
                if (generatedKeys.next()) {
                    final long id = generatedKeys.getLong(1);
                    return new Manager(id, manager);
                } else {
                    throw new DAOException("Couldn't retrieve generated id for manager " + manager);
                }
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

    }

    @Override
    public Boolean updade(Manager manager) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, manager.getName());
            ps.setString(2, manager.getEmail());
            ps.setString(3, manager.getPhone());
            final int rowCount = ps.executeUpdate();
            return rowCount != 0;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Boolean delete(Long id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {
            ps.setLong(1, id);
            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    private Manager toManager(ResultSet rs) {

        try {

            Manager manager = new Manager();
            manager.setId(rs.getLong("id"));
            manager.setName(rs.getString("name"));
            manager.setEmail(rs.getString("email"));
            manager.setPhone(rs.getString("phone"));
            return manager;
        } catch (SQLException ex) {
            throw  new DAOException(ex);
        }

    }
}
