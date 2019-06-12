package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.sql.Date;

public class PlainJdbcProjectDAOIntegrationTest extends BaseDAOIntegrationTest {

    @Autowired
    private BaseDao<Project> projectDAO;

    private Project projectToCreate = new Project(1L, "Integration Tests", "Test project with h2 DB", "WIP", Date.valueOf("2019-05-30"));

    private Delegate updatedEntity() {

        return  (entity) -> {
            Project updatedProject = (Project) entity;
            updatedProject.setName("Developers Team");
            updatedProject.setDescription("first project");
            updatedProject.setStatus("WIP");
            updatedProject.setEta(Date.valueOf("2019-08-01"));

            return updatedProject;
        };
    }

    @Test
    public void createProjectTest() throws Exception {
        createEntityTest(projectDAO, projectToCreate);
    }

    @Test
    public void getProjectByIdTest() throws Exception {
        getEntityByIdTest(projectDAO, projectToCreate);
    }

    @Test
    public void getAllProjectsTest() throws Exception {
        getAllEntitiesTest(projectDAO, projectToCreate);
    }

    @Test
    public void updateProjectTest() throws Exception {
        updateEntityTest(projectDAO, projectToCreate, updatedEntity());
    }

    @Test
    public void deleteProjectTest() throws Exception {
        deleteEntityTest(projectDAO, projectToCreate);
    }
}
