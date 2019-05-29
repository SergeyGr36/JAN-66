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

    @BeforeEach
    public void beforeEach() throws Exception {

    }
    @Test
    void whenCreateCustomerShouldReturnCustomer() throws Exception {
        //given
        long testId = 1L;
        Customer testCustomer = new Customer(testId, "John",null, null);
        try (Connection connection = dataSource.getConnection();){

            DBSchemaCreator.createSchema(connection);

            Customer customer = customerDAO.create(testCustomer);
            assertEquals(testCustomer, customer);
        }
    }
}
