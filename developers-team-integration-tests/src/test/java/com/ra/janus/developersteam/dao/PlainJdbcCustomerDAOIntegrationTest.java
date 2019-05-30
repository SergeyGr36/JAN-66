package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.datasources.DataSourceFactory;
import com.ra.janus.developersteam.entity.Customer;
import com.ra.janus.developersteam.schema.DBSchemaCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class PlainJdbcCustomerDAOIntegrationTest {
    private static final DataSource dataSource = DataSourceFactory.get();
    private static final PlainJdbcCustomerDAO customerDAO = new PlainJdbcCustomerDAO(dataSource);

    private static Customer customerToCreate = new Customer(1L, "John", "Home", "12345");

    @BeforeEach
    public void beforeEach() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            DBSchemaCreator.createSchema(connection, "CUSTOMERS");
        }
    }

    @Test
    void ShouldCreateCustomer() throws Exception {
        //when
        Customer customer = customerDAO.create(customerToCreate);

        //then
        assertEquals(customer, customerDAO.get(customer.getId()));
    }

    @Test
    void ShouldGetCustomer() throws Exception {
        //when
        Customer createdCustomer = customerDAO.create(customerToCreate);
        Customer gottenCustomer = customerDAO.get(createdCustomer.getId());

        //then
        assertEquals(createdCustomer, gottenCustomer);
    }

    @Test
    void ShouldGetAllCustomers() throws Exception {
        //when
        Customer createdCustomer = customerDAO.create(customerToCreate);
        List<Customer> expected = Arrays.asList(createdCustomer);
        List<Customer> actual = customerDAO.getAll();

        //then
        assertIterableEquals(expected, actual);
    }

    @Test
    void ShouldUpdateCustomer() throws Exception {
        //when
        Customer createdCustomer = customerDAO.create(customerToCreate);
        createdCustomer.setName("Jack");
        createdCustomer.setAddress("Somewhere");
        createdCustomer.setPhone("54321");
        customerDAO.update(createdCustomer);
        Customer updated = customerDAO.get(createdCustomer.getId());

        //then
        assertEquals(updated, createdCustomer);
    }

    @Test
    void ShouldDeleteCustomer() throws Exception {
        //when
        Customer createdCustomer = customerDAO.create(customerToCreate);
        customerDAO.delete(createdCustomer.getId());
        Customer actual = customerDAO.get(createdCustomer.getId());

        //then
        assertEquals(null, actual);
    }

}
