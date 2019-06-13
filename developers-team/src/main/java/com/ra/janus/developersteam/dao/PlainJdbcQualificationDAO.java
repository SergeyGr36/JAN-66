package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Qualification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PlainJdbcQualificationDAO implements BaseDao<Qualification> {

    private static final String INSERT_SQL = "INSERT INTO qualifications (name, responsibility) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE qualifications SET name=?,responsibility=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM qualifications";
    private static final String SELECT_ONE_SQL = "SELECT * FROM qualifications WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM qualifications WHERE id=?";

    transient private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PlainJdbcQualificationDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Qualification create(final Qualification qualification) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                final PreparedStatement ps = connection
                        .prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, qualification.getName());
                ps.setString(2, qualification.getResponsibility());
                return ps;
            }
        }, keyHolder);
        final long id = keyHolder.getKey().longValue();
        return new Qualification(id, qualification);
    }

    @Override
    public Qualification get(final long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_ONE_SQL,
                    BeanPropertyRowMapper.newInstance(Qualification.class), id);}
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Qualification> getAll() {
        final List<Map<String, Object>> rows = jdbcTemplate.queryForList(SELECT_ALL_SQL);
        return rows.stream().map(row -> {
            final Qualification qualification = new Qualification();
            qualification.setId((long) row.get("id"));
            qualification.setName((String) row.get("name"));
            qualification.setResponsibility((String) row.get("responsibility"));
            return qualification;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean update(final Qualification qualification) {
        final int rowCount = jdbcTemplate.update(UPDATE_SQL, new PreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps) throws SQLException {
                ps.setString(1, qualification.getName());
                ps.setString(2, qualification.getResponsibility());
                ps.setLong(3, qualification.getId());

            }
        });
        return rowCount != 0;
    }

    @Override
    public boolean delete(final long id) {
        final int rowCount = jdbcTemplate.update(DELETE_SQL, id);
        return rowCount != 0;
    }
}
