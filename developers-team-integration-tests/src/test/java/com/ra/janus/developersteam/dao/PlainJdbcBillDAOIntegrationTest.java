package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Bill;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.sql.Date;

public class PlainJdbcBillDAOIntegrationTest extends BaseDAOIntegrationTest {

    @Autowired
    private BaseDao<Bill> billDAO;

    private Bill billToCreate = new Bill(1L, Date.valueOf("2020-11-03"));

    private Delegate updatedEntity() {

        return  (entity) -> {
            Bill updatedBill = (Bill)entity;
            updatedBill.setDocDate(Date.valueOf("2019-05-05"));

            return updatedBill;
        };
    }

    @Test
    public void createBillTest() throws Exception {
        createEntityTest(billDAO, billToCreate);
    }

    @Test
    public void getBillByIdTest() throws Exception {
        getEntityByIdTest(billDAO, billToCreate);
    }

    @Test
    public void getAllBillsTest() throws Exception {
        getAllEntitiesTest(billDAO, billToCreate);
    }

    @Test
    public void updateBillTest() throws Exception {
        updateEntityTest(billDAO, billToCreate, updatedEntity());
    }

    @Test
    public void deleteBillTest() throws Exception {
        deleteEntityTest(billDAO, billToCreate);
    }
}
