package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.dao.interfaces.TechnicalTaskDAO;
import com.ra.janus.developersteam.entity.TechnicalTask;
import com.ra.janus.developersteam.exception.DAOException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlainJdbcTechnicalTaskDAO implements TechnicalTaskDAO {
    private static final String INSERT_SQL = "INSERT INTO tasks (title, description) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE tasks SET title=?,description=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM tasks";
    private static final String SELECT_ONE_SQL = "SELECT * FROM tasks WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM tasks WHERE id=?";

    private static final Logger LOGGER = LoggerFactory.getLogger(PlainJdbcTechnicalTaskDAO.class);

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
                    throw logAndThrow(new DAOException("Couldn't retrieve generated id for task " + task));
                }
            }
        } catch (SQLException e) {
            throw logAndThrow(new DAOException(e));
        }
    }

    @Override
    public TechnicalTask read(final long id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ONE_SQL)) {
            ps.setLong(1, id);

            TechnicalTask task = null;
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                task = toTask(rs);
            }
            rs.close();
            return task;
        } catch (SQLException e) {
            throw logAndThrow(new DAOException(e));
        }
    }

    @Override
    public List<TechnicalTask> readAll() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            final List<TechnicalTask> task = new ArrayList<>();
            while (rs.next()) {
                task.add(toTask(rs));
            }
            return task;
        } catch (SQLException e) {
            throw logAndThrow(new DAOException(e));
        }
    }

    @Override
    public boolean update(final TechnicalTask task) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            prepareStatement(ps, task);
            final int rowCount = ps.executeUpdate();
            return rowCount != 0;
        } catch (SQLException e) {
            throw logAndThrow(new DAOException(e));
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
            throw logAndThrow(new DAOException(e));
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

    private RuntimeException logAndThrow(RuntimeException ex) {
        LOGGER.error("An exception occurred!", ex);
        return ex;
    }
}
