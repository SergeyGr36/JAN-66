package com.ra.janus.developersteam.dao.interfaces;

import com.ra.janus.developersteam.entity.Work;

import java.util.List;

public interface WorkDAO {
    List<Work> readAll();

    Work read(long id);

    boolean update(Work entity);

    boolean delete(long id);

    Work create(Work entity);
}
