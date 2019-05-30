package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.entity.Course;
import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CourseDaoJdbcIntegrationTest {

    public static  Connection conn ;//= ConnectionFactory.getInstance();
    public static  CourseDaoJdbc courseDao ;//=  new CourseDaoJdbc(conn);

    @BeforeEach
    public void beforeEach () throws Exception{
        courseDao = new CourseDaoJdbc(ConnectionFactory.getInstance().getConnection());
    }

    @Test
    void insert(){
        Course c = new Course();
        c.setCode("J2EE");
        c.setDescription("Java web developmnt course");

        c = courseDao.insert(c);
        assertNotNull(c.getTid());
    }


    @Test
    void findAllWhenNotExists() {
        List<Course> cources = courseDao.findAll();
        for (Course c : cources){
            courseDao.delete(c);
        }
        assertEquals(0,courseDao.findAll().size());
    }


    @Test
    void findAllWhenExists() {
        int n0 = courseDao.findAll().size();
        insert();
        insert();
        insert();
        assertEquals(n0+3,courseDao.findAll().size());
    }


    @Test
    void findByIdWhenExists() {
        Course c = new Course();
        c.setCode("J2EE");
        c.setDescription("Java web development course");

        c = courseDao.insert(c);
        assertNotNull(courseDao.findByTid(c.getTid()));
    }

    @Test
    void findByIdWhenNotExists() {
        assertNull(courseDao.findByTid(-1));
    }

    @Test
    void updateWhenExists() {
        Course c = new Course();
        c.setCode("UPD_E");
        c.setDescription("Test update when exists");
        c = courseDao.insert(c);
        c.setDescription("Test update when exists - updated");
        courseDao.update(c);

        Course cToCheck = courseDao.findByTid(c.getTid());
        assertTrue("Test update when exists - updated".equals(cToCheck.getDescription()));

        assertNull(courseDao.findByTid(-1));
    }

    @Test
    void updateWhenNotExists() {
        Course c = new Course();
        c.setCode("UPD_NE");
        c.setDescription("Test update when not exists");
        c = courseDao.insert(c);
        c.setDescription("Test update when exists - updated");
        c.setTid(c.getTid()+1);
        assertNull(courseDao.update(c));

    }

    @Test
    void deleteWhenExists() {
        Course c = new Course();
        c.setCode("DEL_E");
        c.setDescription("Test delete when exists");
        c = courseDao.insert(c);
        assertTrue(courseDao.delete(c));

    }

    @Test
    void deleteWhenNotExists() {
        Course c = new Course();
        c.setCode("DEL_NE");
        c.setDescription("Test delete when not exists");
        c = courseDao.insert(c);
        c.setTid(c.getTid()+1);
        assertFalse(courseDao.delete(c));

    }
}
