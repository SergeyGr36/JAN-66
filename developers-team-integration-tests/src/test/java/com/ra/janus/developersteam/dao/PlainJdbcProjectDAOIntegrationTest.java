package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.datasources.DataSourceFactory;
import com.ra.janus.developersteam.entity.Project;
import com.ra.janus.developersteam.schema.DBSchemaCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class PlainJdbcProjectDAOIntegrationTest {
    private static final DataSource dataSource = new DataSourceFactory().get();
    private static final BaseDao<Project> projectDAO = new PlainJdbcProjectDAO(dataSource);

    private static Project projectToCreate = new Project(1L, "Integration Tests", "Test project with h2 DB", "WIP", Date.valueOf("2019-05-30"));

    @BeforeEach
    public void beforeEach() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            DBSchemaCreator.createSchema(connection);
        }
    }

    @Test
    void createProjectTest() throws Exception {
        //when
        Project project = projectDAO.create(projectToCreate);

        //then
        assertEquals(projectToCreate, project);
    }

    @Test
    void getProjectByIdTest() throws Exception {
        //when
        Project createdProject = projectDAO.create(projectToCreate);
        Project gottenProject = projectDAO.get(createdProject.getId());

        //then
        assertEquals(createdProject, gottenProject);
    }

    @Test
    void getAllProjectsTest() throws Exception {
        //when
        Project createdProject = projectDAO.create(projectToCreate);
        List<Project> expected = Arrays.asList(createdProject);
        List<Project> actual = projectDAO.getAll();

        //then
        assertIterableEquals(expected, actual);
    }

    @Test
    void updateProjectTest() throws Exception {
        //when
        Project createdProject = projectDAO.create(projectToCreate);
        createdProject.setName("Developers Team");
        createdProject.setDescription("first project");
        createdProject.setStatus("WIP");
        createdProject.setEta(Date.valueOf("2019-08-01"));
        projectDAO.update(createdProject);
        Project updated = projectDAO.get(createdProject.getId());

        //then
        assertEquals(updated, createdProject);
    }

    @Test
    void deleteProjectTest() throws Exception {
        //when
        Project createdProject = projectDAO.create(projectToCreate);
        projectDAO.delete(createdProject.getId());
        Project expected = null;
        Project actual = projectDAO.get(createdProject.getId());

        //then
        assertEquals(expected, actual);
    }
}
