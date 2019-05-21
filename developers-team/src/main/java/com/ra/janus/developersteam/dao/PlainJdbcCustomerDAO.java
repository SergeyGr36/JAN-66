package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.dao.interfaces.CustomerDAO;
import com.ra.janus.developersteam.entity.Customer;
import com.ra.janus.developersteam.exception.DAOException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlainJdbcCustomerDAO implements CustomerDAO {
    private static final String INSERT_SQL = "INSERT INTO customers (name, address, phone) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE customers SET name=?,address=?,phone=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM customers";
    private static final String SELECT_ONE_SQL = "SELECT * FROM customers WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM customers WHERE id=?";

    transient private final DataSource dataSource;

    public PlainJdbcCustomerDAO(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Customer create(final Customer customer) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatement(ps, customer);
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys();) {
                if (generatedKeys.next()) {
                    final long id = generatedKeys.getLong(1);
                    return new Customer(id, customer);
                } else {
                    throw new DAOException("Couldn't retrieve generated id for customer " + customer);
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

            Customer customer = null;
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                customer = toCustomer(rs);
            }
            rs.close();
            return customer;
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
        return new Customer(rs.getLong("id"),
                rs.getString("name"),
                rs.getString("address"),
                rs.getString("phone"));
    }

    private void prepareStatement(final PreparedStatement ps, final Customer customer) throws SQLException {
        ps.setString(1, customer.getName());
        ps.setString(2, customer.getAddress());
        ps.setString(3, customer.getPhone());
    }
}
