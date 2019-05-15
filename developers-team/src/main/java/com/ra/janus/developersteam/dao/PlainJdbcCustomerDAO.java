package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.dao.interfaces.ConnectionFactory;
import com.ra.janus.developersteam.dao.interfaces.CustomerDAO;
import com.ra.janus.developersteam.entity.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlainJdbcCustomerDAO implements CustomerDAO {
    private static final String INSERT_SQL = "INSERT INTO CUSTOMERS (NAME, ADDRESS, PHONE) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE CUSTOMERS SET NAME=?,ADDRESS=?,PHONE=? WHERE ID=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM CUSTOMERS";
    private static final String SELECT_ONE_SQL = "SELECT * FROM CUSTOMERS WHERE ID = ?";
    private static final String DELETE_SQL = "DELETE FROM CUSTOMERS WHERE ID=?";

    private final ConnectionFactory connectionFactory;

    public PlainJdbcCustomerDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Customer create(Customer customer) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatement(ps, customer);
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                long id = generatedKeys.getLong(1);
                customer.setId(id);
                return customer;
            } else {
                throw new IllegalStateException("Couldn't retreive generated id for customer " + customer);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Customer read(long id) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ONE_SQL)) {
            ps.setLong(1, id);

            Customer customer = null;
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    customer = toCustomer(rs);
                }
            }
            return customer;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customer> readAll() {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            List<Customer> customers = new ArrayList<>();
            while (rs.next()) {
                customers.add(toCustomer(rs));
            }
            return customers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Customer entity) {
        return false;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }

    private Customer toCustomer(ResultSet rs) throws SQLException {
        return new Customer(rs.getLong("ID"),
                rs.getString("NAME"),
                rs.getString("ADDRESS"),
                rs.getString("PHONE"));
    }

    private void prepareStatement(PreparedStatement ps, Customer customer) throws SQLException {
        ps.setString(1, customer.getName());
        ps.setString(2, customer.getAddress());
        ps.setString(3, customer.getPhone());
    }
}
