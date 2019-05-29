package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.datasources.DataSourceFactory;
import com.ra.janus.developersteam.entity.Customer;
import com.ra.janus.developersteam.schema.DBSchemaCreator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.*;

public class PlainJdbcCustomerDAOIntegrationTest {
    private static final DataSource dataSource = new DataSourceFactory().get();
    private static final PlainJdbcCustomerDAO customerDAO = new PlainJdbcCustomerDAO(dataSource);

    private static Customer customerToCreate;

    @BeforeAll
    public static void beforeAll() throws Exception {
        customerToCreate = new Customer(1L, "John",null, null);
    }

    @Test
    void ShouldCreateCustomer() throws Exception {
        //given
        try (Connection connection = dataSource.getConnection();){

            DBSchemaCreator.createSchema(connection);

            //when
            Customer customer = customerDAO.create(customerToCreate);

            //then
            assertEquals(customerToCreate, customer);
        }
    }

    @Test
    void ShouldGetCustomer() throws Exception {
        //given
        try (Connection connection = dataSource.getConnection();){

            DBSchemaCreator.createSchema(connection);

            //when
            Customer createdCustomer = customerDAO.create(customerToCreate);
            Customer gottenCustomer = customerDAO.get(createdCustomer.getId());

            //then
            assertEquals(gottenCustomer, createdCustomer);
        }
    }

}
