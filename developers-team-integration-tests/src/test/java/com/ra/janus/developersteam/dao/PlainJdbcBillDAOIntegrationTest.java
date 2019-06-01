package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.datasources.DataSourceFactory;
import com.ra.janus.developersteam.entity.Bill;
import com.ra.janus.developersteam.schema.DBSchemaCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlainJdbcBillDAOIntegrationTest {

    private static final DataSource dataSource = DataSourceFactory.get();
    private static final BaseDao<Bill> billDAO = new PlainJdbcBillDAO(dataSource);

    private static Bill billToCreate = new Bill(1L, Date.valueOf("2020-11-03"));

    @BeforeEach
    public void beforeEach() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            DBSchemaCreator.createSchema(connection, "BILLS");
        }
    }

    @Test
    void createBillTest() throws Exception {
        //when
        Bill bill = billDAO.create(billToCreate);

        //then
        assertEquals(bill, billDAO.get(bill.getId()));
    }

    @Test
    void getBillByIdTest() throws Exception {
        //when
        Bill createdBill = billDAO.create(billToCreate);
        Bill gottenBill = billDAO.get(createdBill.getId());

        //then
        assertEquals(createdBill, gottenBill);
    }

    @Test
    void getAllBillsTest() throws Exception {
        //when
        Bill createdBill = billDAO.create(billToCreate);
        List<Bill> expected = Arrays.asList(createdBill);
        List<Bill> actual = billDAO.getAll();

        //then
        assertIterableEquals(expected, actual);
    }

    @Test
    void updateBillTest() throws Exception {
        //when
        Bill createdBill = billDAO.create(billToCreate);
        createdBill.setDocDate(Date.valueOf("2019-05-05"));
        billDAO.update(createdBill);
        Bill updated = billDAO.get(createdBill.getId());

        //then
        assertEquals(updated, createdBill);
    }

    @Test
    void deleteBillTest() throws Exception {
        //when
        Bill createdBill = billDAO.create(billToCreate);
        billDAO.delete(createdBill.getId());
        Bill actual = billDAO.get(createdBill.getId());

        //then
        assertEquals(null, actual);
    }
}