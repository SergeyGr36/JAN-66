package com.ra.course.janus.traintickets.dao.api;

import java.util.List;

public interface DAO<T> {

    T save(T item);

    boolean update(Long id, T item);

    boolean delete(Long id);

    T findById(Long id);

    List<T> findAll();
}
