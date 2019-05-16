package com.ra.janus.developersteam.dao.interfaces;

import com.ra.janus.developersteam.entity.Customer;

import java.util.List;

public interface CustomerDAO {
    List<Customer> readAll();

    Customer read(long id);

    boolean update(Customer entity);

    boolean delete(long id);

    long create(Customer entity);
}
