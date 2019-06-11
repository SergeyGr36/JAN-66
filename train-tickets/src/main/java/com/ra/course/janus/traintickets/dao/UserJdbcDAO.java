package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.entity.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserJdbcDAO implements IJdbcDao<User> {
    private static final String SAVE_USER = "insert into USERS (name,email,password) values (?,?,?)";
    private static final String UPDATE_USER = "update USERS set name=?, email=?, password=? WHERE id=?";
    private static final String DELETE_USER = "delete from USERS where id=?";
    private static final String FIND_BY_ID = "select * from USERS where id=?";
    private static final String FIND_ALL = "select * from USERS";

    private final transient JdbcTemplate jdbcTemplate;

    public UserJdbcDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User save(final User user) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                conn -> {
                    final PreparedStatement ps = conn.prepareStatement(SAVE_USER, Statement.RETURN_GENERATED_KEYS);
                    prepareStatementWithUser(ps, user);
                    return ps;
                },
                keyHolder
        );

        final long id = keyHolder.getKey().longValue();

        return new User(
                id,
                user.getName(),
                user.getEmail(),
                user.getPassword()
        );
    }

    @Override
    public boolean update(final User user) {
        final int rowCount = jdbcTemplate.update(UPDATE_USER, findStmt -> {
            prepareStatementWithUser(findStmt, user);
            findStmt.setLong(4, user.getId());
        });
        return rowCount > 0;
    }


    @Override
    public boolean delete(final Long id) {
        final int rowCount = jdbcTemplate.update(DELETE_USER, id);
        return rowCount > 0;
    }

    @Override
    public User findById(final Long id) {
        return jdbcTemplate.queryForObject(FIND_BY_ID,
                BeanPropertyRowMapper.newInstance(User.class), id);
    }

    @Override
    public List<User> findAll() {
        final List<Map<String, Object>> rows = jdbcTemplate.queryForList(FIND_ALL);
        return rows.stream()
                .map(this::mapToUser)
                .collect(Collectors.toList());
    }

    private void prepareStatementWithUser(final PreparedStatement ps, final User user) throws SQLException {
        ps.setString(1, user.getName());
        ps.setString(2, user.getEmail());
        ps.setString(3, user.getPassword());
    }

    private User mapToUser(final Map<String, Object>  userMap)  {
        return new User(
                (Long)   userMap.get("id"),
                (String) userMap.get("name"),
                (String) userMap.get("email"),
                (String) userMap.get("password")
        );
    }
}