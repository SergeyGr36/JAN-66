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
    private static final Teacher TEACHER_INSERT = new Teacher(1, "Roma", "Java");

    private static Teacher TEST_TEACHER;

    @BeforeEach
    public void before() throws Exception {
        createTable();
    }

    @Test
    public void insertTest() {
        TEST_TEACHER = TEACHER_DAO.insert(TEACHER_INSERT);
        assertEquals(TEST_TEACHER, TEACHER_INSERT);
    }

    @Test
    public void updateTest() {
        Teacher teacher = new Teacher(1, "Max", "PHP");
        TEST_TEACHER = TEACHER_DAO.insert(TEACHER_INSERT);
        TEACHER_DAO.update(teacher);
        List<Teacher> list = TEACHER_DAO.select();
        assertNotEquals(TEST_TEACHER, list);
    }

    @Test
    public void selectTest() {
        TEACHER_DAO.insert(TEACHER_INSERT);
        List<Teacher> list = TEACHER_DAO.select();
        assertTrue(list.size() > 0);
    }

    @Test
    public void selectByIdTest() {
        TEST_TEACHER = TEACHER_DAO.insert(TEACHER_INSERT);
        Teacher teacher = TEACHER_DAO.selectById(TEST_TEACHER.getId());
        assertEquals(TEST_TEACHER, teacher);
    }

    @Test
    public void deleteTest() {
        Teacher teacherTest = TEACHER_DAO.insert(TEACHER_INSERT);
        boolean isDeleted = TEACHER_DAO.delete(teacherTest.getId());
        assertTrue(isDeleted);
    }

    private static void createTable() throws SQLException, IOException {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            connection.createStatement().execute(Files.readAllLines(Paths.get(FILE)).stream().collect(Collectors.joining()));
        }
    }
}
