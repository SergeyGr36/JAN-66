package com.ra.janus.faculty.dao;

public interface EntityDao<T> {
    T insert(T var);
    T update(T var);
    T select(T var);
    void delete(T var);
}
