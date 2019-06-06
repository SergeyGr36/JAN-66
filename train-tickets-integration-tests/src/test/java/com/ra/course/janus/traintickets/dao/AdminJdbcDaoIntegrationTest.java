package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.configuration.DataSourceFactory;
import com.ra.course.janus.traintickets.entity.Admin;
import org.junit.jupiter.api.*;

import javax.sql.DataSource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


public class AdminJdbcDaoIntegrationTest {

    private static final DataSource DATA_SOURCE = DataSourceFactory.DATA_SOURCE.getInstance();
    private static final String FILE_PATH = "src/test/resources/sql_scripts/admin_table.sql";
    private AdminJdbcDao adminDao;
    private static final Admin TEST_ADMIN = new Admin(0, "Petya", "Volk", "12345");
    private static final Admin ADMIN = new Admin(0,"Roman", "Hreits", "12345");

    @BeforeAll
    public static void init() throws IOException, SQLException {
        createTable();
    }
    @BeforeEach
    public void setUp()  {
        adminDao = new AdminJdbcDao(DATA_SOURCE);
    }

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
        // when
        Admin saveAdmin1 = adminDao.save(ADMIN);
        final Long ID = saveAdmin1.getId();
        adminDao.update(ID, TEST_ADMIN);
        Admin byId = adminDao.findById(ID);
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
        // when
        Admin saveAdmin1 = adminDao.save(ADMIN);
        final long id = saveAdmin1.getId();
        // then
        assertNotNull(adminDao.findById(id));
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

    //------------------------------------------------------------------------------------
    private static void createTable() throws SQLException, IOException {
        try (Connection conn = DATA_SOURCE.getConnection()) {
           conn.createStatement().execute(Files.readAllLines(Paths.get(FILE_PATH)).stream()
                   .collect(Collectors.joining("")));
        }
    }
    @AfterEach
    public void clearTable () throws SQLException, IOException {
        try (Connection conn = DATA_SOURCE.getConnection()) {
            conn.createStatement().execute("TRUNCATE TABLE ADMIN");
        }
    }
}


