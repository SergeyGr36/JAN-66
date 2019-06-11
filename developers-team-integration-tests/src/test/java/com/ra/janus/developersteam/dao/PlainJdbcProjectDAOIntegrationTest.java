package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.BaseEntity;
import com.ra.janus.developersteam.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import java.sql.Date;

public class PlainJdbcProjectDAOIntegrationTest extends BaseDAOIntegrationTest {

    @Autowired
    private BaseDao<Project> projectDAO;

    protected Project projectToCreate = new Project(1L, "Integration Tests", "Test project with h2 DB", "WIP", Date.valueOf("2019-05-30"));

    @Override
    protected String getTableName() {return "PROJECTS";}

    @Override
    protected BaseDao getDAO() {
        return projectDAO;
    }

    @Override
    protected BaseEntity getEntityToCreate() {
        return projectToCreate;
    }

    @Override
    protected BaseEntity getUpdatedEntity(BaseEntity entity) {

        Project updatedProject = (Project) entity;
        updatedProject.setName("Developers Team");
        updatedProject.setDescription("first project");
        updatedProject.setStatus("WIP");
        updatedProject.setEta(Date.valueOf("2019-08-01"));

        return updatedProject;
    }
}
