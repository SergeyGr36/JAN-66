package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.BaseEntity;
import com.ra.janus.developersteam.entity.Developer;
import java.util.HashMap;
import java.util.Map;

class PlainJdbcDeveloperDAOMockTest extends BaseDAOMockTest {

    private BaseDao<Developer> developerDAO;
    private Developer developer;

    @Override
    protected BaseEntity getTestEntity() {
        if (developer == null)
            developer = new Developer(testId, "John Doe");
        return developer;
    }
    @Override
    protected BaseDao getDAO() {
        developerDAO = new PlainJdbcDeveloperDAO(mockTemplate);
        return developerDAO;
    }
    @Override
    protected String getInsertSql() {
        return "INSERT INTO developers (name) VALUES (?)";
    }
    @Override
    protected String getUpdateSql() {
        return "UPDATE developers SET name=? WHERE id=?";
    }
    @Override
    protected String getSelectAllSql() {
        return "SELECT * FROM developers";
    }
    @Override
    protected String getSelectOneSql() {
        return "SELECT * FROM developers WHERE id = ?";
    }
    @Override
    protected String getDeleteSql() {
        return "DELETE FROM developers WHERE id=?";
    }
    @Override
    protected Map<String, Object> getTestEntityMap() {
        Map<String, Object> testMap = new HashMap<>(1);
        Developer developer = (Developer) getTestEntity();
        testMap.put("id", developer.getId());
        testMap.put("name", developer.getName());
        return testMap;
    }
}