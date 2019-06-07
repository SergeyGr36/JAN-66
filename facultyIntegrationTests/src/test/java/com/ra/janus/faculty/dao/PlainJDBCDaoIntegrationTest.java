package com.ra.janus.faculty.dao;

import com.ra.course.janus.faculty.connection.ConnectToDb;
import com.ra.course.janus.faculty.dao.PlainJDBCTeacherDao;
import com.ra.course.janus.faculty.dao.TeacherDao;
import com.ra.course.janus.faculty.entity.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlainJDBCDaoIntegrationTest {
    private static String script = "C:\\Users\\User\\Desktop\\projects\\Java\\Hillel\\janus\\facultyIntegrationTests\\src\\test\\resources\\create_teacher_table.sql";
    private static DataSource dataSource;
    private static TeacherDao<Teacher> teacherDao;
    private static Teacher teacherInsert;

    @BeforeEach
    void before() throws Exception {
        dataSource = ConnectToDb.getDataSource();
        teacherDao = new PlainJDBCTeacherDao(dataSource);
        teacherInsert = new Teacher(1,"Roma", "Java");
        createTable();
    }

    @Test
    void insertTest() {
        Teacher teacher = teacherDao.insert(teacherInsert);
        assertEquals(teacher, teacherInsert);
    }

    @Test
    void updateTest() {
        Teacher teacher = new Teacher(1, "Oleg", "PHP");
        teacherDao.insert(teacherInsert);
        boolean isUpdated = teacherDao.update(teacher);
        assertTrue(isUpdated);
    }

    @Test
    void selectTest() {
        teacherDao.insert(teacherInsert);
        List list = teacherDao.select();
        System.out.println(list);
    }

    @Test
    void deleteTest() {
        teacherDao.insert(teacherInsert);
        boolean isDeleted = teacherDao.delete(1);
        assertTrue(isDeleted);
    }

    private static void createTable() throws SQLException, IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(script)); Connection connection = dataSource.getConnection()) {
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
            connection.createStatement().executeUpdate(stringBuilder.toString());
        }
    }
}
