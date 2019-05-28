package com.ra.janus.hotel.dao;

import java.util.List;

public interface GenericDao<T> {

    T save(T t);

    T update(T t);

    int delete(long id);

    T findById(long id);

    List<T> findAll();
}
