package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.connect.ConnectionUtil;
import com.ra.course.janus.faculty.entity.Course;
import org.junit.jupiter.api.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JDBCCourseDaoIntegrationTest {

    public static JDBCCourseDao courseDao;

    private static final long ID = 1;

    @BeforeEach
    public void beforeEach () {
        courseDao = new JDBCCourseDao(ConnectionUtil.getDataSource());
    }

    @Test
    void insert(){
        //given
        Course c = new Course("J2EE","Java web developmnt course");

        //when
        c = courseDao.insert(c);
        //then
        assertNotNull(c.getTid());
    }

    @Test
    void findAllWhenNotExists() {
        //when
        List<Course> cources = courseDao.select();

        courseDao.delete(1);
        courseDao.delete(2);
        courseDao.delete(3);
        courseDao.delete(4);

        //then
        assertEquals(0,courseDao.select().size());
    }


    @Test
    void findAllWhenExists() {
        //given
        int n0 = courseDao.select().size();

        //when
        insert();

        //then
        assertEquals(n0+1,courseDao.select().size());
    }

    @Test
    void findByIdWhenExists() {
        //when
        Course c = courseDao.insert( new Course("J2EE","Java web development course"));

        //then
        assertNotNull(courseDao.selectById(c.getTid()));
    }

    @Test
    void findByIdWhenNotExists() {
        assertNull(courseDao.selectById(-1));
    }

    @Test
    void updateWhenExists() {
        //when
        Course c = courseDao.insert(new Course("UPD_E","Test update when exists"));
        c.setDescription("Test update when exists - updated");
        courseDao.update(c);

        //then
        Course cToCheck = courseDao.selectById(c.getTid());
        assertTrue("Test update when exists - updated".equals(cToCheck.getDescription()));
    }

    @Test
    void updateWhenNotExists() {
        //when
        Course c = courseDao.insert(new Course("UPD_NE","Test update when not exists"));
        c.setDescription("Test update when exists - updated");
        c.setTid(c.getTid()+1);

        //then
        assertFalse(courseDao.update(c));

    }

    @Test
    void deleteWhenExists() {
        //when
        Course c= courseDao.insert(new Course("DEL_E","Test delete when exists"));

        //then
        assertTrue(courseDao.delete(ID));
    }

    @Test
    void deleteWhenNotExists() {
        //when
        Course c= courseDao.insert(new Course("DEL_NE","Test delete when not exists"));
        c.setTid(c.getTid()+1);

        //then
        assertFalse(courseDao.delete(ID));

    }
}
