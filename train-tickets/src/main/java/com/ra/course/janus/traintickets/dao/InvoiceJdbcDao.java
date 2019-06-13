package com.ra.course.janus.traintickets.dao;

import java.util.List;
import javax.sql.DataSource;
import com.ra.course.janus.traintickets.entity.Invoice;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component(value = "InvoiceDAO")
public class InvoiceJdbcDao implements IJdbcDao<Invoice> {
    private static final String INSERT_INTO = "INSERT INTO invoices (price, attributes) VALUES (?, ?)";
    private static final String UPDATE_TABLE = "UPDATE invoices SET price = ?, attributes = ? WHERE id = ?";
    private static final String FIND_BY_ID = "SELECT * FROM invoices WHERE id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM invoices WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM invoices";
    private final transient JdbcTemplate jdbcTemplate;

    public InvoiceJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public Invoice save(final Invoice item) {
       final Invoice invoice = jdbcTemplate.queryForObject(INSERT_INTO,
               new Object[]{item.getPrice(), item.getAttributes()}, new BeanPropertyRowMapper<>(Invoice.class));
        return invoice;
    }

    @Override
    public boolean update(final Invoice item) {
        return jdbcTemplate.update(UPDATE_TABLE, new Object[]{item.getPrice(),
        item.getAttributes(), item.getId()})>0;
    }

    @Override
    public boolean delete(final Long id) {
        return jdbcTemplate.update(DELETE_BY_ID, new Object[]{id})>0;
    }

    @Override
    public Invoice findById(final Long id) {
        final Invoice invoice = jdbcTemplate.queryForObject(FIND_BY_ID,
                new Object[]{id}, new BeanPropertyRowMapper<>(Invoice.class));
        return invoice;
    }

    @Override
    public List<Invoice> findAll() {
        final List<Invoice> invoices = jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Invoice.class));
        return invoices;
    }
}
