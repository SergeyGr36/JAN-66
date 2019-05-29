package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Work;
import com.ra.janus.developersteam.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class PlainJdbcWorkDAO implements BaseDao<Work> {
    private static final String INSERT_SQL = "INSERT INTO works (name, price) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE works SET name=?,price=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM works";
    private static final String SELECT_ONE_SQL = "SELECT * FROM works WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM works WHERE id=?";

    private static final Logger LOGGER = LoggerFactory.getLogger(PlainJdbcWorkDAO.class);
    private static final String EXCEPTION_WARN = "An exception occurred!";

    transient private final DataSource dataSource;

    public PlainJdbcWorkDAO(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Work create(final Work work) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatement(ps, work);
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys();) {
                if (generatedKeys.next()) {
                    final long id = generatedKeys.getLong(1);
                    return new Work(id, work);
                } else {
                    throw new DAOException("Could not create a work");
                }
            }
        } catch (SQLException e) {
            LOGGER.error(EXCEPTION_WARN, e);
            throw new DAOException(e);
        }
    }

    @Override
    @SuppressWarnings("PMD.CloseResource")
    public Work get(final long id) {
        try  {
            final Connection conn = dataSource.getConnection();
            final PreparedStatement ps = conn.prepareStatement(SELECT_ONE_SQL);
            ps.setLong(1, id);
            final ResultSet rs = ps.executeQuery();
            try {
                if (rs.next()) {
                    return toWork(rs);
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
    public List<Work> getAll() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            final List<Work> works = new ArrayList<>();
            while (rs.next()) {
                works.add(toWork(rs));
            }
            return works;
        } catch (SQLException e) {
            LOGGER.error(EXCEPTION_WARN, e);
            throw new DAOException(e);
        }
    }

    @Override
    public boolean update(final Work work) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            prepareStatement(ps, work);
            ps.setLong(3, work.getId());
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

    private Work toWork(final ResultSet rs) throws SQLException {
        return new Work(rs.getLong("id"),
                rs.getString("name"),
                rs.getBigDecimal("price"));
    }

    private void prepareStatement(final PreparedStatement ps, final Work work) throws SQLException {
        ps.setString(1, work.getName());
        ps.setBigDecimal(2, work.getPrice());
    }
}
