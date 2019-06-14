package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.entity.Train;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import java.sql.*;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainJdbcDaoMockTest {

    private static final long TEST_ID = 1L;
    private static final String TEST_NAME = "Test Train Name";
    private static final int TEST_SEATING = 100;
    private static final int TEST_FREE_SEATS = 90;
    private static final Train TEST_TRAIN = new Train(TEST_ID,TEST_NAME,TEST_SEATING,TEST_FREE_SEATS);

    private TrainJdbcDao trainDAO;
    private SimpleJdbcInsert mockJdbcInsert;
    private NamedParameterJdbcTemplate mockNamedJdbcTemplate;

    @BeforeEach
    public void setUp(){
        mockJdbcInsert = mock(SimpleJdbcInsert.class);
        mockNamedJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        trainDAO = new TrainJdbcDao(mockJdbcInsert,mockNamedJdbcTemplate);
    }

    // Test SAVE --------------------------------------------------------------
    @Test
    public void whenWeDoSaveTheObjectInDB () throws SQLException{
        when(mockJdbcInsert.executeAndReturnKey(any(SqlParameterSource.class)))
                .thenReturn(TEST_ID);

        final Train someTrain = trainDAO.save(TEST_TRAIN);

        assertEquals(TEST_TRAIN, someTrain);
    }

    //Testes UPDATE------------------------------------------------------------------
    @Test
    public void whenWeDoUPDATEtheObjectInDbAndAsReturnTrue() throws SQLException{
        when(mockNamedJdbcTemplate
                .update(any(String.class),
                        any(BeanPropertySqlParameterSource.class))).thenReturn(1);

        assertTrue(trainDAO.update(TEST_TRAIN));
    }

    @Test
    public void whenWeDoUPDATEtheObjectInDbAndAsReturnFalse() throws SQLException{
        when(mockNamedJdbcTemplate
                .update(any(String.class),
                        any(BeanPropertySqlParameterSource.class))).thenReturn(0);

        assertFalse(trainDAO.update(TEST_TRAIN));
    }

    //Testes DELETE------------------------------------------------------------------------
    @Test
    public void whenWeDoDELETEtheObjectInDbAndAsReturnTrue()throws SQLException{
        when(mockNamedJdbcTemplate.
                update(any(String.class),
                        any(MapSqlParameterSource.class))).thenReturn(1);

        assertTrue(trainDAO.delete(TEST_ID));
    }

    @Test
    public void whenWeDoDELETEtheObjectInDbAndAsReturnFalse()throws SQLException{
        when(mockNamedJdbcTemplate.
                update(any(String.class),
                        any(MapSqlParameterSource.class))).thenReturn(0);

        assertFalse(trainDAO.delete(TEST_ID));
    }

    //Testes FIND_BY_ID-------------------------------------------------------------------
    @Test
    public void whenWeDoFINDbyIDtheObjectInDbAndWeFoundTrain()throws SQLException{
        when(mockNamedJdbcTemplate.queryForObject(
                anyString(),
                any(MapSqlParameterSource.class),
                any(BeanPropertyRowMapper.class)))
                .thenReturn(TEST_TRAIN);

        final Train someTrain = trainDAO.findById(TEST_ID);

        assertEquals(TEST_TRAIN,someTrain);
    }

    //Test FIND_ALL--------------------------------------------------------------
    @Test
    public void findAllTrainsIfOneTrainFoundThenReturnsTheListWithOneTrain(){

        List<Train>expectedTrains = Collections.singletonList(TEST_TRAIN);

        when(mockNamedJdbcTemplate
                .query(anyString(),
                        any(MapSqlParameterSource.class),
                        any(BeanPropertyRowMapper.class)))
                .thenReturn(Collections.singletonList(TEST_TRAIN));

        List<Train>someTrains = trainDAO.findAll();

        assertEquals(expectedTrains,someTrains);
    }
}