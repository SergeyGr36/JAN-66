package com.ra.janus.hotel.dao;

import com.ra.janus.hotel.entity.Client;
import com.ra.janus.hotel.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;

import java.sql.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


public class MockClientDaoTest {
    private static Client mockClient;
    private static ClientDao clientDao;
    private static DataSource mockDataSource;
    private static Connection mockConnection;
    private static PreparedStatement mockStatement;
    private static ResultSet mockResultSet;


    @BeforeEach
    public void init() {
        mockDataSource = Mockito.mock(DataSource.class);
        clientDao = new ClientDao(mockDataSource);
        mockConnection = Mockito.mock(Connection.class);
        mockStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        mockClient = Mockito.mock(Client.class);
    }

    @Test
    public void whenInsertClientInDbThanSaveIt() throws SQLException, DaoException {
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(Query.CLIENT_SAVE_SQL, PreparedStatement.RETURN_GENERATED_KEYS))
                .thenReturn(mockStatement);
        when(mockStatement.getGeneratedKeys()).thenReturn(mockResultSet);
    }
}
