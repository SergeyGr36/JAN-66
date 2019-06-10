package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.BaseEntity;
import com.ra.janus.developersteam.entity.Manager;
import java.util.HashMap;
import java.util.Map;

class PlainJdbcManagerDAOMockTest extends BaseDAOMockTest {

    private BaseDao<Manager> managerDAO;
    private Manager manager;

    @Override
    protected BaseEntity getTestEntity() {
        if (manager == null)
            manager = new Manager(testId, "John", "box@mail.com", "911");
        return manager;
    }
    @Override
    protected BaseDao getDAO() {
        managerDAO = new PlainJdbcManagerDAO(mockTemplate);
        return managerDAO;
    }
    @Override
    protected String getInsertSql() {
        return "INSERT INTO managers (name, email, phone) VALUES (?, ?, ?)";
    }
    @Override
    protected String getUpdateSql() {
        return "UPDATE managers SET name=?,email=?,phone=? WHERE id=?";
    }
    @Override
    protected String getSelectAllSql() {
        return "SELECT * FROM managers";
    }
    @Override
    protected String getSelectOneSql() {
        return "SELECT * FROM managers WHERE id = ?";
    }
    @Override
    protected String getDeleteSql() {
        return "DELETE FROM managers WHERE id=?";
    }
    @Override
    protected Map<String, Object> getTestEntityMap() {
        Map<String, Object> testMap = new HashMap<>(1);
        Manager manager = (Manager) getTestEntity();
        testMap.put("id", manager.getId());
        testMap.put("name", manager.getName());
        testMap.put("email", manager.getEmail());
        testMap.put("phone", manager.getPhone());
        return testMap;
    }
}