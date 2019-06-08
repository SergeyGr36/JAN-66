package com.ra.course.janus.traintickets.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.ra.course.janus.traintickets.entity.Invoice;
import com.ra.course.janus.traintickets.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvoiceJdbcDao implements IJdbcDao<Invoice> {
    private static final String INSERT_INTO = "INSERT INTO invoices (price, attributes) VALUES (?, ?)";
    private static final String UPDATE_TABLE = "UPDATE invoices SET price = ?, attributes = ? WHERE id = ?";
    private static final String FIND_BY_ID = "SELECT * FROM invoices WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM invoices WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM invoices";
    private static final Logger LOGER = LoggerFactory.getLogger(InvoiceJdbcDao.class);
    private transient final DataSource ds;
    private static final String LOG_MESSAGE = "failed operation";
    private static final String CANT_CLOSE = "Can`t close connection";

    public InvoiceJdbcDao(final DataSource ds) {
        this.ds = ds;
    }

    @Override
    public Invoice save(final Invoice item) {
        try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(INSERT_INTO)) {
            ps.setDouble(1, item.getPrice());
            ps.setString(2, item.getAttributes());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    final long temp = rs.getLong(1);
                    return new Invoice(temp, item.getPrice(), item.getAttributes());
                } else {
                    throw new DAOException("Can`t save Invoice into table");
                }
            }
        } catch (SQLException e) {
            LOGER.error(LOG_MESSAGE, e);
            throw new DAOException(CANT_CLOSE, e);
        }
    }

    @Override
    public boolean update(final Long id, final Invoice item) {
        try (Connection con = ds.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(UPDATE_TABLE)) {
                ps.setDouble(1, item.getPrice());
                ps.setString(2, item.getAttributes());
                ps.setLong(3, item.getId());
                return ps.executeUpdate() == 1;
            }

        } catch (SQLException e) {
            LOGER.error(LOG_MESSAGE, e);
            throw new DAOException(CANT_CLOSE, e);
        }
    }

    @Override
    public boolean delete(final Long id) {
        try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(DELETE_BY_ID)) {
            ps.setLong(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGER.error(LOG_MESSAGE, e);
            throw new DAOException(CANT_CLOSE, e);
        }
    }

    @Override
    public Invoice findById(final Long id) {
        try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(FIND_BY_ID)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return createInvoice(rs);
                } else {
                    throw new DAOException("Can`t find invoice with this ID");
                }

            }
        } catch (SQLException e) {

            LOGER.error(LOG_MESSAGE, e);
            throw new DAOException(CANT_CLOSE, e);
        }
    }

    @Override
    public List<Invoice> findAll() {
        final List<Invoice> invoices = new ArrayList<>();
        try (Connection con = ds.getConnection()) {
            try (Statement st = con.createStatement()) {
                try (ResultSet rs = st.executeQuery(FIND_ALL)) {
                    while (rs.next()) {
                        invoices.add(createInvoice(rs));
                    }
                }
            }

            return invoices;

        } catch (SQLException e) {
            LOGER.error(LOG_MESSAGE, e);
            throw new DAOException(CANT_CLOSE, e);
        }
    }

    private Invoice createInvoice(final ResultSet rs) throws SQLException {
        return new Invoice(rs.getLong(1), rs.getDouble(2), rs.getString(3));
    }
}
