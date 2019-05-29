package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Project;
import com.ra.janus.developersteam.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlainJdbcProjectDAO implements BaseDao<Project> {

    private static final String INSERT_SQL = "INSERT INTO projects (name, description, status, eta) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE projects SET name=?,description=?,status=?,eta=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM projects";
    private static final String SELECT_ONE_SQL = "SELECT * FROM projects WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM projects WHERE id=?";

    private static final Logger LOGGER = LoggerFactory.getLogger(PlainJdbcProjectDAO.class);
    public static final String EXCEPTION_WARN = "An exception occurred!";

    transient private final DataSource dataSource;

    public PlainJdbcProjectDAO(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Project create(final Project project) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatement(ps, project);
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys();) {
                if (generatedKeys.next()) {
                    final long id = generatedKeys.getLong(1);
                    return new Project(id, project);
                } else {
                    throw new DAOException("Could not create a Project");
                }
            }
        } catch (SQLException e) {
            LOGGER.error(EXCEPTION_WARN, e);
            throw new DAOException(e);
        }
    }

    @Override
    @SuppressWarnings("PMD.CloseResource")
    public Project get(final long id) {
        try  {
            final Connection conn = dataSource.getConnection();
            final PreparedStatement ps = conn.prepareStatement(SELECT_ONE_SQL);
            ps.setLong(1, id);
            final ResultSet rs = ps.executeQuery();
            try {
                if (rs.next()) {
                    return toProject(rs);
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
    public List<Project> getAll() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            final List<Project> projects = new ArrayList<>();
            while (rs.next()) {
                projects.add(toProject(rs));
            }
            return projects;
        } catch (SQLException e) {
            LOGGER.error(EXCEPTION_WARN, e);
            throw new DAOException(e);
        }
    }

    @Override
    public boolean update(final Project project) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            prepareStatement(ps, project);
            ps.setLong(5, project.getId());
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

    private Project toProject(final ResultSet rs) throws SQLException {
        return new Project(rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("status"),
                rs.getString("eta"));
    }

    private void prepareStatement(final PreparedStatement ps, final Project project) throws SQLException {
        ps.setString(1, project.getName());
        ps.setString(2, project.getDescription());
        ps.setString(3, project.getStatus());
        ps.setString(4, project.getEta());
    }
}
