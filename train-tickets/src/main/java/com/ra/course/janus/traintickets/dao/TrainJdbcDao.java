package com.ra.course.janus.traintickets.dao;
import com.ra.course.janus.traintickets.entity.Train;
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
public class TrainJdbcDao implements IJdbcDao<Train> {

    private static final String UPDATE_TRAIN =
            "update TRAINS set name = :name, seating = :seating, freeSeats = :freeSeats where id = :id";
    private static final String FIND_BY_ID = "select * from TRAINS where id = :id";
    private static final String DELETE_TRAIN = "delete from TRAINS where id = :id";
    private static final String FIND_TRAINS_ALL = "select * from TRAINS";

    private final transient SimpleJdbcInsert jdbcInsert;
    private final transient NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired()
    public TrainJdbcDao(
            @Qualifier("trainJdbcInsert") final SimpleJdbcInsert jdbcInsert,
            final NamedParameterJdbcTemplate namedJdbcTemplate) {
                this.jdbcInsert = jdbcInsert;
                this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Override
    public Train save(final Train train) {
        final Number id = jdbcInsert.executeAndReturnKey(
                new BeanPropertySqlParameterSource(train));
        return new Train(id.longValue(),train.getName(),
                train.getSeating(),train.getFreeSeats());
    }

    @Override
    public boolean update(final Train train) {
        return namedJdbcTemplate.update
                (UPDATE_TRAIN,new BeanPropertySqlParameterSource(train)) > 0;
    }

    @Override
    public boolean delete(final Long id) {
        return namedJdbcTemplate.update
                (DELETE_TRAIN, new MapSqlParameterSource("id",id)) > 0;
    }

    @Override
    public Train findById(final Long id) {
        return namedJdbcTemplate
                .queryForObject(FIND_BY_ID,
                        new MapSqlParameterSource("id",id),
                        BeanPropertyRowMapper.newInstance(Train.class));
    }

    @Override
    public List<Train> findAll() {
        return namedJdbcTemplate.query(FIND_TRAINS_ALL,
                new MapSqlParameterSource(),
                BeanPropertyRowMapper.newInstance(Train.class));
    }
}

