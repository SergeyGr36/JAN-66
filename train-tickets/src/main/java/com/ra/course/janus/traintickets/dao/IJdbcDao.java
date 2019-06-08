package com.ra.course.janus.traintickets.dao;

import java.util.List;

public interface IJdbcDao<T> {

    T save(T item);

    boolean update(Long id, T item);

    boolean delete(Long id);

    T findById(Long id);

    List<T> findAll();
}
