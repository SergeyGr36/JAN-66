package com.ra.janus.developersteam.dao.interfaces;

import com.ra.janus.developersteam.entity.Manager;

import java.util.List;

public interface ManagerDAO {

    List<Manager> findAll();
    Manager findById(Long id);
    Manager findByName(String name);
    void create(Long id);
    void updade(Long id);
    void delete(Long id);

}
