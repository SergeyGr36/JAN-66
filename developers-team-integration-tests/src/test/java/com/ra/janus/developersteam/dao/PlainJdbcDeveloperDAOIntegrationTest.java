package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.config.DAOConfiguration;
import com.ra.janus.developersteam.entity.Developer;
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
@ContextConfiguration(classes = {DAOConfiguration.class})
public class PlainJdbcDeveloperDAOIntegrationTest {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private BaseDao<Developer> developerDAO;

    private static Developer developerToCreate = new Developer(1L, "Nick");

    @BeforeEach
    public void beforeEach() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            DBSchemaCreator.createSchema(connection, "DEVELOPERS");
        }
    }

    @Test
    void createDeveloperTest() throws Exception {
        //when
        Developer developer = developerDAO.create(developerToCreate);

        //then
        assertEquals(developer, developerDAO.get(developer.getId()));
    }

    @Test
    void getDeveloperByIdTest() throws Exception {
        //when
        Developer createdDeveloper = developerDAO.create(developerToCreate);
        Developer gottenDeveloper = developerDAO.get(createdDeveloper.getId());

        //then
        assertEquals(createdDeveloper, gottenDeveloper);
    }

    @Test
    void getAllDevelopersTest() throws Exception {
        //when
        Developer createdDeveloper = developerDAO.create(developerToCreate);
        List<Developer> expected = Arrays.asList(createdDeveloper);
        List<Developer> actual = developerDAO.getAll();

        //then
        assertIterableEquals(expected, actual);
    }

    @Test
    void updateDeveloperTest() throws Exception {
        //when
        Developer createdDeveloper = developerDAO.create(developerToCreate);
        createdDeveloper.setName("Jamshut");
        developerDAO.update(createdDeveloper);
        Developer updated = developerDAO.get(createdDeveloper.getId());

        //then
        assertEquals(updated, createdDeveloper);
    }

    @Test
    void deleteDeveloperTest() throws Exception {
        //when
        Developer createdDeveloper = developerDAO.create(developerToCreate);
        developerDAO.delete(createdDeveloper.getId());
        Developer actual = developerDAO.get(createdDeveloper.getId());

        //then
        assertEquals(null, actual);
    }
}
