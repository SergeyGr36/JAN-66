package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Qualification;
import com.ra.janus.developersteam.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlainJdbcQualificationDAO implements BaseDao<Qualification> {

    private static final String INSERT_SQL = "INSERT INTO qualifications (name, responsibility) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE qualifications SET name=?,responsibility=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM qualifications";
    private static final String SELECT_ONE_SQL = "SELECT * FROM qualifications WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM qualifications WHERE id=?";

    private static final Logger LOGGER = LoggerFactory.getLogger(PlainJdbcQualificationDAO.class);
    public static final String EXCEPTION_WARN = "An exception occurred!";

    transient private final DataSource dataSource;

    public PlainJdbcQualificationDAO(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Qualification create(final Qualification qualification) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatement(ps, qualification);
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys();) {
                if (generatedKeys.next()) {
                    final long id = generatedKeys.getLong(1);
                    return new Qualification(id, qualification);
                } else {
                    throw new DAOException("Could not create a Qualification");
                }
            }
        } catch (SQLException e) {
            LOGGER.error(EXCEPTION_WARN, e);
            throw new DAOException(e);
        }
    }

    @Override
    @SuppressWarnings("PMD.CloseResource")
    public Qualification get(final long id) {
        try  {
            final Connection conn = dataSource.getConnection();
            final PreparedStatement ps = conn.prepareStatement(SELECT_ONE_SQL);
            ps.setLong(1, id);
            final ResultSet rs = ps.executeQuery();
            try {
                if (rs.next()) {
                    return toQualification(rs);
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
    public List<Qualification> getAll() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            final List<Qualification> qualifications = new ArrayList<>();
            while (rs.next()) {
                qualifications.add(toQualification(rs));
            }
            return qualifications;
        } catch (SQLException e) {
            LOGGER.error(EXCEPTION_WARN, e);
            throw new DAOException(e);
        }
    }

    @Override
    public boolean update(final Qualification qualification) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            prepareStatement(ps, qualification);
            ps.setLong(3, qualification.getId());
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

    private Qualification toQualification(final ResultSet rs) throws SQLException {
        return new Qualification(rs.getLong("id"),
                rs.getString("name"),
                rs.getString("responsibility"));
    }

    private void prepareStatement(final PreparedStatement ps, final Qualification qualification) throws SQLException {
        ps.setString(1, qualification.getName());
        ps.setString(2, qualification.getResponsibility());
    }

}
