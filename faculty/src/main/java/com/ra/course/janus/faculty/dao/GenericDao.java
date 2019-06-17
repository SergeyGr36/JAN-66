package com.ra.course.janus.faculty.dao;

import java.util.List;

public interface GenericDao<T> {
    T insert(T teacher);

    boolean update(T teacher);

    List<T> select();

    T selectById(long id);

    boolean delete(long id);
}
