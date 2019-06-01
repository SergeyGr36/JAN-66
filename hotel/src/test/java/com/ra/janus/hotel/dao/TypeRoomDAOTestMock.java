package com.ra.janus.hotel.dao;

import com.ra.janus.hotel.entity.TypeRoom;
import com.ra.janus.hotel.enums.Query;
import com.ra.janus.hotel.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TypeRoomDAOTestMock {

    private TypeRoomDAO typeRoomDAO;
    private DataSource mockDataSource;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

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
        when(mockConnection.prepareStatement(Query.INSERT_TYPE_BY_ID.get())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        assertNotNull(typeRoomDAO.save(typeRoom));
    }
    @Test
    void whenCallSaveThenReturnNothingSaved() throws SQLException {
        TypeRoom typeRoom = new TypeRoom();
        when(mockConnection.prepareStatement(Query.INSERT_TYPE_BY_ID.get())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        assertThrows(DaoException.class,()-> typeRoomDAO.save(typeRoom));
    }
    @Test
    void whenCallSaveThenReturnException() throws SQLException {
        TypeRoom typeRoom = new TypeRoom();
        when(mockConnection.prepareStatement(Query.INSERT_TYPE_BY_ID.get())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class,()-> typeRoomDAO.save(typeRoom));
    }


    @Test
    void whenCallUpdateThenReturnTypeRoom() throws SQLException, DaoException {
        TypeRoom typeRoom = new TypeRoom();
        when(mockConnection.prepareStatement(Query.UPDATE_TYPE_BY_ID.get())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        assertNotNull(typeRoomDAO.update(typeRoom));
    }
    @Test
    void whenCallUpdateThenReturnNothingUpdated () throws SQLException {
        TypeRoom typeRoom = new TypeRoom();
        when(mockConnection.prepareStatement(Query.UPDATE_TYPE_BY_ID.get())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        assertThrows(DaoException.class,()-> typeRoomDAO.update(typeRoom));
    }
    @Test
    void whenCallUpdateThenReturnException() throws SQLException {
        TypeRoom typeRoom = new TypeRoom();
        when(mockConnection.prepareStatement(Query.UPDATE_TYPE_BY_ID.get())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class,()-> typeRoomDAO.update(typeRoom));
    }



    @Test
    void whenCallDeleteThenDeleted () throws SQLException {
        when(mockConnection.prepareStatement(Query.DELETE_TYPE_BY_ID.get())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        assertDoesNotThrow(()->typeRoomDAO.delete(1L));
    }
    @Test
    void whenCallDeleteThenNothingDeleted () throws SQLException {
        when(mockConnection.prepareStatement(Query.DELETE_TYPE_BY_ID.get())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        assertEquals(typeRoomDAO.delete(1L),0);
    }
    @Test
    void whenCallDeleteThenReturnException() throws SQLException {
        when(mockConnection.prepareStatement(Query.DELETE_TYPE_BY_ID.get())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(DaoException.class,()-> typeRoomDAO.delete(1L));
    }


    @Test
    void whenCallFindByIdThenReturnTypeRoom() throws SQLException, DaoException {
        final long id = 1L;
        when(mockConnection.prepareStatement(Query.SELECT_TYPE_BY_ID.get())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("ID")).thenReturn(id);
        TypeRoom typeRoom = typeRoomDAO.findById(id);
        assertEquals(id, typeRoom.getId());
    }
    @Test
    void whenCallFindByIdThenReturnNothing() throws SQLException, DaoException {
        final long id = 1L;
        when(mockConnection.prepareStatement(Query.SELECT_TYPE_BY_ID.get())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        assertNull(typeRoomDAO.findById(id));
    }
    @Test
    void whenCallFindByIdThenReturnException() throws SQLException {
        final long id = 1L;
        when(mockConnection.prepareStatement(Query.SELECT_TYPE_BY_ID.get())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class,()->typeRoomDAO.findById(id));
    }



    @Test
    void whenCallFindAllThenReturnList() throws SQLException, DaoException {
        when(mockConnection.prepareStatement(Query.SELECT_ALL_TYPES.get())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        List <TypeRoom> typeRooms = typeRoomDAO.findAll();
        assertEquals(2,typeRooms.size());
    }
    @Test
    void whenCallFindAllThenReturnEmptyList() throws SQLException, DaoException {
        when(mockConnection.prepareStatement(Query.SELECT_ALL_TYPES.get())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        List <TypeRoom> typeRooms = typeRoomDAO.findAll();
        assertEquals(0,typeRooms.size());
    }
    @Test
    void whenCallFindAllThenReturnException() throws SQLException {
        when(mockConnection.prepareStatement(Query.SELECT_ALL_TYPES.get())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(DaoException.class,()->typeRoomDAO.findAll());
    }

}
