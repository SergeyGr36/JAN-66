package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Manager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PlainJdbcManagerDAOIntegrationTest extends BaseDAOIntegrationTest {

    @Autowired
    private BaseDao<Manager> managerDAO;

    private Manager managerToCreate = new Manager(1L, "John", "manager@gmail.com", "050-000-11-22");

    private Delegate updatedEntity() {

        return  (entity) -> {
            Manager updatedManager = (Manager) entity;
            updatedManager.setName("Jack");
            updatedManager.setEmail("jach@gmail.com");
            updatedManager.setPhone("050-222-33-44");

            return updatedManager;
        };
    }

    @Test
    public void createManagerTest() throws Exception {
        createEntityTest(managerDAO, managerToCreate);
    }

    @Test
    public void getManagerByIdTest() throws Exception {
        getEntityByIdTest(managerDAO, managerToCreate);
    }

    @Test
    public void getAllManagersTest() throws Exception {
        getAllEntitiesTest(managerDAO, managerToCreate);
    }

    @Test
    public void updateManagerTest() throws Exception {
        updateEntityTest(managerDAO, managerToCreate, updatedEntity());
    }

    @Test
    public void deleteManagerTest() throws Exception {
        deleteEntityTest(managerDAO, managerToCreate);
    }
}
