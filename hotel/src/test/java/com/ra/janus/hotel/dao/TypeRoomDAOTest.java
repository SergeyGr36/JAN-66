package com.ra.janus.hotel.dao;

import com.ra.janus.hotel.entity.TypeRoom;
import com.ra.janus.hotel.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TypeRoomDAOTest {

    private TypeRoomDAO typeRoomDAO;
    @Mock
    private DataSource mockDataSource;
    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;

    private final static String INSERT_BY_ID = "INSERT INTO TYPE_ROOM (COUNT_PLACES, PRISE, DESCRIPTION, CLASS_OF_ROOM, ID) VALUES (?, ?, ?, ?, ?)";
    private final static String UPDATE_BY_ID = "UPDATE TYPE_ROOM SET COUNT_PLACES = ?, PRISE = ?, DESCRIPTION = ?, CLASS_OF_ROOM = ? WHERE ID = ?";
    private final static String DELETE_BY_ID = "DELETE FROM TYPE+ROOM WHERE ID = ?";
    private final static String SELECT_BY_ID = "SELECT * FROM TYPE_ROOM WHERE ID = ?";
    private final static String SELECT_ALL = "SELECT * FROM TYPE_ROOM";

    @BeforeEach
    void before() throws Exception {
        mockDataSource = Mockito.mock(DataSource.class);
        typeRoomDAO = new TypeRoomDAO(mockDataSource);
        mockConnection = Mockito.mock(Connection.class);
        Mockito.when(mockDataSource.getConnection()).thenReturn(mockConnection);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        }


    @Test
    void whenCallSaveThenReturnTypeRoom() throws SQLException, DaoException {
        TypeRoom typeRoom = new TypeRoom();
        when(mockConnection.prepareStatement(INSERT_BY_ID)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        assertNotNull(typeRoomDAO.save(typeRoom));
    }
    @Test
    void whenCallSaveThenReturnNothingSaved() throws SQLException {
        TypeRoom typeRoom = new TypeRoom();
        when(mockConnection.prepareStatement(INSERT_BY_ID)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        assertThrows(DaoException.class,()-> typeRoomDAO.save(typeRoom));
    }
    @Test
    void whenCallSaveThenReturnException() throws SQLException {
        TypeRoom typeRoom = new TypeRoom();
        when(mockConnection.prepareStatement(INSERT_BY_ID)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class,()-> typeRoomDAO.save(typeRoom));
    }


    @Test
    void whenCallUpdateThenReturnTypeRoom() throws SQLException, DaoException {
        TypeRoom typeRoom = new TypeRoom();
        when(mockConnection.prepareStatement(UPDATE_BY_ID)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        assertNotNull(typeRoomDAO.update(typeRoom));
    }
    @Test
    void whenCallUpdateThenReturnNothingUpdated () throws SQLException {
        TypeRoom typeRoom = new TypeRoom();
        when(mockConnection.prepareStatement(UPDATE_BY_ID)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        assertThrows(DaoException.class,()-> typeRoomDAO.update(typeRoom));
    }
    @Test
    void whenCallUpdateThenReturnException() throws SQLException {
        TypeRoom typeRoom = new TypeRoom();
        when(mockConnection.prepareStatement(UPDATE_BY_ID)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class,()-> typeRoomDAO.update(typeRoom));
    }


    @Test
    void whenCallDeleteThenDeleted () throws SQLException {
        when(mockConnection.prepareStatement(DELETE_BY_ID)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        assertDoesNotThrow(()->typeRoomDAO.delete(1L));
    }
    @Test
    void whenCallDeleteThenNothingDeleted () throws SQLException {
        when(mockConnection.prepareStatement(DELETE_BY_ID)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        assertThrows(DaoException.class,()-> typeRoomDAO.delete(1L));
    }
    @Test
    void whenCallDeleteThenReturnException() throws SQLException {
        when(mockConnection.prepareStatement(DELETE_BY_ID)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class,()-> typeRoomDAO.delete(1L));
    }
}
