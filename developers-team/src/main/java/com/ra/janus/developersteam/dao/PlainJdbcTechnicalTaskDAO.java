package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.TechnicalTask;
import com.ra.janus.developersteam.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PlainJdbcTechnicalTaskDAO implements BaseDao<TechnicalTask> {
    private static final String INSERT_SQL = "INSERT INTO tasks (title, description) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE tasks SET title=?,description=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM tasks";
    private static final String SELECT_ONE_SQL = "SELECT * FROM tasks WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM tasks WHERE id=?";

    private static final Logger LOGGER = LoggerFactory.getLogger(PlainJdbcTechnicalTaskDAO.class);
    private static final String EXCEPTION_WARN = "An exception occurred!";

    transient private final DataSource dataSource;

    public PlainJdbcTechnicalTaskDAO(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public TechnicalTask create(final TechnicalTask task) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatement(ps, task);
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    final long id = generatedKeys.getLong(1);
                    return new TechnicalTask(id, task);
                } else {
                    final DAOException e = new DAOException("Could not create a Technical task");
                    LOGGER.error(EXCEPTION_WARN, e);
                    throw e;
                }
            }
        } catch (SQLException e) {
            LOGGER.error(EXCEPTION_WARN, e);
            throw new DAOException(e);
        }
    }

    @Override
    @SuppressWarnings("PMD.CloseResource")
    public TechnicalTask get(final long id) {
        try  {
            final Connection conn = dataSource.getConnection();
            final PreparedStatement ps = conn.prepareStatement(SELECT_ONE_SQL);
            ps.setLong(1, id);
            final ResultSet rs = ps.executeQuery();
            try {
                if (rs.next()) {
                    return toTask(rs);
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
    public List<TechnicalTask> getAll() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            final List<TechnicalTask> task = new ArrayList<>();
            while (rs.next()) {
                task.add(toTask(rs));
            }
            return task;
        } catch (SQLException e) {
            LOGGER.error(EXCEPTION_WARN, e);
            throw new DAOException(e);
        }
    }

    @Override
    public boolean update(final TechnicalTask task) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            prepareStatement(ps, task);
            ps.setLong(3, task.getId());
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

    private TechnicalTask toTask(final ResultSet rs) throws SQLException {
        return new TechnicalTask(rs.getLong("id"),
                rs.getString("title"),
                rs.getString("description"));
    }

    private void prepareStatement(final PreparedStatement ps, final TechnicalTask task) throws SQLException {
        ps.setString(1, task.getTitle());
        ps.setString(2, task.getDescription());
    }
}
