package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.BaseEntity;
import com.ra.janus.developersteam.entity.Work;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

class PlainJdbcWorkDAOMockTest extends BaseDAOMockTest {

    private BaseDao<Work> workDAO;
    private Work work;

    @Override
    protected BaseEntity getTestEntity() {
        if (work == null)
            work = new Work(testId, "name", new BigDecimal(7));
        return work;
    }
    @Override
    protected BaseDao getDAO() {
        workDAO = new PlainJdbcWorkDAO(mockTemplate);
        return workDAO;
    }
    @Override
    protected String getInsertSql() {
        return "INSERT INTO works (name, price) VALUES (?, ?)";
    }
    @Override
    protected String getUpdateSql() {
        return "UPDATE works SET name=?,price=? WHERE id=?";
    }
    @Override
    protected String getSelectAllSql() {
        return "SELECT * FROM works";
    }
    @Override
    protected String getSelectOneSql() {
        return "SELECT * FROM works WHERE id = ?";
    }
    @Override
    protected String getDeleteSql() {
        return "DELETE FROM works WHERE id=?";
    }
    @Override
    protected Map<String, Object> getTestEntityMap() {
        Map<String, Object> testMap = new HashMap<>(1);
        Work work = (Work) getTestEntity();
        testMap.put("id", work.getId());
        testMap.put("name", work.getName());
        testMap.put("address", work.getPrice());
        return testMap;
    }
}