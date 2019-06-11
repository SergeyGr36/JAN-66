package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.BaseEntity;
import com.ra.janus.developersteam.entity.TechnicalTask;
import org.springframework.beans.factory.annotation.Autowired;

public class PlainJdbcTechTaskDAOIntegrationTest extends BaseDAOIntegrationTest {

    @Autowired
    private BaseDao<TechnicalTask> taskDAO;

    protected TechnicalTask taskToCreate = new TechnicalTask(1L, "Jan 40", "Integration tests for Dev Team");

    @Override
    protected String getTableName() {return "TASKS";}

    @Override
    protected BaseDao getDAO() {
        return taskDAO;
    }

    @Override
    protected BaseEntity getEntityToCreate() {
        return taskToCreate;
    }

    @Override
    protected BaseEntity getUpdatedEntity(BaseEntity entity) {

        TechnicalTask updatedTask = (TechnicalTask) entity;
        updatedTask.setTitle("Jan 6");
        updatedTask.setDescription("Project for Dev Team");

        return updatedTask;
    }
}
