package com.ra.janus.dao;

import java.util.List;

public interface Dao <T> {
    public List<T> getAll();
    public T getById(long id);
    public boolean update(T entity);
    public boolean delete(long id);
    public boolean create(T entity);
}
