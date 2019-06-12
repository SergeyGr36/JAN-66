package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.BaseEntity;
import com.ra.janus.developersteam.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;

public class PlainJdbcCustomerDAOIntegrationTest extends BaseDAOIntegrationTest {
    @Autowired
    private BaseDao<Customer> customerDAO;

    protected Customer customerToCreate = new Customer(1L, "John", "Home", "12345");

    @Override
    protected BaseDao getDAO() {
        return customerDAO;
    }

    @Override
    protected BaseEntity getEntityToCreate() {
        return customerToCreate;
    }

    @Override
    protected BaseEntity getUpdatedEntity(BaseEntity entity) {

        Customer updatedCustomer = (Customer) entity;
        updatedCustomer.setName("Jack");
        updatedCustomer.setAddress("Somewhere");
        updatedCustomer.setPhone("54321");

        return updatedCustomer;
    }
}
