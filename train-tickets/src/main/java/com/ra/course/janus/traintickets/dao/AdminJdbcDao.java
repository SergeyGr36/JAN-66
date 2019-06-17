package com.ra.course.janus.traintickets.dao;


import com.ra.course.janus.traintickets.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminJdbcDao implements IJdbcDao<Admin> {

    private static final String UPDATE_ADMINS =
            "update ADMINS set name = :name, lastName = :lastName, password = :password WHERE id = :id";
    private static final String DELETE_ADMINS = "delete from ADMINS where id = :id";
    private static final String FIND_BY_ID = "select * from ADMINS where id = :id";
    private static final String FIND_ALL = "select * from ADMINS";


    private final transient SimpleJdbcInsert jdbcInsert;

    private final transient NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired()
    public AdminJdbcDao(
            @Qualifier("adminJdbcInsert") final SimpleJdbcInsert jdbcInsert,
            final NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.jdbcInsert = jdbcInsert;
        this.namedJdbcTemplate = namedJdbcTemplate;
    }


    @Override
    public Admin save(final Admin item) {
        final Long number = (Long) jdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(item));
        return new Admin(number, item.getName(), item.getLastName(),
                item.getPassword());
    }

    @Override
    public boolean update(final Admin item) {
        final int update = namedJdbcTemplate.update(UPDATE_ADMINS, new BeanPropertySqlParameterSource(item));
        return update>0;
    }

    @Override
    public boolean delete(final Long id) {
        final int update = namedJdbcTemplate.update(DELETE_ADMINS, new MapSqlParameterSource("id", id));
        return update>0;
    }

    @Override
    public Admin findById(final Long id) {
        return namedJdbcTemplate.queryForObject(FIND_BY_ID,
                new MapSqlParameterSource("id", id),
                BeanPropertyRowMapper.newInstance(Admin.class));
    }

    @Override
    public List<Admin> findAll() {
        return namedJdbcTemplate.query(FIND_ALL, new MapSqlParameterSource(),
                BeanPropertyRowMapper.newInstance(Admin.class));

    }
}
