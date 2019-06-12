package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class UserJdbcDAO implements IJdbcDao<User> {
    private static final String UPDATE_USER =
            "update USERS set name = :name, email = :email, password = :password WHERE id = :id";
    private static final String DELETE_USER = "delete from USERS where id = :id";
    private static final String FIND_BY_ID = "select * from USERS where id = :id";
    private static final String FIND_ALL = "select * from USERS";

    private final transient SimpleJdbcInsert jdbcInsert;
    private final transient NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    public UserJdbcDAO(final DataSource dataSource) {
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public User save(final User user) {
        Number id = jdbcInsert.executeAndReturnKey(paramSourceFrom(user));
        return new User(id.longValue(), user.getName(),
                user.getEmail(), user.getPassword()
        );
    }

    @Override
    public boolean update(final User user) {
        final int rowsAffected = namedJdbcTemplate.update(
                UPDATE_USER,
                paramSourceFrom(user)
        );
        return rowsAffected > 0;
    }

    @Override
    public boolean delete(final Long id) {
        final int rowsAffected = namedJdbcTemplate.update(
                DELETE_USER,
                paramSourceFrom(id));
        return rowsAffected > 0;
    }

    @Override
    public User findById(final Long id) {
        return namedJdbcTemplate.queryForObject(
                FIND_BY_ID,
                paramSourceFrom(id),
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

    private SqlParameterSource paramSourceFrom(final User user) {
        return new BeanPropertySqlParameterSource(user);
   }

   private SqlParameterSource paramSourceFrom(final Long id) {
        return new MapSqlParameterSource("id", id);
   }

}

