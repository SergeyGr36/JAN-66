package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Developer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PlainJdbcDeveloperDAOIntegrationTest extends BaseDAOIntegrationTest {

    @Autowired
    private BaseDao<Developer> developerDAO;

    private Developer developerToCreate = new Developer(1L, "Nick");

    private Delegate updatedEntity() {

        return  (entity) -> {
            Developer updatedDeveloper = (Developer) entity;
            updatedDeveloper.setName("Jamshut");

            return updatedDeveloper;
        };
    }

    @Test
    public void createDeveloperTest() throws Exception {
        createEntityTest(developerDAO, developerToCreate);
    }

    @Test
    public void getDeveloperByIdTest() throws Exception {
        getEntityByIdTest(developerDAO, developerToCreate);
    }

    @Test
    public void getAllDevelopersTest() throws Exception {
        getAllEntitiesTest(developerDAO, developerToCreate);
    }

    @Test
    public void updateDeveloperTest() throws Exception {
        updateEntityTest(developerDAO, developerToCreate, updatedEntity());
    }

    @Test
    public void deleteDeveloperTest() throws Exception {
        deleteEntityTest(developerDAO, developerToCreate);
    }
}
