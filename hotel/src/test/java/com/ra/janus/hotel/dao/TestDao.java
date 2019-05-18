package com.ra.janus.hotel.dao;

import com.ra.janus.hotel.configuration.H2ConnectionUtils;
import com.ra.janus.hotel.entity.Client;
import com.ra.janus.hotel.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestDao {
    private static DataSource dataSource;
    private static ClientDao clientDao;

    @BeforeEach
    public void init() throws SQLException {
        dataSource = H2ConnectionUtils.getDefaultDataSource();
        clientDao = new ClientDao(dataSource);
        Connection c = dataSource.getConnection();
        Statement statement = c.createStatement();
        statement.execute(Query.TRUNCATE_CLIENT_TABLE);
    }


    @Test
    public void testMethodSaveClientInDB() throws DaoException {

        Client client = new Client("Tommy", "0960960606", "tommy@gmail.com",
                Date.valueOf(LocalDate.now()));
        clientDao.save(client);
        assertEquals(client, clientDao.findById(client.getId()));
    }


    @Test
    public void testMethodUpdateClientInDB() throws DaoException {
        Client client = new Client("Tommy", "0960960606", "tommy@gmail.com",
                Date.valueOf(LocalDate.now()));
        clientDao.save(client);
        Client client1 = new Client(client.getId(), "Sam", "0000000000", "sam@",
                Date.valueOf(LocalDate.now()));
        clientDao.update(client1);
        assertEquals(client1, clientDao.findById(client.getId()));
    }


    @Test
    public void testMethodDeleteClientInDB() throws DaoException {
        Client client = new Client("TommyString", "0960960606", "tommy@gmail.com",
                Date.valueOf(LocalDate.now()));
        clientDao.save(client);
        int i = clientDao.delete(client.getId());
        assertEquals(i, 1);
    }


    @Test
    public void testFindByIdInDB() throws DaoException {
        Client client = new Client("Tommy", "0960960606", "tommy@gmail.com",
                Date.valueOf(LocalDate.now()));
        clientDao.save(client);
        Client client1 = new Client(client.getId(), "Sam", "0000000000", "sam@",
                Date.valueOf(LocalDate.now()));
        clientDao.save(client1);
        assertEquals(client1, clientDao.findById(client1.getId()));
    }


    @Test
    public void testFindAllInDB() throws DaoException {
        Client client = new Client("Tommy", "0960960606", "tommy@gmail.com",
                Date.valueOf(LocalDate.now()));
        clientDao.save(client);
        Client client1 = new Client(client.getId(), "Sam", "0000000000", "sam@",
                Date.valueOf(LocalDate.now()));
        clientDao.save(client1);
        List<Client> clientList1 = new ArrayList<>();
        clientList1.add(client);
        clientList1.add(client1);

        List<Client> clientList = clientDao.findAll();

        assertEquals(clientList, clientList1);
    }


    @Test
    public void testMethodFindByPhoneNumberInDB() throws DaoException {
        Client client = new Client("Tommy", "0960960606", "tommy@gmail.com",
                Date.valueOf(LocalDate.now()));
        clientDao.save(client);
        Client client1 = new Client(client.getId(), "Sam", "0000000000", "sam@",
                Date.valueOf(LocalDate.now()));
        clientDao.save(client1);
        Client client2 = new Client(client.getId(), "Ben", "0000000000", "sam@",
                Date.valueOf(LocalDate.now()));
        clientDao.save(client2);
        Client client3 = new Client(client.getId(), "Josh", "0000000000", "sam@",
                Date.valueOf(LocalDate.now()));
        clientDao.save(client3);
        Client client4 = new Client(client.getId(), "Sam", "0000000000", "sam@",
                Date.valueOf(LocalDate.now()));
        clientDao.save(client4);

        List<Client> clientList = new ArrayList<>();
        clientList.add(client1);
        clientList.add(client2);
        clientList.add(client3);
        clientList.add(client4);

        assertEquals(clientList, clientDao.findByPhoneNumber("0000000000"));
    }


    @Test
    public void testMethodSaveClientInDbWithException() {
        Client client = new Client(null, "0960960606", "tommy@gmail.com",
                Date.valueOf(LocalDate.now()));
        assertThrows(DaoException.class, () -> clientDao.save(client));

    }


    @Test
    public void testMethodUpdateClientInDbWithException() throws DaoException {
        Client client = new Client("Tommy", "0960960606", "tommy@gmail.com",
                Date.valueOf(LocalDate.now()));
        clientDao.save(client);
        Client client1 = new Client(4, "Sam", "0000000000", "sam@",
                Date.valueOf(LocalDate.now()));
        assertThrows(DaoException.class, () -> clientDao.update(client1));
    }


    @Test
    public void testMethodDeleteClientInDbWithException() {
        assertThrows(DaoException.class, () -> clientDao.delete(null));
    }


    @Test
    public void testFindByIdInDbWithException() {
        assertThrows(DaoException.class, () -> clientDao.findById(null));
    }


}
