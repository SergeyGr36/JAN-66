package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.configuration.AppConfig;
import com.ra.janus.developersteam.entity.BaseEntity;
import com.ra.janus.developersteam.schema.DBSchemaCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public abstract class BaseDAOIntegrationTest {

    @Autowired
    protected DataSource dataSource;


    protected String getTableName() {return "";}
    protected BaseDao<BaseEntity> getDAO() {
        return null;
    }
    protected BaseEntity getEntityToCreate() {
        return null;
    }
    protected BaseEntity getUpdatedEntity(BaseEntity entity) {
        return null;
    }

    @BeforeEach
    public void beforeEach() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            DBSchemaCreator.createSchema(connection, getTableName());
        }
    }

    @Test
    void createEntityTest() throws Exception {
        //when
        BaseEntity entity = getDAO().create(getEntityToCreate());

        //then
        assertEquals(entity, getDAO().get(entity.getId()));
    }

    @Test
    void getEntityByIdTest() throws Exception {
        //when
        BaseEntity createdEntity = getDAO().create(getEntityToCreate());
        BaseEntity gottenBill = getDAO().get(createdEntity.getId());

        //then
        assertEquals(createdEntity, gottenBill);
    }

    @Test
    void getAllEntitiesTest() throws Exception {
        //when
        BaseEntity createdEntity = getDAO().create(getEntityToCreate());
        List<BaseEntity> expected = Arrays.asList(createdEntity);
        List<BaseEntity> actual = getDAO().getAll();

        //then
        assertIterableEquals(expected, actual);
    }

    @Test
    void updateEntityTest() throws Exception {
        //when
        BaseEntity createdEntity = getDAO().create(getEntityToCreate());
        getDAO().update(getUpdatedEntity(createdEntity));
        BaseEntity updated = getDAO().get(createdEntity.getId());

        //then
        assertEquals(updated, createdEntity);
    }

    @Test
    void deleteEntityTest() throws Exception {
        //when
        BaseEntity createdEntity = getDAO().create(getEntityToCreate());
        getDAO().delete(createdEntity.getId());
        BaseEntity actual = getDAO().get(createdEntity.getId());

        //then
        assertEquals(null, actual);
    }

}
