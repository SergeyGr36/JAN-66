package com.ra.janus.hotel.dao;

import com.ra.janus.hotel.entity.Client;
import com.ra.janus.hotel.exception.DaoException;
import com.ra.janus.hotel.configuration.ConnectionUtils;
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

public class ClientDAOIntegrationTest {
    private static DataSource dataSource;
    private static ClientDAO clientDao;

    @BeforeEach
    public void init() throws SQLException {
        dataSource = ConnectionUtils.getDefaultDataSource();
        clientDao = new ClientDAO(dataSource);
        Connection c = dataSource.getConnection();
        Statement statement = c.createStatement();
        statement.execute("TRUNCATE TABLE client");
    }


    @Test
    public void testMethodSaveClientInDB() throws DaoException {
        Client client = new Client("Tommy", "0960960606", "tommy@gmail.com", Date.valueOf(LocalDate.now()));
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

}
