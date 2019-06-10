package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.connect.ConnectionUtil;
import com.ra.course.janus.faculty.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



public class JDBCStudentsDaoIntegrationTest {
    public static  Connection conn ;
    public static JDBCDaoStudent studentDao;

    private static final long ID = 1;

    @BeforeEach
    public void beforeEach () throws Exception{
        studentDao = new JDBCDaoStudent( ConnectionUtil.getDataSource());
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
        List<Student> students = studentDao.select();
        studentDao.delete(1);
        studentDao.delete(2);
        studentDao.delete(3);
        studentDao.delete(4);
        assertEquals(0,studentDao.select().size());
    }

    @Test
    void findAllWhenExists() {
        int n0 = studentDao.select().size();
        insert();
        insert();
        insert();
        assertEquals(n0+3,studentDao.select().size());
    }

    @Test
    void findByIdWhenExists() {
        Student s = new Student();
        s.setCode("J2EE");
        s.setDescription("Java web development course");

        s = studentDao.insert(s);
        assertNotNull(studentDao.selectById(s.getId()));
    }

    @Test
    void findByIdWhenNotExists() {
        assertNull(studentDao.selectById((-1)));
    }

    @Test
    void updateWhenExists() {
        Student s = new Student();
        s.setCode("UPD_E");
        s.setDescription("Test update when exists");
        s = studentDao.insert(s);
        s.setDescription("Test update when exists - updated");
        studentDao.update(s);

        Student sToCheck =  studentDao.selectById(s.getId());
        assertTrue("Test update when exists - updated".equals(sToCheck.getDescription()));

        assertNull(studentDao.selectById((-1)));
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
        assertTrue(studentDao.delete(ID));

    }

    @Test
    void deleteWhenNotExists() {
        Student s = new Student();
        s.setCode("DEL_NE");
        s.setDescription("Test delete when not exists");
        s = studentDao.insert(s);
        s.setId(s.getId()+1);
        assertFalse(studentDao.delete(ID));

    }
}
