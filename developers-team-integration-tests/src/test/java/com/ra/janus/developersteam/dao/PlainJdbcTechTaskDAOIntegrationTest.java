package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.configuration.AppConfig;
import com.ra.janus.developersteam.entity.TechnicalTask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public class PlainJdbcTechTaskDAOIntegrationTest {

    @Autowired
    private BaseDao<TechnicalTask> dao;

    private TechnicalTask entityToCreate = new TechnicalTask(1L, "Jan 40", "Integration tests for Dev Team");

    private TechnicalTask getUpdatedEntity(TechnicalTask entity) {

        TechnicalTask updatedTask = entity;
        updatedTask.setTitle("Jan 6");
        updatedTask.setDescription("Project for Dev Team");

        return updatedTask;
    }

    @Test
    public void createTechnicalTaskTest() throws Exception {
        //when
        TechnicalTask entity = dao.create(entityToCreate);

        //then
        assertEquals(entity, dao.get(entity.getId()));
    }

    @Test
    public void getTechnicalTaskByIdTest() throws Exception {
        //when
        TechnicalTask createdEntity = dao.create(entityToCreate);
        TechnicalTask gottenTechnicalTask = dao.get(createdEntity.getId());

        //then
        assertEquals(createdEntity, gottenTechnicalTask);
    }

    @Test
    public void getAllTechnicalTasksTest() throws Exception {
        //given
        for (var entity : dao.getAll()) {
            dao.delete(entity.getId());
        }

        //when
        TechnicalTask createdEntity = dao.create(entityToCreate);
        List<TechnicalTask> expected = Arrays.asList(createdEntity);
        List<TechnicalTask> actual = dao.getAll();

        //then
        assertIterableEquals(expected, actual);
    }

    @Test
    public void updateTechnicalTaskTest() throws Exception {
        //when
        TechnicalTask createdEntity = dao.create(entityToCreate);
        dao.update(getUpdatedEntity(createdEntity));
        TechnicalTask updated = dao.get(createdEntity.getId());

        //then
        assertEquals(updated, createdEntity);
    }

    @Test
    public void deleteTechnicalTaskTest() throws Exception {
        //when
        TechnicalTask createdEntity = dao.create(entityToCreate);
        dao.delete(createdEntity.getId());
        TechnicalTask actual = dao.get(createdEntity.getId());

        //then
        assertEquals(null, actual);
    }
}
