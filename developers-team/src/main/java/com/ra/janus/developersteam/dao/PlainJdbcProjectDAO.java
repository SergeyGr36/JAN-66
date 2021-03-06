package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Project;
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
public class PlainJdbcProjectDAO implements BaseDao<Project> {

    private static final String INSERT_SQL = "INSERT INTO projects (name, description, status, eta) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE projects SET name=?,description=?,status=?,eta=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM projects";
    private static final String SELECT_ONE_SQL = "SELECT * FROM projects WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM projects WHERE id=?";

    transient private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PlainJdbcProjectDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Project create(final Project project) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                final PreparedStatement ps = connection
                        .prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, project.getName());
                ps.setString(2, project.getDescription());
                ps.setString(3, project.getStatus());
                ps.setDate(4, project.getEta());
                return ps;
            }
        }, keyHolder);
        final long id = keyHolder.getKey().longValue();
        return new Project(id, project);
    }

    @Override
    public Project get(final long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_ONE_SQL,
                    BeanPropertyRowMapper.newInstance(Project.class), id);}
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Project> getAll() {
        final List<Map<String, Object>> rows = jdbcTemplate.queryForList(SELECT_ALL_SQL);
        return rows.stream().map(row -> {
            final Project project = new Project();
            project.setId((long) row.get("id"));
            project.setName((String) row.get("name"));
            project.setDescription((String) row.get("description"));
            project.setStatus((String) row.get("status"));
            project.setEta((Date) row.get("eta"));
            return project;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean update(final Project project) {
        final int rowCount = jdbcTemplate.update(UPDATE_SQL, new PreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps) throws SQLException {
                ps.setString(1, project.getName());
                ps.setString(2, project.getDescription());
                ps.setString(3, project.getStatus());
                ps.setDate(4, project.getEta());
                ps.setLong(5, project.getId());

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
