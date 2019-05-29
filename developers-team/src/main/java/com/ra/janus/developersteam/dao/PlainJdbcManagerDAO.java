package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Manager;
import com.ra.janus.developersteam.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlainJdbcManagerDAO implements BaseDao<Manager> {

    private static final String INSERT_SQL = "INSERT INTO managers (name, email, phone) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE managers SET name=?,email=?,phone=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM managers";
    private static final String SELECT_ONE_SQL = "SELECT * FROM managers WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM managers WHERE id=?";

    private static final Logger LOGGER = LoggerFactory.getLogger(PlainJdbcManagerDAO.class);
    public static final String EXCEPTION_WARN = "An exception occurred!";

    transient private final DataSource dataSource;

    public PlainJdbcManagerDAO(final DataSource dataSource) {

        this.dataSource = dataSource;
    }

    @Override
    public Manager create(final Manager manager) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatement(ps, manager);
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys();) {
                if (generatedKeys.next()) {
                    final long id = generatedKeys.getLong(1);
                    return new Manager(id, manager);
                } else {
                    throw new DAOException("Could not create a Manager");
                }
            }
        } catch (SQLException e) {
            LOGGER.error(EXCEPTION_WARN, e);
            throw new DAOException(e);
        }
    }

    @Override
    @SuppressWarnings("PMD.CloseResource")
    public Manager get(final long id) {
        try  {
            final Connection conn = dataSource.getConnection();
            final PreparedStatement ps = conn.prepareStatement(SELECT_ONE_SQL);
            ps.setLong(1, id);
            final ResultSet rs = ps.executeQuery();
            try {
                if (rs.next()) {
                    return toManager(rs);
                } else {
                    return null;
                }
            } finally {
                rs.close();
                conn.close();
            }
        } catch (SQLException e) {
            LOGGER.error(EXCEPTION_WARN, e);
            throw new DAOException(e);
        }
    }

    @Override
    public List<Manager> getAll() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            final List<Manager> managers = new ArrayList<>();
            while (rs.next()) {
                managers.add(toManager(rs));
            }
            return managers;
        } catch (SQLException e) {
            LOGGER.error(EXCEPTION_WARN, e);
            throw new DAOException(e);
        }
    }

    @Override
    public boolean update(final Manager manager) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            prepareStatement(ps, manager);
            ps.setLong(4, manager.getId());
            final int rowCount = ps.executeUpdate();
            return rowCount != 0;
        } catch (SQLException e) {
            LOGGER.error(EXCEPTION_WARN, e);
            throw new DAOException(e);
        }
    }

    @Override
    public boolean delete(final long id) {
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {
            ps.setLong(1, id);
            final int rowCount = ps.executeUpdate();
            return rowCount != 0;
        } catch (SQLException e) {
            LOGGER.error(EXCEPTION_WARN, e);
            throw new DAOException(e);
        }
    }

    private Manager toManager(final ResultSet rs) throws SQLException {
        return new Manager(rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone"));
    }

    private void prepareStatement(final PreparedStatement ps, final Manager manager) throws SQLException {
        ps.setString(1, manager.getName());
        ps.setString(2, manager.getEmail());
        ps.setString(3, manager.getPhone());
    }
}
