package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.BaseEntity;
import com.ra.janus.developersteam.entity.Work;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
public class PlainJdbcWorkDAOIntegrationTest extends BaseDAOIntegrationTest {

    @Autowired
    private BaseDao<Work> workDAO;

    Work workToCreate = new Work(1L, "Developer", new BigDecimal(4000.00));

    @Override
    protected String getTableName() {return "WORKS";}

    @Override
    protected BaseDao getDAO() {
        return workDAO;
    }

    @Override
    protected BaseEntity getEntityToCreate() {
        return workToCreate;
    }

    @Override
    protected BaseEntity getUpdatedEntity(BaseEntity entity) {

        Work updatedWork = (Work) entity;
        updatedWork.setName("Tester");
        updatedWork.setPrice(new BigDecimal(2000.00));

        return updatedWork;
    }

}
