package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PlainJdbcCustomerDAOIntegrationTest extends BaseDAOIntegrationTest {
    @Autowired
    private BaseDao<Customer> customerDAO;

    private Customer customerToCreate = new Customer(1L, "John", "Home", "12345");

    private Delegate updatedEntity() {

        return  (entity) -> {
            Customer updatedCustomer = (Customer) entity;
            updatedCustomer.setName("Jack");
            updatedCustomer.setAddress("Somewhere");
            updatedCustomer.setPhone("54321");

            return updatedCustomer;
        };
    }

    @Test
    public void createCustomerTest() throws Exception {
        createEntityTest(customerDAO, customerToCreate);
    }

    @Test
    public void getCustomerByIdTest() throws Exception {
        getEntityByIdTest(customerDAO, customerToCreate);
    }

    @Test
    public void getAllCustomersTest() throws Exception {
        getAllEntitiesTest(customerDAO, customerToCreate);
    }

    @Test
    public void updateCustomerTest() throws Exception {
        updateEntityTest(customerDAO, customerToCreate, updatedEntity());
    }

    @Test
    public void deleteCustomerTest() throws Exception {
        deleteEntityTest(customerDAO, customerToCreate);
    }
}
