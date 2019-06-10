package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.connect.ConnectionUtil;
import com.ra.course.janus.faculty.entity.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class JDBCTeacherDaoIntegrationTest {
    private static final String FILE = "src/test/resources/scripts/create_faculty.sql";
    private static final DataSource DATA_SOURCE = ConnectionUtil.getDataSource();
    private static final GenericDao<Teacher> TEACHER_DAO = new JDBCTeacherDao(DATA_SOURCE);
    private static final Teacher TEACHER_INSERT = new Teacher(1L,"Roma", "Java");

    @BeforeEach
    void before() throws Exception {
        createTable();
    }

    @Test
    void insertTest() {
        Teacher teacher = TEACHER_DAO.insert(TEACHER_INSERT);
        assertEquals(teacher, TEACHER_INSERT);
    }

    @Test
    void updateTest() {
        Teacher teacher = new Teacher(1L, "Oleg", "PHP");
        TEACHER_DAO.insert(TEACHER_INSERT);
        boolean isUpdated = TEACHER_DAO.update(teacher);
        assertTrue(isUpdated);
    }

    @Test
    void selectTest() {
        TEACHER_DAO.insert(TEACHER_INSERT);
        List list = TEACHER_DAO.select();
        assertTrue(list.size()>0);
    }

    @Test
    void deleteTest() {
        TEACHER_DAO.insert(TEACHER_INSERT);
        boolean isDeleted = TEACHER_DAO.delete(1L);
        assertTrue(isDeleted);
    }

    private static void createTable() throws SQLException, IOException {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            connection.createStatement().execute(Files.readAllLines(Paths.get(FILE)).stream().collect(Collectors.joining()));
        }
    }
}
