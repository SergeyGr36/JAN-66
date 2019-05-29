package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Developer;
import com.ra.janus.developersteam.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlainJdbcDeveloperDAO implements BaseDao<Developer> {
    private static final String EXCEPTION_WARN = "An exception occurred!";
    private static final String INSERT_SQL = "INSERT INTO developers (name) VALUES (?)";
    private static final String UPDATE_SQL = "UPDATE developers SET name=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM developers";
    private static final String SELECT_ONE_SQL = "SELECT * FROM developers WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM developers WHERE id=?";
    private static final Logger LOGGER = LoggerFactory.getLogger(PlainJdbcDeveloperDAO.class);
    transient private final DataSource dataSource;


    public PlainJdbcDeveloperDAO(final DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public Developer create(final Developer developer) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatement(ps, developer);
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys();) {
                if (generatedKeys.next()) {
                    final long id = generatedKeys.getLong(1);
                    return new Developer(id, developer);
                } else {
                    throw new DAOException("Could not create a Developer");
                }
            }
        } catch (SQLException e) {
            LOGGER.error(EXCEPTION_WARN, e);
            throw new DAOException(e);
        }
    }

    @Override
    @SuppressWarnings("PMD.CloseResource")
    public Developer get(final long id) {
        try {
            final Connection conn = dataSource.getConnection();
            final PreparedStatement ps = conn.prepareStatement(SELECT_ONE_SQL);
            ps.setLong(1, id);
            final ResultSet rs = ps.executeQuery();
            try {
                if (rs.next()) {
                    return toDeveloper(rs);
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
    public List<Developer> getAll() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            final List<Developer> developers = new ArrayList<>();
            while (rs.next()) {
                developers.add(toDeveloper(rs));
            }
            return developers;
        } catch (SQLException e) {
            LOGGER.error(EXCEPTION_WARN, e);
            throw new DAOException(e);
        }
    }

    @Override
    public boolean update(final Developer developer) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            prepareStatement(ps, developer);
            ps.setLong(2, developer.getId());
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

    private Developer toDeveloper(final ResultSet rs) throws SQLException {
        return new Developer(rs.getLong("id"),
                rs.getString("name"));
    }

    private void prepareStatement(final PreparedStatement ps, final Developer developer) throws SQLException {
        ps.setString(1, developer.getName());
    }
}
