package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.BaseEntity;
import com.ra.janus.developersteam.entity.TechnicalTask;
import java.util.HashMap;
import java.util.Map;

class PlainJdbcTechnicalTaskDAOMockTest extends BaseDAOMockTest {

    private TechnicalTask task;

    @Override
    protected BaseEntity getTestEntity() {
        if (task == null)
            task = new TechnicalTask(testId, "title", "description");
        return task;
    }
    @Override
    protected BaseDao getDAO() {
        BaseDao<TechnicalTask> taskDAO = new PlainJdbcTechnicalTaskDAO(mockTemplate);
        return taskDAO;
    }
    @Override
    protected String getInsertSql() {
        return "INSERT INTO tasks (title, description) VALUES (?, ?)";
    }
    @Override
    protected String getUpdateSql() {
        return "UPDATE tasks SET title=?,description=? WHERE id=?";
    }
    @Override
    protected String getSelectAllSql() {
        return "SELECT * FROM tasks";
    }
    @Override
    protected String getSelectOneSql() {
        return "SELECT * FROM tasks WHERE id = ?";
    }
    @Override
    protected String getDeleteSql() {
        return "DELETE FROM tasks WHERE id=?";
    }
    @Override
    protected Map<String, Object> getTestEntityMap() {
        Map<String, Object> testMap = new HashMap<>(1);
        TechnicalTask task = (TechnicalTask) getTestEntity();
        testMap.put("id", task.getId());
        testMap.put("title", task.getTitle());
        testMap.put("description", task.getDescription());
        return testMap;
    }
}