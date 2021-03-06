package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.configuration.MainSpringConfig;
import com.ra.course.janus.traintickets.entity.Admin;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MainSpringConfig.class)
@TestPropertySource("classpath:test_config.properties")
@Sql("classpath:sql_scripts/admins_table.sql")
public class AdminJdbcDaoIntegrationTest {

    @Autowired
    private AdminJdbcDao adminDao;

    private static final Admin TEST_ADMIN = new Admin(1L, "Petya", "Volk", "12345");
    private static final Admin ADMIN = new Admin(null,"Roman", "Hreits", "12345");


    // Test save Admin--------------------------------------------------------------------
    @Test
    public void whenSaveObjectFirstTimeReturnIdOne() {
        // when
        Admin saveAdmin1 = adminDao.save(ADMIN);
        // then
        assertNotNull(saveAdmin1.getId());
    }

    // Test update Admin------------------------------------------------------------------
    @Test
    public void whenCallUpdateObjectSuccessfullyCompleted(){
        // given
        Admin saveAdmin1 = adminDao.save(ADMIN);
        final Long id = saveAdmin1.getId();
        //when
        adminDao.update(TEST_ADMIN);
        Admin byId = adminDao.findById(id);
        // then
        assertNotEquals(byId, saveAdmin1);
    }

    // Test delete Admin------------------------------------------------------------------
    @Test
    public void deleteObjectFromDb(){
        // when
        Admin saveAdmin1 = adminDao.save(ADMIN);
        // then
        assertTrue(adminDao.delete(saveAdmin1.getId()));
    }

    // Test findById Admin----------------------------------------------------------------
    @Test
    public void findByIdObjectInDb(){
        // given
        Admin saveAdmin1 = adminDao.save(ADMIN);
        final long id = saveAdmin1.getId();
        //when
        final Admin adminById = adminDao.findById(id);
        // then
        assertEquals(saveAdmin1, adminById);
    }

    // Test findAll Admins----------------------------------------------------------------
    @Test
    public void findAllObjectsInDb(){
        // when
        adminDao.save(ADMIN);
        adminDao.save(ADMIN);
        List<Admin> all = adminDao.findAll();
        // then
        assertEquals(all.size(), 2);
    }
}


