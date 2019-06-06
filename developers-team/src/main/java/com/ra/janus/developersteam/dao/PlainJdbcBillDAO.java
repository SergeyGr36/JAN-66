package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Bill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PlainJdbcBillDAO implements BaseDao<Bill> {
    private static final String INSERT_SQL = "INSERT INTO bills (docdate) VALUES (?)";
    private static final String UPDATE_SQL = "UPDATE bills SET docdate=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM bills";
    private static final String SELECT_ONE_SQL = "SELECT * FROM bills WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM bills WHERE id=?";

    transient private final JdbcTemplate jdbcTemplate;

    public PlainJdbcBillDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Bill create(final Bill bill) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(INSERT_SQL);
            ps.setDate(1, bill.getDocDate());
            return ps;
        }, keyHolder);
        final long id = keyHolder.getKey().longValue();
        return new Bill(id, bill);
    }

    @Override
    public Bill get(final long id) {
        try {
        return jdbcTemplate.queryForObject(SELECT_ONE_SQL,
                BeanPropertyRowMapper.newInstance(Bill.class), id);}
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Bill> getAll() {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(SELECT_ALL_SQL);
        return rows.stream().map(row -> {
            Bill bill = new Bill();
            bill.setId((long) row.get("id"));
            bill.setDocDate((Date) row.get("docdate"));
            return bill;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean update(final Bill bill) {
        final int rowCount = jdbcTemplate.update(UPDATE_SQL, ps -> {
            prepareStatement(ps, bill);
            ps.setLong(2, bill.getId());
        });
        return rowCount != 0;
    }

    @Override
    public boolean delete(final long id) {
        final int rowCount = jdbcTemplate.update(DELETE_SQL, id);
        return rowCount != 0;
    }

    private Bill toBill(final ResultSet rs) throws SQLException {
        return new Bill(rs.getLong("id"),
                rs.getDate("docDate"));
    }

    private void prepareStatement(final PreparedStatement ps, final Bill bill) throws SQLException {
        ps.setDate(1, bill.getDocDate());
    }
}
