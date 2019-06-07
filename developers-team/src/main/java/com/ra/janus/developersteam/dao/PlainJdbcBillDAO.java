package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Bill;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class PlainJdbcBillDAO implements BaseDao<Bill> {
    private static final String INSERT_SQL = "INSERT INTO bills (docdate) VALUES (?)";
    private static final String UPDATE_SQL = "UPDATE bills SET docdate=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM bills";
    private static final String SELECT_ONE_SQL = "SELECT * FROM bills WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM bills WHERE id=?";
    public static final String PMD_ANOMALY = "PMD.DataflowAnomalyAnalysis";

    transient private final JdbcTemplate jdbcTemplate;

    public PlainJdbcBillDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public Bill create(final Bill bill) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final PreparedStatement ps = connection
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
        final List<Map<String, Object>> rows = jdbcTemplate.queryForList(SELECT_ALL_SQL);
        return rows.stream().map(row -> {
            final Bill bill = new Bill();
            bill.setId((long) row.get("id"));
            bill.setDocDate((Date) row.get("docdate"));
            return bill;
        }).collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public boolean update(final Bill bill) {
        final int rowCount = jdbcTemplate.update(UPDATE_SQL, ps -> {
            ps.setDate(1, bill.getDocDate());
            ps.setLong(2, bill.getId());
        });
        return rowCount != 0;
    }

    @Override
    public boolean delete(final long id) {
        final int rowCount = jdbcTemplate.update(DELETE_SQL, id);
        return rowCount != 0;
    }
}
