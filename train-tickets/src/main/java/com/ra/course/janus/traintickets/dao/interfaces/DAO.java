package com.ra.course.janus.traintickets.dao.interfaces;

import java.util.List;

public interface DAO<T> {

    T save(T item);

    T update(Long id, T item);

    T delete(Long id);

    T findById(Long id);

    List<T> findAll();

}
