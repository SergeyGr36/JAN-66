package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.entity.Course;

import java.util.List;

public interface CourseDao {

    Course insert(Course course);

    Course update(Course course);

    boolean delete(Course course);

    Course findByTid(long tid);

    List<Course> findAll();
}
