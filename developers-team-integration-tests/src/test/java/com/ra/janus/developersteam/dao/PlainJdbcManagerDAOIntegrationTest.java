package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.datasources.DataSourceFactory;
import com.ra.janus.developersteam.entity.Manager;
import com.ra.janus.developersteam.schema.DBSchemaCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class PlainJdbcManagerDAOIntegrationTest {

    private static final DataSource dataSource = DataSourceFactory.get();
    private static final BaseDao<Manager> managerDAO = new PlainJdbcManagerDAO(dataSource);

    private static Manager managerToCreate = new Manager(1L, "John", "manager@gmail.com", "050-000-11-22");

    @BeforeEach
    public void beforeEach() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            DBSchemaCreator.createSchema(connection, "MANAGERS");
        }
    }

    @Test
    void createManagerTest() throws Exception {
        //when
        Manager manager = managerDAO.create(managerToCreate);

        //then
        assertEquals(manager, managerDAO.get(manager.getId()));
    }

    @Test
    void getManagerByIdTest() throws Exception {
        //when
        Manager createdManager = managerDAO.create(managerToCreate);
        Manager gottenManager = managerDAO.get(createdManager.getId());

        //then
        assertEquals(createdManager, gottenManager);
    }

    @Test
    void getAllManagersTest() throws Exception {
        //when
        Manager createdManager = managerDAO.create(managerToCreate);
        List<Manager> expected = Arrays.asList(createdManager);
        List<Manager> actual = managerDAO.getAll();

        //then
        assertIterableEquals(expected, actual);
    }

    @Test
    void updateManagerTest() throws Exception {
        //when
        Manager createdManager = managerDAO.create(managerToCreate);
        createdManager.setName("Jack");
        createdManager.setEmail("jach@gmail.com");
        createdManager.setPhone("050-222-33-44");
        managerDAO.update(createdManager);
        Manager updated = managerDAO.get(createdManager.getId());

        //then
        assertEquals(updated, createdManager);
    }

    @Test
    void deleteManagerTest() throws Exception {
        //when
        Manager createdManager = managerDAO.create(managerToCreate);
        managerDAO.delete(createdManager.getId());
        Manager actual = managerDAO.get(createdManager.getId());

        //then
        assertEquals(null, actual);
    }
}
