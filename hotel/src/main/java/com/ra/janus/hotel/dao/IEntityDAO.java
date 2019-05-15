package com.ra.janus.hotel.dao;

import com.ra.janus.hotel.exception.DaoException;

import java.util.List;

public interface IEntityDAO<T> {

    T save(T t) throws DaoException;
    T update(T t) throws DaoException;
    void delete (long id) throws DaoException;
    T findById(long id) throws DaoException;
    List<T> findAll() throws DaoException;
}
