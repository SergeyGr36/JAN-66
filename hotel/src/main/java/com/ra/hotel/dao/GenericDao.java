package com.ra.hotel.dao;

import com.ra.hotel.exception.DaoException;

import java.util.List;

public interface GenericDao<T> {

    T save(T t) throws DaoException;

    T update(T t) throws DaoException;

    long delete(long id) throws DaoException;

    T findById(long id) throws DaoException;

    List<T> findAll() throws DaoException;
}
