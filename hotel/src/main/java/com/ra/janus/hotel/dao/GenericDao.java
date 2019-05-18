package com.ra.janus.hotel.dao;

import com.ra.janus.hotel.exception.DaoException;

import java.util.List;

public interface GenericDao<T> {

    T save(T t) throws DaoException;

    T update(T t) throws DaoException;

    int delete(Long id) throws DaoException;

    T findById(Long id) throws DaoException;

    List<T> findByPhoneNumber(String phoneNumber) throws DaoException;
/*
    List<T> findListByParam(Object... params) throws DaoException;
*/

    List<T> findAll() throws DaoException;
}
