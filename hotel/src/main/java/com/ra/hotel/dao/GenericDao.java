package com.ra.hotel.dao;

import java.util.List;

public interface GenericDao<T> {

    T save(T t);

    T update(T t);

    int delete(Long id);

    T findById(Long id);

    List<T> findAll();
}
