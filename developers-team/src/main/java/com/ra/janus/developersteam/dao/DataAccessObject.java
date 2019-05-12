package com.ra.janus.developersteam.dao;

import java.util.List;

public interface DataAccessObject<T> {
    List<T> getAll();
    T getById(long id);
    boolean update(T entity);
    boolean delete(long id);
    boolean create(T entity);
}
