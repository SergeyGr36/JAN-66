package com.ra.course.janus.traintickets.dao;

import java.util.List;

import com.ra.course.janus.traintickets.entity.Invoice;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component(value = "InvoiceDAO")
public class InvoiceJdbcDao implements IJdbcDao<Invoice> {
    private static final String UPDATE_TABLE = "UPDATE invoices SET price = :price, " +
            "attributes = :attributes WHERE id = :id";
    private static final String FIND_BY_ID = "SELECT * FROM invoices WHERE id = :id";
    private static final String DELETE_BY_ID = "DELETE FROM invoices WHERE id = :id";
    private static final String FIND_ALL = "SELECT * FROM invoices";

    private final transient SimpleJdbcInsert jdbcInsert;

    private final transient NamedParameterJdbcTemplate namedJdbcTemplate;

    public InvoiceJdbcDao(@Qualifier("invoiceJdbcInsert") final SimpleJdbcInsert jdbcInsert,
                          final NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.jdbcInsert = jdbcInsert;
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Override
    public Invoice save(final Invoice item) {
        final Number id = jdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(item));
        return new Invoice(id.longValue(), item.getPrice(), item.getAttributes());
    }

    @Override
    public boolean update(final Invoice item) {
        return namedJdbcTemplate.update(UPDATE_TABLE, new BeanPropertySqlParameterSource(item)) > 0;
    }

    @Override
    public boolean delete(final Long id) {
        return namedJdbcTemplate.update(DELETE_BY_ID, new MapSqlParameterSource("id", id)) > 0;
    }

    @Override
    public Invoice findById(final Long id) {
        return namedJdbcTemplate.queryForObject(FIND_BY_ID, new MapSqlParameterSource("id", id),
                new BeanPropertyRowMapper<>(Invoice.class));
    }

    @Override
    public List<Invoice> findAll() {
        return namedJdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Invoice.class));
    }
}
