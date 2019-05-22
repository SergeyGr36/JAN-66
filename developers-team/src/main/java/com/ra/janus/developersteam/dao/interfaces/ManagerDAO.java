package com.ra.janus.developersteam.dao.interfaces;

import com.ra.janus.developersteam.entity.Manager;

import java.util.List;

public interface ManagerDAO {

    List<Manager> findAll();
    Manager findById(Long id);
    Manager findByName(String name);
    Manager create(Manager manager);
    Boolean updade(Manager manager);
    Boolean delete(Long id);

}
