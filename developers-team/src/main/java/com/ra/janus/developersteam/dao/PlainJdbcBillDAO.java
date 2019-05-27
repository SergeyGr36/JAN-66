package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.dao.interfaces.BillDAO;
import com.ra.janus.developersteam.entity.Bill;
import com.ra.janus.developersteam.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class PlainJdbcBillDAO implements BillDAO {
    private static final String INSERT_SQL = "INSERT INTO bills (docDate) VALUES (?)";
    private static final String UPDATE_SQL = "UPDATE bills SET docDate=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM bills";
    private static final String SELECT_ONE_SQL = "SELECT * FROM bills WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM bills WHERE id=?";

    private static final Logger LOGGER = LoggerFactory.getLogger(PlainJdbcBillDAO.class);
    private static final String EXCEPTION_WARN = "An exception occurred!";

    transient private final DataSource dataSource;

    public PlainJdbcBillDAO(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Bill create(final Bill bill) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatement(ps, bill);
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys();) {
                if (generatedKeys.next()) {
                    final long id = generatedKeys.getLong(1);
                    return new Bill(id, bill);
                } else {
                    throw new DAOException("Could not create a bill");
                }
            }
        } catch (SQLException e) {
            LOGGER.error(EXCEPTION_WARN, e);
            throw new DAOException(e);
        }
    }

    @Override
    public Bill read(final long id) {
        try  {
            final Connection conn = dataSource.getConnection();
            final PreparedStatement ps = conn.prepareStatement(SELECT_ONE_SQL);
            final ResultSet rs = ps.executeQuery();
            try {
                if (rs.next()) {
                    return toBill(rs);
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
    public List<Bill> readAll() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            final List<Bill> bills = new ArrayList<>();
            while (rs.next()) {
                bills.add(toBill(rs));
            }
            return bills;
        } catch (SQLException e) {
            LOGGER.error(EXCEPTION_WARN, e);
            throw new DAOException(e);
        }
    }

    @Override
    public boolean update(final Bill bill) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            prepareStatement(ps, bill);
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

    private Bill toBill(final ResultSet rs) throws SQLException {
        return new Bill(rs.getLong("id"),
                rs.getDate("docDate"));
    }

    private void prepareStatement(final PreparedStatement ps, final Bill bill) throws SQLException {
        ps.setDate(1, bill.getDocDate());
    }
}
