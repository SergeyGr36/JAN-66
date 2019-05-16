package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.dao.interfaces.TechnicalTaskDAO;
import com.ra.janus.developersteam.entity.Customer;
import com.ra.janus.developersteam.entity.TechnicalTask;
import com.ra.janus.developersteam.exception.DAOException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlainJdbcTechnicalTaskDAO implements TechnicalTaskDAO {
    private static final String INSERT_SQL = "INSERT INTO CUSTOMERS (NAME, ADDRESS, PHONE) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE CUSTOMERS SET NAME=?,ADDRESS=?,PHONE=? WHERE ID=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM CUSTOMERS";
    private static final String SELECT_ONE_SQL = "SELECT * FROM CUSTOMERS WHERE ID = ?";
    private static final String DELETE_SQL = "DELETE FROM CUSTOMERS WHERE ID=?";

    transient private final DataSource dataSource;

    public PlainJdbcTechnicalTaskDAO(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public long create(final TechnicalTask customer) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatement(ps, customer);
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys();) {
                if (generatedKeys.next()) {
                    final long id = generatedKeys.getLong(1);
                    customer.setId(id);

                    return id;
                } else {
                    throw new IllegalStateException("Couldn't retrieve generated id for customer " + customer);
                }
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Customer read(final long id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ONE_SQL)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return toCustomer(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public List<Customer> readAll() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            final List<Customer> customers = new ArrayList<>();
            while (rs.next()) {
                customers.add(toCustomer(rs));
            }
            return customers;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public boolean update(final Customer customer) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            prepareStatement(ps, customer);
            final int rowCount = ps.executeUpdate();
            return rowCount != 0;
        } catch (SQLException e) {
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
            throw new DAOException(e);
        }
    }

    private Customer toCustomer(final ResultSet rs) throws SQLException {
        return new Customer(rs.getLong("ID"),
                rs.getString("NAME"),
                rs.getString("ADDRESS"),
                rs.getString("PHONE"));
    }

    private void prepareStatement(final PreparedStatement ps, final Customer customer) throws SQLException {
        ps.setString(1, customer.getName());
        ps.setString(2, customer.getAddress());
        ps.setString(3, customer.getPhone());
    }
}
