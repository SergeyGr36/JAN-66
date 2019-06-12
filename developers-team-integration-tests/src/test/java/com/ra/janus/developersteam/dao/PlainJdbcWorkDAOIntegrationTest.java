package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Work;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class PlainJdbcWorkDAOIntegrationTest extends BaseDAOIntegrationTest {

    @Autowired
    private BaseDao<Work> workDAO;

    private Work workToCreate = new Work(1L, "Developer", new BigDecimal("4000.00"));

    private Delegate updatedEntity() {

        return  (entity) -> {
            Work updatedWork = (Work) entity;
            updatedWork.setName("Tester");
            updatedWork.setPrice(new BigDecimal("2000.00"));

            return updatedWork;
        };
    }

    @Test
    public void createWorkTest() throws Exception {
        createEntityTest(workDAO, workToCreate);
    }

    @Test
    public void getWorkByIdTest() throws Exception {
        getEntityByIdTest(workDAO, workToCreate);
    }

    @Test
    public void getAllWorksTest() throws Exception {
        getAllEntitiesTest(workDAO, workToCreate);
    }

    @Test
    public void updateWorkTest() throws Exception {
        updateEntityTest(workDAO, workToCreate, updatedEntity());
    }

    @Test
    public void deleteWorkTest() throws Exception {
        deleteEntityTest(workDAO, workToCreate);
    }

}
