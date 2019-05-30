package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.datasources.DataSourceFactory;
import com.ra.janus.developersteam.entity.TechnicalTask;
import com.ra.janus.developersteam.schema.DBSchemaCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class PlainJdbcTechTaskDAOIntegrationTest {
    private static final DataSource dataSource = DataSourceFactory.get();
    private static final BaseDao<TechnicalTask> technicalTaskDAO = new PlainJdbcTechnicalTaskDAO(dataSource);

    private static TechnicalTask technicalTaskToCreate = new TechnicalTask(1L, "Jan 40", "Integration tests for Dev Team");

    @BeforeEach
    public void beforeEach() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            DBSchemaCreator.createSchema(connection, "TASKS");
        }
    }

    @Test
    void createTechnicalTaskTest() throws Exception {
        //when
        TechnicalTask technicalTask = technicalTaskDAO.create(technicalTaskToCreate);

        //then
        assertEquals(technicalTask, technicalTaskDAO.get(technicalTask.getId()));
    }

    @Test
    void getTechnicalTaskByIdTest() throws Exception {
        //when
        TechnicalTask createdTechnicalTask = technicalTaskDAO.create(technicalTaskToCreate);
        TechnicalTask gottenTechnicalTask = technicalTaskDAO.get(createdTechnicalTask.getId());

        //then
        assertEquals(createdTechnicalTask, gottenTechnicalTask);
    }

    @Test
    void getAllTechnicalTasksTest() throws Exception {
        //when
        TechnicalTask createdTechnicalTask = technicalTaskDAO.create(technicalTaskToCreate);
        List<TechnicalTask> expected = Arrays.asList(createdTechnicalTask);
        List<TechnicalTask> actual = technicalTaskDAO.getAll();

        //then
        assertIterableEquals(expected, actual);
    }

    @Test
    void updateTechnicalTaskTest() throws Exception {
        //when
        TechnicalTask createdTechnicalTask = technicalTaskDAO.create(technicalTaskToCreate);
        createdTechnicalTask.setTitle("Jan 6");
        createdTechnicalTask.setDescription("Project for Dev Team");
        technicalTaskDAO.update(createdTechnicalTask);
        TechnicalTask updated = technicalTaskDAO.get(createdTechnicalTask.getId());

        //then
        assertEquals(updated, createdTechnicalTask);
    }

    @Test
    void deleteTechnicalTaskTest() throws Exception {
        //when
        TechnicalTask createdTechnicalTask = technicalTaskDAO.create(technicalTaskToCreate);
        technicalTaskDAO.delete(createdTechnicalTask.getId());
        TechnicalTask actual = technicalTaskDAO.get(createdTechnicalTask.getId());

        //then
        assertEquals(null, actual);
    }
}
