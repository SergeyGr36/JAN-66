package com.ra.course.janus.traintickets.dao;
import com.ra.course.janus.traintickets.MainSpringConfig;
import com.ra.course.janus.traintickets.entity.Train;
import com.ra.course.janus.traintickets.exception.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import javax.swing.plaf.basic.BasicTreeUI;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import static com.ra.course.janus.traintickets.exception.ErrorMessages.*;

@Component
public class TrainJdbcDao implements IJdbcDao<Train> {

    private static final String UPDATE_TRAIN =
            "update TRAINS set name = :name, seating = :seating, freeSeats = :freeSeats where id = :id";
    private static final String SELECT_WHEN_ID = "select * from TRAINS where id = :id";
    private static final String DELETE_TRAIN = "delete from TRAINS where id = :id";
    private static final String SELECT_TRAIN_ALL = "select * from TRAINS";

    private final transient SimpleJdbcInsert jdbcInsert;
    private final transient NamedParameterJdbcTemplate namedJdbcTemplate;

    public TrainJdbcDao(final DataSource dataSource) {
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("TRAINS")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Train save(final Train train) {
        Number id = jdbcInsert.executeAndReturnKey(paramSourceFromTrain(train));
        return new Train(id.longValue(),train.getName(),
                train.getSeating(),train.getFreeSeats());
    }

    @Override
    public boolean update(final Train train) {
        return namedJdbcTemplate.update
                (UPDATE_TRAIN,paramSourceFromTrain(train)) > 0;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public Train findById(Long id) {
        return null;
    }

    @Override
    public List<Train> findAll() {
        return null;
    }

   private SqlParameterSource paramSourceFromTrain(final Train train){
        return new BeanPropertySqlParameterSource(train);
   }

   private SqlParameterSource paramSourceFromId(final Long id){
        return new MapSqlParameterSource("id",id);
   }
}

