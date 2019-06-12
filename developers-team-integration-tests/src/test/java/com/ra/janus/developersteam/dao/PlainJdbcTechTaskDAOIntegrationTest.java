package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.TechnicalTask;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PlainJdbcTechTaskDAOIntegrationTest extends BaseDAOIntegrationTest {

    @Autowired
    private BaseDao<TechnicalTask> taskDAO;

    private TechnicalTask taskToCreate = new TechnicalTask(1L, "Jan 40", "Integration tests for Dev Team");

    private Delegate updatedEntity() {

        return  (entity) -> {
            TechnicalTask updatedTask = (TechnicalTask) entity;
            updatedTask.setTitle("Jan 6");
            updatedTask.setDescription("Project for Dev Team");

            return updatedTask;
        };
    }

    @Test
    public void createTaskTest() throws Exception {
        createEntityTest(taskDAO, taskToCreate);
    }

    @Test
    public void getTaskByIdTest() throws Exception {
        getEntityByIdTest(taskDAO, taskToCreate);
    }

    @Test
    public void getAllTasksTest() throws Exception {
        getAllEntitiesTest(taskDAO, taskToCreate);
    }

    @Test
    public void updateTaskTest() throws Exception {
        updateEntityTest(taskDAO, taskToCreate, updatedEntity());
    }

    @Test
    public void deleteTaskTest() throws Exception {
        deleteEntityTest(taskDAO, taskToCreate);
    }
}
