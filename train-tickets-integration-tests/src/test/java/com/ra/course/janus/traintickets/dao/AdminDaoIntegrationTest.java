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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


public class AdminDaoIntegrationTest {

    private static final DataSource DATA_SOURCE = DataSourceFactory.HIKARY_H2_IN_MEMORY.getDataSource();
    private static final String FILE_PATH = "src/test/resources/sql_scripts/admin_table.sql";
    private JdbcAdminDao adminDao;
    private static final Admin TEST_ADMIN = new Admin(0, "Petya", "Volk", "12345");
    private static final Admin ADMIN = new Admin(0,"Roman", "Hreits", "12345");

    @BeforeAll
    public static void init() throws IOException, SQLException {
        createTable();
    }
    @BeforeEach
    public void setUp()  {
        adminDao = new JdbcAdminDao(DATA_SOURCE);
    }

    @Test
    public void whenSaveObjectFirstTimeReturnIdOne() {
        Admin saveAdmin1 = adminDao.save(ADMIN);
        assertTrue(saveAdmin1.getId()==1);
        Admin saveAdmin2 = adminDao.save(ADMIN);
        assertTrue(saveAdmin2.getId()==2);
    }

    @Test
    public void whenCallUpdateObjectSuccessfullyCompleted(){

        Admin saveAdmin1 = adminDao.save(ADMIN);
        Admin saveAdmin2 = adminDao.save(ADMIN);
        final Long ID = saveAdmin1.getId();
        boolean update = adminDao.update(ID, TEST_ADMIN);
        Admin byId = adminDao.findById(ID);
        assertNotEquals(byId, saveAdmin1);
    }

    public void deleteObjectFromDb(){

    }

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


