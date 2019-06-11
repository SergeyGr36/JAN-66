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

    protected abstract String getTableName();
    protected abstract BaseDao<BaseEntity> getDAO();
    protected abstract BaseEntity getEntityToCreate();
    protected abstract BaseEntity getUpdatedEntity(BaseEntity entity);

    @BeforeEach
    public void beforeEach() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            DBSchemaCreator.createSchema(connection, getTableName());
        }
    }

    @Test
    public void createEntityTest() throws Exception {
        //when
        BaseEntity entity = getDAO().create(getEntityToCreate());

        //then
        assertEquals(entity, getDAO().get(entity.getId()));
    }

    @Test
    public void getEntityByIdTest() throws Exception {
        //when
        BaseEntity createdEntity = getDAO().create(getEntityToCreate());
        BaseEntity gottenBill = getDAO().get(createdEntity.getId());

        //then
        assertEquals(createdEntity, gottenBill);
    }

    @Test
    public void getAllEntitiesTest() throws Exception {
        //when
        BaseEntity createdEntity = getDAO().create(getEntityToCreate());
        List<BaseEntity> expected = Arrays.asList(createdEntity);
        List<BaseEntity> actual = getDAO().getAll();

        //then
        assertIterableEquals(expected, actual);
    }

    @Test
    public void updateEntityTest() throws Exception {
        //when
        BaseEntity createdEntity = getDAO().create(getEntityToCreate());
        getDAO().update(getUpdatedEntity(createdEntity));
        BaseEntity updated = getDAO().get(createdEntity.getId());

        //then
        assertEquals(updated, createdEntity);
    }

    @Test
    public void deleteEntityTest() throws Exception {
        //when
        BaseEntity createdEntity = getDAO().create(getEntityToCreate());
        getDAO().delete(createdEntity.getId());
        BaseEntity actual = getDAO().get(createdEntity.getId());

        //then
        assertEquals(null, actual);
    }

}
