package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.dao.DataSourceUtils;
import com.ra.course.janus.faculty.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
/*import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;*/

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



public class StudentDaoIntegrationTests {
    public static  Connection conn ;
    public static JDBCDaoStudent studentDao;


    @BeforeEach
    public void beforeEach () throws Exception{
        studentDao = new JDBCDaoStudent( DataSourceUtils.getDataSource());
    }

    @Test

    void insert(){
        Student s = new Student();
        s.setCode("J2EE");
        s.setDescription("Java web developmnt course");
        s = (Student) studentDao.insert(s);
        assertNotNull(s.getId());
    }


    @Test
    void findAllWhenNotExists() {
        List<Student> students = studentDao.findAll();
        for (Student s : students){
            studentDao.delete(s);
        }
        assertEquals(0,studentDao.findAll().size());
    }


    @Test
    void findAllWhenExists() {
        int n0 = studentDao.findAll().size();
        insert();
        insert();
        insert();
        assertEquals(n0+3,studentDao.findAll().size());
    }


    @Test
    void findByIdWhenExists() {
        Student s = new Student();
        s.setCode("J2EE");
        s.setDescription("Java web development course");

        s = studentDao.insert(s);
        assertNotNull(studentDao.findByStudentId(s.getId()));
    }

    @Test
    void findByIdWhenNotExists() {
        assertNull(studentDao.findByStudentId((-1)));
    }

    @Test
    void updateWhenExists() {
        Student s = new Student();
        s.setCode("UPD_E");
        s.setDescription("Test update when exists");
        s = studentDao.insert(s);
        s.setDescription("Test update when exists - updated");
        studentDao.update(s);

        Student sToCheck =  studentDao.findByStudentId(s.getId());
        assertTrue("Test update when exists - updated".equals(sToCheck.getDescription()));

        assertNull(studentDao.findByStudentId((-1)));
    }

    @Test
    void updateWhenNotExists() {
        Student s = new Student();
        s.setCode("UPD_NE");
        s.setDescription("Test update when not exists");
        s = studentDao.insert(s);
        s.setDescription("Test update when exists - updated");
        s.setId((s.getId()+1));
        assertFalse(studentDao.update(s));

    }

    @Test
    void deleteWhenExists() {
        Student s = new Student();
        s.setCode("DEL_E");
        s.setDescription("Test delete when exists");
        s = studentDao.insert(s);
        assertTrue(studentDao.delete(s));

    }

    @Test
    void deleteWhenNotExists() {
        Student s = new Student();
        s.setCode("DEL_NE");
        s.setDescription("Test delete when not exists");
        s = studentDao.insert(s);
        s.setId(s.getId()+1);
        assertFalse(studentDao.delete(s));

    }


}

