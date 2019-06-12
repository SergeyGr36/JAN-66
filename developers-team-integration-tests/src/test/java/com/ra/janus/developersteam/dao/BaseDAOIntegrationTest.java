package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.configuration.AppConfig;
import com.ra.janus.developersteam.entity.BaseEntity;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
public abstract class BaseDAOIntegrationTest {

    public void createEntityTest(BaseDao baseDao, BaseEntity entityToCreate) throws Exception {
        //when
        BaseDao<BaseEntity> dao = baseDao;
        BaseEntity entity = dao.create(entityToCreate);

        //then
        assertEquals(entity, dao.get(entity.getId()));
    }

    public void getEntityByIdTest(BaseDao baseDao, BaseEntity entityToCreate) throws Exception {
        //when
        BaseDao<BaseEntity> dao = baseDao;
        BaseEntity createdEntity = dao.create(entityToCreate);
        BaseEntity gottenBill = dao.get(createdEntity.getId());

        //then
        assertEquals(createdEntity, gottenBill);
    }

    public void getAllEntitiesTest(BaseDao baseDao, BaseEntity entityToCreate) throws Exception {
        //given
        BaseDao<BaseEntity> dao = baseDao;
        for (BaseEntity entity : dao.getAll()) {
            dao.delete(entity.getId());
        }

        //when
        BaseEntity createdEntity = dao.create(entityToCreate);
        List<BaseEntity> expected = Arrays.asList(createdEntity);
        List<BaseEntity> actual = dao.getAll();

        //then
        assertIterableEquals(expected, actual);
    }

    public void updateEntityTest(BaseDao baseDao, BaseEntity entityToCreate, Delegate getUpdatedEntity) throws Exception {
        //when
        BaseDao<BaseEntity> dao = baseDao;
        BaseEntity createdEntity = dao.create(entityToCreate);
        dao.update(getUpdatedEntity.execute(createdEntity));
        BaseEntity updated = dao.get(createdEntity.getId());

        //then
        assertEquals(updated, createdEntity);
    }

    public void deleteEntityTest(BaseDao baseDao, BaseEntity entityToCreate) throws Exception {
        //when
        BaseDao<BaseEntity> dao = baseDao;
        BaseEntity createdEntity = dao.create(entityToCreate);
        dao.delete(createdEntity.getId());
        BaseEntity actual = dao.get(createdEntity.getId());

        //then
        assertEquals(null, actual);
    }

}
