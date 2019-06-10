package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.BaseEntity;
import com.ra.janus.developersteam.entity.Qualification;
import org.springframework.beans.factory.annotation.Autowired;

public class PlainJdbcQualificationDAOIntegrationTest extends BaseDAOIntegrationTest {

    @Autowired
    private BaseDao<Qualification> qualificationDAO;

    Qualification qualificationToCreate = new Qualification(1L, "Web Developer", "Front End");

    @Override
    protected String getTableName() {return "QUALIFICATIONS";}

    @Override
    protected BaseDao getDAO() {
        return qualificationDAO;
    }

    @Override
    protected BaseEntity getEntityToCreate() {
        return qualificationToCreate;
    }

    @Override
    protected BaseEntity getUpdatedEntity(BaseEntity entity) {

        Qualification updatedQualification = (Qualification) entity;

        updatedQualification.setName("Java Developer");
        updatedQualification.setResponsibility("Back End");

        return updatedQualification;
    }
}
