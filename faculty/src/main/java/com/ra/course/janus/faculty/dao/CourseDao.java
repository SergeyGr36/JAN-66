package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.entity.Course;

import java.sql.SQLException;
import java.util.List;

public interface CourseDao {

    Course insert(Course course) throws SQLException;

    Course update(Course course) throws SQLException;

    boolean delete(Course course) throws SQLException;

    Course findByTid(long tid) throws SQLException;

    List<Course> findAll() throws SQLException;
}
