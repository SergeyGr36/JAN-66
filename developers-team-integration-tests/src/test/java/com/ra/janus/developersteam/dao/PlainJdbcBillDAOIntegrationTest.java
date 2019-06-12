package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.BaseEntity;
import com.ra.janus.developersteam.entity.Bill;
import org.springframework.beans.factory.annotation.Autowired;
import java.sql.Date;

public class PlainJdbcBillDAOIntegrationTest extends BaseDAOIntegrationTest {

    @Autowired
    private BaseDao<Bill> billDAO;

    protected Bill billToCreate = new Bill(1L, Date.valueOf("2020-11-03"));

    @Override
    protected BaseDao getDAO() {
        return billDAO;
    }

    @Override
    protected BaseEntity getEntityToCreate() {
        return billToCreate;
    }

    @Override
    protected BaseEntity getUpdatedEntity(BaseEntity entity) {

        Bill updatedBill = (Bill)entity;
        updatedBill.setDocDate(Date.valueOf("2019-05-05"));

        return updatedBill;
    }
}
