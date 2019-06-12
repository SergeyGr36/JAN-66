package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Qualification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PlainJdbcQualificationDAOIntegrationTest extends BaseDAOIntegrationTest {

    @Autowired
    private BaseDao<Qualification> qualificationDAO;

    private Qualification qualificationToCreate = new Qualification(1L, "Web Developer", "Front End");

    private Delegate updatedEntity() {

        return  (entity) -> {
            Qualification updatedQualification = (Qualification) entity;

            updatedQualification.setName("Java Developer");
            updatedQualification.setResponsibility("Back End");

            return updatedQualification;
        };
    }

    @Test
    public void createQualificationTest() throws Exception {
        createEntityTest(qualificationDAO, qualificationToCreate);
    }

    @Test
    public void getQualificationByIdTest() throws Exception {
        getEntityByIdTest(qualificationDAO, qualificationToCreate);
    }

    @Test
    public void getAllQualificationsTest() throws Exception {
        getAllEntitiesTest(qualificationDAO, qualificationToCreate);
    }

    @Test
    public void updateQualificationTest() throws Exception {
        updateEntityTest(qualificationDAO, qualificationToCreate, updatedEntity());
    }

    @Test
    public void deleteQualificationTest() throws Exception {
        deleteEntityTest(qualificationDAO, qualificationToCreate);
    }
}
