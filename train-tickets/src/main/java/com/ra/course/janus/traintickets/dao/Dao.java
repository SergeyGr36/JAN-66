package com.ra.course.janus.traintickets.dao;

import java.util.List;

public interface Dao<T> {

    public T save(T item);

    public T update(Long id, T item);

    public T delete(Long id);

    public T findById(Long id);

    public List<T> findAll();

}
