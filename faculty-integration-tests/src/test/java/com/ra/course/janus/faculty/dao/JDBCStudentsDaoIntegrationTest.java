package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.connect.ConnectionUtil;
import com.ra.course.janus.faculty.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



public class JDBCStudentsDaoIntegrationTest {
    public static JDBCDaoStudent studentDao;

    private static final long ID = 1;

    @BeforeEach
    public void beforeEach () {
        studentDao = new JDBCDaoStudent( ConnectionUtil.getDataSource());
    }

    @Test

    public void insert(){
        Student s = new Student();
        s.setCode("J2EE");
        s.setDescription("Java web development course");
        s = studentDao.insert(s);
        assertNotNull(s.getId());
    }

    @Test
    public void findAllWhenNotExists() {
        studentDao.delete(1);
        studentDao.delete(2);
        studentDao.delete(3);
        studentDao.delete(4);
        assertEquals(0,studentDao.select().size());
    }

    @Test
    public void findAllWhenExists() {
        int n0 = studentDao.select().size();
        insert();
        insert();
        insert();
        assertEquals(n0+3,studentDao.select().size());
    }

    @Test
    public void findByIdWhenExists() {
        Student s = new Student();
        s.setCode("J2EE");
        s.setDescription("Java web development course");

        s = studentDao.insert(s);
        assertNotNull(studentDao.selectById(s.getId()));
    }

    @Test
    public void findByIdWhenNotExists() {
        assertNull(studentDao.selectById((-1)));
    }

    @Test
    public void updateWhenExists() {
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
    public void updateWhenNotExists() {
        Student s = new Student();
        s.setCode("UPD_NE");
        s.setDescription("Test update when not exists");
        s = studentDao.insert(s);
        s.setDescription("Test update when exists - updated");
        s.setId((s.getId()+1));
        assertFalse(studentDao.update(s));

    }

    @Test
    public void deleteWhenExists() {
        Student s = new Student();
        s.setCode("DEL_E");
        s.setDescription("Test delete when exists");
        studentDao.insert(s);
        assertTrue(studentDao.delete(ID));

    }

    @Test
    public void deleteWhenNotExists() {
        Student s = new Student();
        s.setCode("DEL_NE");
        s.setDescription("Test delete when not exists");
        s = studentDao.insert(s);
        s.setId(s.getId()+1);
        assertFalse(studentDao.delete(ID));

    }
}
