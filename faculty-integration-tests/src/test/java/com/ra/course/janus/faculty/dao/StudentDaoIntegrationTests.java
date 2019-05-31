package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.DataSource.ConnectionFactory;
import dao.DaoStudent;
import dao.JDBCDaoStudent;
import entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
/*import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;*/

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.*;



public class StudentDaoIntegrationTests {
    public static  Connection conn ;
    public static JDBCDaoStudent studentDao;


    @BeforeEach
    public void beforeEach () throws Exception{
       studentDao = new JDBCDaoStudent((DataSource) ConnectionFactory.getInstance().getConnection());
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
        List<Student> cources = DaoStudent.findAll();
        for (Student c : cources){
           // DaoStudent.delete(s);
        }
        assertEquals(0,DaoStudent.findAll().size());
    }


    @Test
    void findAllWhenExists() {
        int n0 = DaoStudent.findAll().size();
        insert();
        insert();
        insert();
        assertEquals(n0+3,DaoStudent.findAll().size());
    }


    @Test
    void findByIdWhenExists() {
        Student s = new Student();
        s.setCode("J2EE");
        s.setDescription("Java web development course");

        s = DaoStudent.insert(s);
        assertNotNull(DaoStudent.findByStudentId(s.getId()));
    }

    @Test
    void findByIdWhenNotExists() {
        assertNull(DaoStudent.findByStudentId(String.valueOf(-1)));
    }

    @Test
    void updateWhenExists() {
        Student s = new Student();
        s.setCode("UPD_E");
        s.setDescription("Test update when exists");
        s = DaoStudent.insert(s);
        s.setDescription("Test update when exists - updated");
        DaoStudent.update(s);

        Student sToCheck = (Student) DaoStudent.findByStudentId(s.getId());
        assertTrue("Test update when exists - updated".equals(sToCheck.getDescription()));

        assertNull(DaoStudent.findByStudentId(String.valueOf(-1)));
    }

    @Test
    void updateWhenNotExists() {
        Student s = new Student();
        s.setCode("UPD_NE");
        s.setDescription("Test update when not exists");
        s = DaoStudent.insert(s);
        s.setDescription("Test update when exists - updated");
        s.setId(Integer.parseInt(s.getId()+1));
        assertNull(DaoStudent.update(s));

    }

    @Test
    void deleteWhenExists() {
        Student s = new Student();
        s.setCode("DEL_E");
        s.setDescription("Test delete when exists");
        s = (Student) DaoStudent.insert(s);
        assertTrue((BooleanSupplier) DaoStudent.delete(s));

    }

    @Test
    void deleteWhenNotExists() {
        Student s = new Student();
        s.setCode("DEL_NE");
        s.setDescription("Test delete when not exists");
        s = DaoStudent.insert(s);
        s.setId(Integer.parseInt(s.getId()+1));
        assertFalse(DaoStudent.delete(s));

    }


}

