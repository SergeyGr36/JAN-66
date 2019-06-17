package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.entity.Admin;
import com.ra.course.janus.traintickets.exception.DAOException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminJdbcDaoMockTest {

    private static final Long ADMIN_ID = 2L;
    private static final String ADMIN_NAME = "Roman";
    private static final String ADMIN_LASTNAME = "Hreits";
    private static final String PASSWORD = "romanHreits";

    private static final Admin ADMIN_TEST = new Admin(ADMIN_ID, ADMIN_NAME, ADMIN_LASTNAME, PASSWORD);
    private static final Long ID_TEST = 5L;

    private SimpleJdbcInsert mockJdbcInsert;
    private NamedParameterJdbcTemplate mockNamedJdbcTemplate;
    private AdminJdbcDao adminDao;


    @BeforeEach
    public void before()  {

        mockJdbcInsert = mock(SimpleJdbcInsert.class);
        mockNamedJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        adminDao = new AdminJdbcDao(mockJdbcInsert, mockNamedJdbcTemplate);
    }

    // Test method Save---------------------------------------------------------
    @Test
    public void whenCallMethodSaveThenReturnNewObjectEqualsWithTestingObject()  {
        // when
        when(mockJdbcInsert.executeAndReturnKey(any(SqlParameterSource.class)))
                .thenReturn(ADMIN_ID);
        // then
        assertEquals(adminDao.save(ADMIN_TEST), ADMIN_TEST);
    }

    // Test method update-------------------------------------------------------------
    @Test
    public void whenCallMethodUpdateThanReturnTrue() {
        // when
        when(mockNamedJdbcTemplate.update(anyString(), any(BeanPropertySqlParameterSource.class)))
                .thenReturn(1);
        // then
        assertTrue(adminDao.update(ADMIN_TEST));
    }

    @Test
    public void whenCallMethodUpdateThanReturnFalse()  {
        // when
        when(mockNamedJdbcTemplate.update(anyString(), any(BeanPropertySqlParameterSource.class)))
                .thenReturn(0);
        // then
        assertFalse(adminDao.update(ADMIN_TEST));
    }

    // Test method delete-------------------------------------------------------------------
    @Test
    public void whenCallDeleteObjectFromDbReturnTrue() {
        // when
        when(mockNamedJdbcTemplate.update(anyString(), any(SqlParameterSource.class))).thenReturn(1);
        // then
        assertTrue(adminDao.delete(ID_TEST));
    }

    @Test
    public void whenCallDeleteObjectFromDbReturnFalse()  {
        // when
        when(mockNamedJdbcTemplate.update(anyString(), any(SqlParameterSource.class))).thenReturn(0);
        // then
        assertFalse(adminDao.delete(ID_TEST));
    }

    // Test method findById-----------------------------------------------------------------------
    @Test
    public void whenCallMethodFindByIdThanReturnNotNullObject() {
        // when
        when(mockNamedJdbcTemplate.queryForObject(anyString(), any(SqlParameterSource.class),
                any(BeanPropertyRowMapper.class))).thenReturn(ADMIN_TEST);
        //then
        assertEquals(adminDao.findById(ADMIN_ID), ADMIN_TEST);
    }

    // Test method findAll-------------------------------------------------------------------

    @Test
    public void whenCallMethodFindAllInDbThenReturnNonEmptyList() {
            // when
            when(mockNamedJdbcTemplate.query(anyString(), any(MapSqlParameterSource.class),
                    any(BeanPropertyRowMapper.class))).thenReturn(List.of(Admin.class));
            // then
            assertFalse(adminDao.findAll().isEmpty());
    }
    @Test
    public void whenCallMethodFindAllInDbThenReturnEmptyList() {
        // when
        when(mockNamedJdbcTemplate.query(anyString(), any(MapSqlParameterSource.class),
                any(BeanPropertyRowMapper.class))).thenReturn(Collections.emptyList());
        // then
        assertTrue(adminDao.findAll().isEmpty());
    }
}