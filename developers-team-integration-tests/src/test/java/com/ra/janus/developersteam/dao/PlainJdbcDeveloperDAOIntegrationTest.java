package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.configuration.AppConfig;
import com.ra.janus.developersteam.entity.Developer;
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
public class PlainJdbcDeveloperDAOIntegrationTest {

    @Autowired
    private BaseDao<Developer> dao;

    private Developer entityToCreate = new Developer(1L, "Nick");

    private Developer getUpdatedEntity(Developer entity) {

        Developer updatedDeveloper = entity;
        updatedDeveloper.setName("Jamshut");

        return updatedDeveloper;
    }

    @Test
    public void createDeveloperTest() throws Exception {
        //when
        Developer entity = dao.create(entityToCreate);

        //then
        assertEquals(entity, dao.get(entity.getId()));
    }

    @Test
    public void getDeveloperByIdTest() throws Exception {
        //when
        Developer createdEntity = dao.create(entityToCreate);
        Developer gottenDeveloper = dao.get(createdEntity.getId());

        //then
        assertEquals(createdEntity, gottenDeveloper);
    }

    @Test
    public void getAllDevelopersTest() throws Exception {
        //given
        for (var entity : dao.getAll()) {
            dao.delete(entity.getId());
        }

        //when
        Developer createdEntity = dao.create(entityToCreate);
        List<Developer> expected = Arrays.asList(createdEntity);
        List<Developer> actual = dao.getAll();

        //then
        assertIterableEquals(expected, actual);
    }

    @Test
    public void updateDeveloperTest() throws Exception {
        //when
        Developer createdEntity = dao.create(entityToCreate);
        dao.update(getUpdatedEntity(createdEntity));
        Developer updated = dao.get(createdEntity.getId());

        //then
        assertEquals(updated, createdEntity);
    }

    @Test
    public void deleteDeveloperTest() throws Exception {
        //when
        Developer createdEntity = dao.create(entityToCreate);
        dao.delete(createdEntity.getId());
        Developer actual = dao.get(createdEntity.getId());

        //then
        assertEquals(null, actual);
    }
}
