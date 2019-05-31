package com.ra.course.janus.faculty.dao;

import java.util.List;

public interface TeacherDao<T> {
    T insert(T teacher);

    boolean update(T teacher);

    List<T> select();

    boolean delete();
}
