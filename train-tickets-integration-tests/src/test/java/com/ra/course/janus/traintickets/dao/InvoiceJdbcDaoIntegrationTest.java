package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.MainSpringConfig;
import com.ra.course.janus.traintickets.entity.Invoice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration (classes = MainSpringConfig.class)
@Sql("classpath:sql_scripts/invoices_table.sql")
public class InvoiceJdbcDaoIntegrationTest {
    private static final long ID = 1;
    private static final Invoice INVOICE_TEST = new Invoice(ID, new BigDecimal("10.00"), "Something");
    @Autowired
    private InvoiceJdbcDao invoiceDAO;

    @Test
    public void saveInvoiceThenReturnIt() {
        assertNotNull(invoiceDAO.save(INVOICE_TEST));
    }

    @Test
    public void deleteInvoiceWhenOkThenReturnTrue() {
        final long id = invoiceDAO.save(INVOICE_TEST).getId();
        assertTrue(invoiceDAO.delete(id));
    }

    @Test
    public void updateInvoiceWhenOkThenReturnTrue() {
        final long id = invoiceDAO.save(INVOICE_TEST).getId();
        Invoice temp =  new Invoice(id, new BigDecimal("15"), "fsf");
        assertTrue(invoiceDAO.update(temp));
    }

    @Test
    public void findInvoiceByIdWhenOkThenReturnThatOne() {
        final long id = invoiceDAO.save(INVOICE_TEST).getId();
        Invoice temp = invoiceDAO.findById(id);
        assertEquals(temp, invoiceDAO.findById(id));
    }

    @Test
    public void findAllInvoicesFromDataBaseWhenOkThenReturnListWithThem() {
        final List<Invoice> expect = new ArrayList<>();
        expect.add(invoiceDAO.save(INVOICE_TEST));
        final List<Invoice> acttual = invoiceDAO.findAll();
        assertEquals(expect, acttual);
    }
   }
