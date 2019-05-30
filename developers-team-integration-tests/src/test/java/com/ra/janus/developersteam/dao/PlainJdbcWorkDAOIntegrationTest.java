package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.datasources.DataSourceFactory;
import com.ra.janus.developersteam.entity.Work;
import com.ra.janus.developersteam.schema.DBSchemaCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class PlainJdbcWorkDAOIntegrationTest {
    private static final DataSource dataSource = new DataSourceFactory().get();
    private static final BaseDao<Work> workDAO = new PlainJdbcWorkDAO(dataSource);

    private static Work workToCreate = new Work(1L, "Developer", new BigDecimal(4000.00));

    @BeforeEach
    public void beforeEach() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            DBSchemaCreator.createSchema(connection);
        }
    }

    @Test
    void createWorkTest() throws Exception {
        //when
        Work work = workDAO.create(workToCreate);

        //then
        assertEquals(workToCreate, work);
    }

    @Test
    void getWorkByIdTest() throws Exception {
        //when
        Work createdWork = workDAO.create(workToCreate);
        Work gottenWork = workDAO.get(createdWork.getId());

        //then
        assertEquals(createdWork, gottenWork);
    }

    @Test
    void getAllWorksTest() throws Exception {
        //when
        Work createdWork = workDAO.create(workToCreate);
        List<Work> expected = Arrays.asList(createdWork);
        List<Work> actual = workDAO.getAll();

        //then
        assertIterableEquals(expected, actual);
    }

    @Test
    void updateWorkTest() throws Exception {
        //when
        Work createdWork = workDAO.create(workToCreate);
        createdWork.setName("Tester");
        createdWork.setPrice(new BigDecimal(2000.00));
        workDAO.update(createdWork);
        Work updated = workDAO.get(createdWork.getId());

        //then
        assertEquals(updated, createdWork);
    }

    @Test
    void deleteWorkTest() throws Exception {
        //when
        Work createdWork = workDAO.create(workToCreate);
        workDAO.delete(createdWork.getId());

        //then
        assertEquals(null, workDAO.get(createdWork.getId()));
    }
}
