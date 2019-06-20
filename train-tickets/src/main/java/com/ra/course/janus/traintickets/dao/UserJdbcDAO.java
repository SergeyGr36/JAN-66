package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.entity.User;
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
public class UserJdbcDAO implements IJdbcDao<User> {
    private static final String UPDATE_USER =
            "update USERS set name = :name, email = :email, password = :password WHERE id = :id";
    private static final String DELETE_USER = "delete from USERS where id = :id";
    private static final String FIND_BY_ID = "select * from USERS where id = :id";
    private static final String FIND_ALL = "select * from USERS";
    public static final String ID = "id";

    private final transient SimpleJdbcInsert jdbcInsert;

    private final transient NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired()
    public UserJdbcDAO(
            @Qualifier("userJdbcInsert") final SimpleJdbcInsert jdbcInsert,
            final NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.jdbcInsert = jdbcInsert;
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Override
    public User save(final User user) {
        final Number id = jdbcInsert.executeAndReturnKey(
                new BeanPropertySqlParameterSource(user)
        );
        return new User(id.longValue(), user);
    }

    @Override
    public boolean update(final User user) {
        final int rowsAffected = namedJdbcTemplate.update(
                UPDATE_USER,
                new BeanPropertySqlParameterSource(user)
        );
        return rowsAffected > 0;
    }

    @Override
    public boolean delete(final Long id) {
        final int rowsAffected = namedJdbcTemplate.update(
                DELETE_USER,
                new MapSqlParameterSource(ID, id)
        );
        return rowsAffected > 0;
    }

    @Override
    public User findById(final Long id) {
        return namedJdbcTemplate.queryForObject(
                FIND_BY_ID,
                new MapSqlParameterSource(ID, id),
                BeanPropertyRowMapper.newInstance(User.class)
        );
    }

    @Override
    public List<User> findAll() {
        return namedJdbcTemplate.query(
                FIND_ALL,
                new MapSqlParameterSource(),
                BeanPropertyRowMapper.newInstance(User.class)
        );
    }
}

