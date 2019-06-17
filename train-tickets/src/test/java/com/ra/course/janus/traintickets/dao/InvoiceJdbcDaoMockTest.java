package com.ra.course.janus.traintickets.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import com.ra.course.janus.traintickets.entity.Invoice;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;


class InvoiceJdbcDaoMockTest {

    private final long ID = 1;
    private Invoice invoice;
    private InvoiceJdbcDao invoiceDao;
    private final transient SimpleJdbcInsert mockJdbcInsert = mock(SimpleJdbcInsert.class) ;
    private final transient NamedParameterJdbcTemplate mockNamedJdbcTemplate = mock(NamedParameterJdbcTemplate.class);

    @BeforeEach
    public void before() {
        invoiceDao = new InvoiceJdbcDao(mockJdbcInsert, mockNamedJdbcTemplate);
        invoice = new Invoice(ID, null, null);
    }
//    save
    @Test
    public void whenInputInvoiceThanSaveThatOneAndReturnWithNewKey(){
        when(mockJdbcInsert.executeAndReturnKey(any(BeanPropertySqlParameterSource.class))).thenReturn(ID);
        Invoice temp = invoiceDao.save(invoice);
        assertEquals(invoice, temp);
    }

// update true
    @Test
    public void updateInvoiceByIDThenReturnTrue(){
        when(mockNamedJdbcTemplate.update(anyString(), any(BeanPropertySqlParameterSource.class))).thenReturn(1);
        assertTrue(() -> invoiceDao.update(invoice));
    }
// update false
    @Test
    public void updateInvoiceByIDThenReturnFalse(){
        when(mockNamedJdbcTemplate.update(anyString(), any(BeanPropertySqlParameterSource.class))).thenReturn(0);
        assertFalse(() -> invoiceDao.update(invoice));
    }
// delete true
    @Test
    public void testDeleteInvoiceFromDBByIdThenReturnTrue(){
        when(mockNamedJdbcTemplate.update(anyString(), any(MapSqlParameterSource.class))).thenReturn(1);
        assertTrue(() -> invoiceDao.delete(ID));
    }
// delete false
    @Test
    public void testDeleteInvoiceFromDBByIdThenReturnFalse(){
        when(mockNamedJdbcTemplate.update(anyString(), any(MapSqlParameterSource.class))).thenReturn(0);
        assertFalse(() -> invoiceDao.delete(ID));
    }
// find by id
    @Test
    public void testFindInvoiceInDBByIdThenReturnInvoice(){
        when(mockNamedJdbcTemplate.queryForObject(anyString(), any(MapSqlParameterSource.class),
                any(BeanPropertyRowMapper.class))).thenReturn(invoice);
        Invoice temp = invoiceDao.findById(ID);
        assertEquals(temp, invoice);
    }
// find all
    @Test
    public void testFindAllInvoicesFromDBThenReturnListWithThem(){
        List<Invoice> exp = new ArrayList<>();
        when(mockNamedJdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class))).thenReturn(exp);
        assertEquals(exp, invoiceDao.findAll());
    }
}
