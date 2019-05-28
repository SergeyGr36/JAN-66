package com.ra.janus.hotel.dao;

import com.ra.janus.hotel.entity.Client;
import com.ra.janus.hotel.enums.Query;
import com.ra.janus.hotel.exception.DaoException;
import com.ra.janus.hotel.dao.ClientDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


public class ClientDaoTestMock {
    private static Client mockClient;
    private static ClientDao clientDao;
    private static DataSource mockDataSource;
    private static Connection mockConnection;
    private static PreparedStatement mockStatement;
    private static ResultSet mockResultSet;
    private static Client client;

    @BeforeEach
    public void init() throws SQLException {
        mockDataSource = Mockito.mock(DataSource.class);
        clientDao = new ClientDao(mockDataSource);
        mockConnection = Mockito.mock(Connection.class);
        mockStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        mockClient = Mockito.mock(Client.class);
        client = new Client();
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
    }

    @Test
    public void whenInsertClientInDbThanSaveIt() throws SQLException, DaoException {
        when(mockConnection.prepareStatement(Query.CLIENT_SAVE.get(), PreparedStatement.RETURN_GENERATED_KEYS))
                .thenReturn(mockStatement);
        when(mockStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(1L);
        assertEquals(1L, clientDao.save(client).getId());
    }

    @Test
    public void whenSearchClientByIdThanReturnClient() throws SQLException {
        when(mockConnection.prepareStatement(Query.CLIENT_FIND_BY_ID.get()))
                .thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("id")).thenReturn(1L);
        assertEquals(1L, clientDao.findById(1L).getId());
    }

    @Test
    public void whenDeleteClientFromDbThenReturnOneIfDone() throws SQLException {
        when(mockConnection.prepareStatement(Query.CLIENT_DELETE.get())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);
        assertEquals(1, clientDao.delete(1L));
    }

    @Test
    public void whenUpdateClientInDbThanReturnUpdateClient() throws SQLException {
        when(mockConnection.prepareStatement(Query.CLIENT_UPDATE.get()))
                .thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);
        when(mockClient.getId()).thenReturn(1L);
        PreparedStatement statement = Mockito.mock(PreparedStatement.class);
        ResultSet rs = Mockito.mock(ResultSet.class);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(Query.CLIENT_FIND_BY_ID.get()))
                .thenReturn(statement);
        when(statement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getLong("id")).thenReturn(1L);
        assertEquals(1L, clientDao.findById(1L).getId());
        assertEquals(mockClient.getId(), clientDao.update(mockClient).getId());
    }

    @Test
    public void whenFindAllThanReturnClientList() throws SQLException {
        when(mockConnection.prepareStatement(Query.CLIENT_FIND_ALL.get())).thenReturn(mockStatement);
        List<Client> mockList = Mockito.mock(ArrayList.class);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getLong("id")).thenReturn(1l);
        when(mockList.add(mockClient)).thenReturn(true);
        assertNotNull(clientDao.findAll());
    }

    @Test
    public void whenFindAllThanThrowException() throws SQLException {
        when(mockDataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DaoException.class, () -> clientDao.findAll());

    }

    @Test
    public void whenUpdateClientInDbThanThrowException() throws SQLException {
        when(mockDataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DaoException.class, () -> clientDao.update(mockClient));
    }

    @Test
    public void whenDeleteClientFromDbThenThrowException() throws SQLException {
        when(mockDataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DaoException.class, () -> clientDao.delete(1L));
    }

    @Test
    public void whenFindByIdClientByIdThanThrowException() throws SQLException {
        when(mockDataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DaoException.class, () -> clientDao.findById(1L));
    }

    @Test
    public void whenSaveClientInDbThanThrowException() throws SQLException, DaoException {
        when(mockDataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DaoException.class, () -> clientDao.save(mockClient));

    }
}

