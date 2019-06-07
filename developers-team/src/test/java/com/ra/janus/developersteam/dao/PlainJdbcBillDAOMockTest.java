package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Bill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlainJdbcBillDAOMockTest {
    private static final String INSERT_SQL = "INSERT INTO bills (docdate) VALUES (?)";
    private static final String UPDATE_SQL = "UPDATE bills SET docdate=? WHERE id=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM bills";
    private static final String SELECT_ONE_SQL = "SELECT * FROM bills WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM bills WHERE id=?";
    private static final long TEST_ID = 1L;
    private static final Bill TEST_BILL = new Bill(TEST_ID, new Date(System.currentTimeMillis()));

    private JdbcTemplate mockTemplate = mock(JdbcTemplate.class);
    private KeyHolder mockKeyHolder = mock(KeyHolder.class);
    private PreparedStatementCreator mockPSC = mock(PreparedStatementCreator.class);

    private PlainJdbcBillDAO billDAO;

    @BeforeEach
    void before() throws Exception {
        billDAO = new PlainJdbcBillDAO(mockTemplate);

    }

    @Test
    void whenCreateBillShouldReturnBill() throws Exception {
        //given
        when(mockTemplate.update(any(PreparedStatementCreator.class), any(KeyHolder.class))).thenAnswer(
                new Answer() {
                    public Object answer(InvocationOnMock invocation) {
                        Object[] args = invocation.getArguments();
                        KeyHolder holder = (KeyHolder) args[1];
                        Map<String, Object> map = new HashMap<>(1);
                        map.put("Something like a generated key", Long.valueOf(1L));
                        holder.getKeyList().add(map);
                        return 1;
                    }
                });

        //when
        Bill bill = billDAO.create(TEST_BILL);

        //then
        assertEquals(TEST_BILL, bill);
    }

    //==============================

    @Test
    void whenReadBillFromDbByIdThenReturnIt() throws Exception {
        //given
        when(mockTemplate.queryForObject(eq(SELECT_ONE_SQL), any(BeanPropertyRowMapper.class), eq(TEST_ID)))
                .thenReturn(TEST_BILL);

        //when
        Bill bill = billDAO.get(TEST_ID);

        //then
        assertEquals(TEST_ID, bill.getId());
    }

    @Test
    void whenReadAbsentBillFromDbByIdThenReturnNull() throws Exception {
        //given
        when(mockTemplate.queryForObject(eq(SELECT_ONE_SQL), any(BeanPropertyRowMapper.class), eq(TEST_ID)))
                .thenThrow(new EmptyResultDataAccessException(1));

        //when
        Bill bill = billDAO.get(TEST_ID);

        //then
        assertEquals(null, bill);
    }

    @Test
    void whenReadAllBillsFromDbThenReturnNonEmptyList() throws Exception {
        //given
        Map<String, Object> testMap = new HashMap<>(1);
        testMap.put("id", TEST_BILL.getId());
        testMap.put("docdate", TEST_BILL.getDocDate());
        List<Map<String, Object>> rows = new ArrayList<>();
        rows.add(testMap);
        when(mockTemplate.queryForList(SELECT_ALL_SQL)).thenReturn(rows);

        //when
        List<Bill> list = billDAO.getAll();

        //then
        assertFalse(list.isEmpty());
    }

    @Test
    void whenUpdateBillInDbThenReturnTrue() throws Exception {
        //given
        when(mockTemplate.update(eq(UPDATE_SQL), any(PreparedStatementSetter.class))).thenReturn(1);

        //when
        boolean updated = billDAO.update(TEST_BILL);

        //then
        assertTrue(updated);
    }

    @Test
    void whenUpdateBillInDbThenReturnFalse() throws Exception {
        //given
        when(mockTemplate.update(eq(UPDATE_SQL), any(PreparedStatementSetter.class))).thenReturn(0);

        //when
        boolean updated = billDAO.update(TEST_BILL);

        //then
        assertFalse(updated);
    }

    @Test
    void whenDeleteBillFromDbThenReturnTrue() throws Exception {
        //given
        when(mockTemplate.update(DELETE_SQL, TEST_ID)).thenReturn(1);

        //when
        boolean deleted = billDAO.delete(TEST_ID);

        //then
        assertTrue(deleted);
    }

    @Test
    void whenDeleteBillFromDbThenReturnFalse() throws Exception {
        //given
        when(mockTemplate.update(DELETE_SQL, TEST_ID)).thenReturn(0);

        //when
        boolean deleted = billDAO.delete(TEST_ID);

        //then
        assertFalse(deleted);
    }
}