package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.BaseEntity;
import com.ra.janus.developersteam.entity.Developer;
import org.springframework.beans.factory.annotation.Autowired;

public class PlainJdbcDeveloperDAOIntegrationTest extends BaseDAOIntegrationTest {

    @Autowired
    private BaseDao<Developer> developerDAO;

    protected Developer developerToCreate = new Developer(1L, "Nick");

    @Override
    protected String getTableName() {return "DEVELOPERS";}

    @Override
    protected BaseDao getDAO() {
        return developerDAO;
    }

    @Override
    protected BaseEntity getEntityToCreate() {
        return developerToCreate;
    }

    @Override
    protected BaseEntity getUpdatedEntity(BaseEntity entity) {

        Developer updatedDeveloper = (Developer) entity;
        updatedDeveloper.setName("Jamshut");

        return updatedDeveloper;
    }
}
