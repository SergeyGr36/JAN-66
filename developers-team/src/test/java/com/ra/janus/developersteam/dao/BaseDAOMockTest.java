package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.BaseEntity;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class BaseDAOMockTest {

    protected JdbcTemplate mockTemplate = mock(JdbcTemplate.class);
    protected Connection mockConnection = mock(Connection.class);
    protected PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
    protected long testId = 1L;

    protected abstract BaseEntity getTestEntity();
    protected abstract BaseDao<BaseEntity> getDAO();
    protected abstract String getInsertSql();
    protected abstract String getUpdateSql();
    protected abstract String getSelectAllSql();
    protected abstract String getSelectOneSql();
    protected abstract String getDeleteSql();
    protected abstract Map<String, Object> getTestEntityMap();

    @Test
    public void whenCreateEntityShouldReturnIt() throws Exception {
        //given
        when(mockConnection.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS))
                .thenReturn(mockPreparedStatement);
        when(mockTemplate.update(any(PreparedStatementCreator.class), any(KeyHolder.class))).thenAnswer(
                new Answer() {
                    public Object answer(InvocationOnMock invocation) throws SQLException {
                        Object[] args = invocation.getArguments();
                        PreparedStatementCreator creator = (PreparedStatementCreator) args[0];
                        creator.createPreparedStatement(mockConnection);

                        KeyHolder holder = (KeyHolder) args[1];
                        Map<String, Object> map = new HashMap<>(1);
                        map.put("Something like a generated key", Long.valueOf(1L));
                        holder.getKeyList().add(map);
                        return 1;
                    }
                });

        //when
        BaseEntity entity = getDAO().create(getTestEntity());

        //then
        assertEquals(getTestEntity(), entity);
    }

    //==============================

    @Test
    public void whenReadEntityFromDbByIdThenReturnIt() throws Exception {
        //given
        when(mockTemplate.queryForObject(eq(getSelectOneSql()), any(BeanPropertyRowMapper.class), eq(testId)))
                .thenReturn(getTestEntity());

        //when
        BaseEntity entity = getDAO().get(testId);

        //then
        assertEquals(testId, entity.getId());
    }

    @Test
    public void whenReadAbsentEntityFromDbByIdThenReturnNull() throws Exception {
        //given
        when(mockTemplate.queryForObject(eq(getSelectOneSql()), any(BeanPropertyRowMapper.class), eq(testId)))
                .thenThrow(new EmptyResultDataAccessException(1));

        //when
        BaseEntity entity = getDAO().get(testId);

        //then
        assertEquals(null, entity);
    }

    @Test
    public void whenReadAllEntitiesFromDbThenReturnNonEmptyList() throws Exception {
        //given
        List<Map<String, Object>> rows = new ArrayList<>();
        rows.add(getTestEntityMap());
        when(mockTemplate.queryForList(getSelectAllSql())).thenReturn(rows);

        //when
        List<BaseEntity> list = getDAO().getAll();

        //then
        assertFalse(list.isEmpty());
    }

    @Test
    public void whenUpdateEntityInDbThenReturnTrue() throws Exception {
        //given
        when(mockTemplate.update(eq(getUpdateSql()), any(PreparedStatementSetter.class))).thenAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                PreparedStatementSetter setter = (PreparedStatementSetter) args[1];
                setter.setValues(mockPreparedStatement);
                return 1;
            }
        });

        //when
        boolean updated = getDAO().update(getTestEntity());

        //then
        assertTrue(updated);
    }

    @Test
    public void whenUpdateEntityInDbThenReturnFalse() throws Exception {
        //given
        when(mockTemplate.update(eq(getUpdateSql()), any(PreparedStatementSetter.class))).thenReturn(0);

        //when
        boolean updated = getDAO().update(getTestEntity());

        //then
        assertFalse(updated);
    }

    @Test
    public void whenDeleteEntityFromDbThenReturnTrue() throws Exception {
        //given
        when(mockTemplate.update(getDeleteSql(), testId)).thenReturn(1);

        //when
        boolean deleted = getDAO().delete(testId);

        //then
        assertTrue(deleted);
    }

    @Test
    public void whenDeleteEntityFromDbThenReturnFalse() throws Exception {
        //given
        when(mockTemplate.update(getDeleteSql(), testId)).thenReturn(0);

        //when
        boolean deleted = getDAO().delete(testId);

        //then
        assertFalse(deleted);
    }

}
