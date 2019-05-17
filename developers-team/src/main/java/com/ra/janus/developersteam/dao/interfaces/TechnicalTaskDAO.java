package com.ra.janus.developersteam.dao.interfaces;

import com.ra.janus.developersteam.entity.TechnicalTask;

import java.util.List;

public interface TechnicalTaskDAO {
    List<TechnicalTask> readAll();

    TechnicalTask read(long id);

    boolean update(TechnicalTask entity);

    boolean delete(long id);

    long create(TechnicalTask entity);
}
