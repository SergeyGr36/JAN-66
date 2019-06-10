package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.BaseEntity;
import com.ra.janus.developersteam.entity.Manager;
import org.springframework.beans.factory.annotation.Autowired;

public class PlainJdbcManagerDAOIntegrationTest extends BaseDAOIntegrationTest {

    @Autowired
    private BaseDao<Manager> managerDAO;

    Manager managerToCreate = new Manager(1L, "John", "manager@gmail.com", "050-000-11-22");

    @Override
    protected String getTableName() {return "MANAGERS";}

    @Override
    protected BaseDao getDAO() {
        return managerDAO;
    }

    @Override
    protected BaseEntity getEntityToCreate() {
        return managerToCreate;
    }

    @Override
    protected BaseEntity getUpdatedEntity(BaseEntity entity) {

        Manager updatedManager = (Manager) entity;
        updatedManager.setName("Jack");
        updatedManager.setEmail("jach@gmail.com");
        updatedManager.setPhone("050-222-33-44");

        return updatedManager;
    }
}
