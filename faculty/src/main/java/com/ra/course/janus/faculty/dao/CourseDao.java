package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.entity.Course;

import java.util.List;

public interface CourseDao {
    Course insert(Course course);

    boolean update(Course course);

    boolean delete(Course course);

    Course selectById(long tid);

    List<Course> select();
}
