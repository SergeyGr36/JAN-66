package com.ra.course.janus.traintickets.dao.api;

import com.ra.course.janus.traintickets.entity.Train;

import java.util.List;

public class TrainDAO implements DAO<Train> {
    @Override
    public Train save(Train item) {
        return null;
    }

    @Override
    public boolean update(Long id, Train item) {
        return false;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public Train findById(Long id) {
        return null;
    }

    @Override
    public List<Train> findAll() {
        return null;
    }
}
