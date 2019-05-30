package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.datasources.DataSourceFactory;
import com.ra.janus.developersteam.entity.Qualification;
import com.ra.janus.developersteam.schema.DBSchemaCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class PlainJdbcQualificationDAOIntegrationTest {

    private static final DataSource dataSource = new DataSourceFactory().get();
    private static final BaseDao<Qualification> qualificationDAO = new PlainJdbcQualificationDAO(dataSource);

    private static Qualification qualificationToCreate = new Qualification(1L, "Web Developer", "Front End");

    @BeforeEach
    public void beforeEach() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            DBSchemaCreator.createSchema(connection);
        }
    }

    @Test
    void createQualificationTest() throws Exception {
        //when
        Qualification qualification = qualificationDAO.create(qualificationToCreate);

        //then
        assertEquals(qualificationToCreate, qualification);
    }

    @Test
    void getQualificationByIdTest() throws Exception {
        //when
        Qualification createdQualification = qualificationDAO.create(qualificationToCreate);
        Qualification gottenQualification = qualificationDAO.get(createdQualification.getId());

        //then
        assertEquals(createdQualification, gottenQualification);
    }

    @Test
    void getAllQualificationsTest() throws Exception {
        //when
        Qualification createdQualification = qualificationDAO.create(qualificationToCreate);
        List<Qualification> expected = Arrays.asList(createdQualification);
        List<Qualification> actual = qualificationDAO.getAll();

        //then
        assertIterableEquals(expected, actual);
    }

    @Test
    void updateQualificationTest() throws Exception {
        //when
        Qualification createdQualification = qualificationDAO.create(qualificationToCreate);
        createdQualification.setName("Java Developer");
        createdQualification.setResponsibility("Back End");
        qualificationDAO.update(createdQualification);
        Qualification updated = qualificationDAO.get(createdQualification.getId());

        //then
        assertEquals(updated, createdQualification);
    }

    @Test
    void deleteQualificationTest() throws Exception {
        //when
        Qualification createdQualification = qualificationDAO.create(qualificationToCreate);
        qualificationDAO.delete(createdQualification.getId());
        Qualification expected = null;
        Qualification actual = qualificationDAO.get(createdQualification.getId());

        //then
        assertEquals(expected, actual);
    }
}
