package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.connect.ConnectionUtil;
import com.ra.course.janus.faculty.entity.Course;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class JDBCCourseDaoIntegrationTest {

    public static JDBCCourseDao courseDao;

    private static final long ID = 1;

    @BeforeEach
    public void beforeEach () {
        courseDao = new JDBCCourseDao(ConnectionUtil.getDataSource());
    }

    @Test
    public void insert(){
        //given
        Course c = new Course("J2EE","Java web development course");
        //when
        c = courseDao.insert(c);
        //then
        assertNotNull(c.getTid());
    }

    @Test
    public void findAllWhenNotExists() {
        for (int a = 1; a <= courseDao.select().size(); a++) {
            courseDao.delete(a);
        }

        assertEquals(0, courseDao.select().size());
    }

    @Test
    public void findAllWhenExists() {
        //given
        int n0 = courseDao.select().size();

        //when
        insert();

        //then
        assertEquals(n0+1,courseDao.select().size());
    }

    @Test
    public void findByIdWhenExists() {
        //when
        Course c = courseDao.insert( new Course("J2EE","Java web development course"));

        //then
        assertNotNull(courseDao.selectById(c.getTid()));
    }

    @Test
    public void findByIdWhenNotExists() {
        assertNull(courseDao.selectById(-1));
    }

    @Test
    public void updateWhenExists() {
        //when
        Course c = courseDao.insert(new Course("UPD_E","Test update when exists"));
        c.setDescription("Test update when exists - updated");
        courseDao.update(c);

        //then
        Course cToCheck = courseDao.selectById(c.getTid());
        assertTrue("Test update when exists - updated".equals(cToCheck.getDescription()));
    }

    @Test
    public void updateWhenNotExists() {
        //when
        Course c = courseDao.insert(new Course("UPD_NE","Test update when not exists"));
        c.setDescription("Test update when exists - updated");
        c.setTid(c.getTid()+1);

        //then
        assertFalse(courseDao.update(c));

    }

    @Test
    public void deleteWhenExists() {
        Course course = courseDao.insert(new Course("UPD_NE", "Test delete when exists"));
        assertTrue(courseDao.delete(course.getTid()));
    }

    @Test
    public void deleteWhenNotExists() {
        //when
        Course c= courseDao.insert(new Course("DEL_NE","Test delete when not exists"));
        c.setTid(c.getTid()+1);

        //then
        assertFalse(courseDao.delete(ID));

    }
}
