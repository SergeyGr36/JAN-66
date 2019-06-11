package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.entity.BaseEntity;
import com.ra.janus.developersteam.entity.Customer;
import java.util.HashMap;
import java.util.Map;

class PlainJdbcCustomerDAOMockTest extends BaseDAOMockTest {

    private Customer customer;

    @Override
    protected BaseEntity getTestEntity() {
        if (customer == null)
            customer = new Customer(testId, "John", "Home", "911");
        return customer;
    }
    @Override
    protected BaseDao getDAO() {
        BaseDao<Customer> customerDAO = new PlainJdbcCustomerDAO(mockTemplate);
        return customerDAO;
    }
    @Override
    protected String getInsertSql() {
        return "INSERT INTO customers (name, address, phone) VALUES (?, ?, ?)";
    }
    @Override
    protected String getUpdateSql() {
        return "UPDATE customers SET name=?,address=?,phone=? WHERE id=?";
    }
    @Override
    protected String getSelectAllSql() {
        return "SELECT * FROM customers";
    }
    @Override
    protected String getSelectOneSql() {
        return "SELECT * FROM customers WHERE id = ?";
    }
    @Override
    protected String getDeleteSql() {
        return "DELETE FROM customers WHERE id=?";
    }
    @Override
    protected Map<String, Object> getTestEntityMap() {
        Map<String, Object> testMap = new HashMap<>(1);
        Customer customer = (Customer) getTestEntity();
        testMap.put("id", customer.getId());
        testMap.put("name", customer.getName());
        testMap.put("address", customer.getAddress());
        testMap.put("phone", customer.getPhone());
        return testMap;
    }
}