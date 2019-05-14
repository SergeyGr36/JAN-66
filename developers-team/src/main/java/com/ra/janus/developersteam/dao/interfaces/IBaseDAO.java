package com.ra.janus.developersteam.dao.interfaces;

import java.util.List;

public interface IBaseDAO<T> {
    List<T> getAll();
    T getById(long id);
    boolean update(T entity);
    boolean delete(long id);
    boolean create(T entity);
}
