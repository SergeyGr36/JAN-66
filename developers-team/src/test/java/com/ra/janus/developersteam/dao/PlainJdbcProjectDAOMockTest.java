package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.BaseEntity;
import com.ra.janus.developersteam.entity.Project;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

class PlainJdbcProjectDAOMockTest extends BaseDAOMockTest{

    private Project project;

    @Override
    protected BaseEntity getTestEntity() {
        if (project == null)
            project = new Project(testId, "Mock Tests", "Test project with h2 DB", "WIP",Date.valueOf("2019-05-30"));
        return project;
    }
    @Override
    protected BaseDao getDAO() {
        BaseDao<Project> projectDAO = new PlainJdbcProjectDAO(mockTemplate);
        return projectDAO;
    }
    @Override
    protected String getInsertSql() {
        return "INSERT INTO projects (name, description, status, eta) VALUES (?, ?, ?, ?)";
    }
    @Override
    protected String getUpdateSql() {
        return "UPDATE projects SET name=?,description=?,status=?,eta=? WHERE id=?";
    }
    @Override
    protected String getSelectAllSql() {
        return "SELECT * FROM projects";
    }
    @Override
    protected String getSelectOneSql() {
        return "SELECT * FROM projects WHERE id = ?";
    }
    @Override
    protected String getDeleteSql() {
        return "DELETE FROM projects WHERE id=?";
    }
    @Override
    protected Map<String, Object> getTestEntityMap() {
        Map<String, Object> testMap = new HashMap<>(1);
        Project project = (Project) getTestEntity();
        testMap.put("id", project.getId());
        testMap.put("name", project.getName());
        testMap.put("description", project.getDescription());
        testMap.put("status", project.getStatus());
        testMap.put("eta", project.getEta());
        return testMap;
    }
}