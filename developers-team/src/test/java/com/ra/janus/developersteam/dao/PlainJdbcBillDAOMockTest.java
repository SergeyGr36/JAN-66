package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.BaseEntity;
import com.ra.janus.developersteam.entity.Bill;
import java.sql.Date;
import java.util.*;

class PlainJdbcBillDAOMockTest extends BaseDAOMockTest {

    private BaseDao<Bill> billDAO;
    private Bill bill;

    @Override
    protected BaseEntity getTestEntity() {
        if (bill == null)
            bill = new Bill(testId, new Date(System.currentTimeMillis()));
        return bill;
    }
    @Override
    protected BaseDao getDAO() {
        billDAO = new PlainJdbcBillDAO(mockTemplate);
        return billDAO;
    }
    @Override
    protected String getInsertSql() {
        return "INSERT INTO bills (docdate) VALUES (?)";
    }
    @Override
    protected String getUpdateSql() {
        return "UPDATE bills SET docdate=? WHERE id=?";
    }
    @Override
    protected String getSelectAllSql() {
        return "SELECT * FROM bills";
    }
    @Override
    protected String getSelectOneSql() {
        return "SELECT * FROM bills WHERE id = ?";
    }
    @Override
    protected String getDeleteSql() {
        return "DELETE FROM bills WHERE id=?";
    }
    @Override
    protected Map<String, Object> getTestEntityMap() {
        Map<String, Object> testMap = new HashMap<>(1);
        Bill bill = (Bill) getTestEntity();
        testMap.put("id", bill.getId());
        testMap.put("docdate", bill.getDocDate());
        return testMap;
    }

}