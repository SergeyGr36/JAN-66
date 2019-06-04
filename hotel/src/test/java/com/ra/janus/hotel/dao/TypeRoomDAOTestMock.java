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

    private static DataSource mockDataSource;
    private static TypeRoom typeRoom;
    private static TypeRoomDAO typeRoomDAO;
    private static Connection mockConnection;
    private static PreparedStatement mockPreparedStatement;
    private static ResultSet mockResultSet;

    @BeforeEach
    public void before() throws Exception {
        mockDataSource = Mockito.mock(DataSource.class);
        typeRoom = new TypeRoom();
        typeRoomDAO = new TypeRoomDAO(mockDataSource);
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);

        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(1L);
        when(mockResultSet.getLong("ID")).thenReturn(1L);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        }


    @Test
    public void whenCallSaveThenReturnTypeRoom() throws SQLException, DaoException {
        //when
        when(mockConnection.prepareStatement(Query.INSERT_TYPE_BY_ID.get(), PreparedStatement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        //then
        assertNotNull(typeRoomDAO.save(typeRoom));
    }

    @Test
    public void whenCallSaveThenReturnNothingSaved() throws SQLException {
        //when
        when(mockConnection.prepareStatement(Query.INSERT_TYPE_BY_ID.get(), PreparedStatement.RETURN_GENERATED_KEYS)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        //then
        assertThrows(DaoException.class,()-> typeRoomDAO.save(typeRoom));
    }

    @Test
    public void whenCallSaveThenReturnException() throws SQLException {
        //when
        when(mockDataSource.getConnection()).thenThrow(new SQLException());
        //then
        assertThrows(DaoException.class,()-> typeRoomDAO.save(typeRoom));
    }

    @Test
    public void whenCallUpdateThenReturnTypeRoom() throws SQLException, DaoException {
        //when
        when(mockConnection.prepareStatement(Query.UPDATE_TYPE_BY_ID.get())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        //then
        assertNotNull(typeRoomDAO.update(typeRoom));
    }

    @Test
    public void whenCallUpdateThenReturnNothingUpdated () throws SQLException {
        //when
        when(mockConnection.prepareStatement(Query.UPDATE_TYPE_BY_ID.get())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        //then
        assertThrows(DaoException.class,()-> typeRoomDAO.update(typeRoom));
    }

    @Test
    public void whenCallUpdateThenReturnException() throws SQLException {
        //when
        when(mockDataSource.getConnection()).thenThrow(new SQLException());
        //then
        assertThrows(DaoException.class,()-> typeRoomDAO.update(typeRoom));
    }

    @Test
    public void whenCallDeleteThenDeleted () throws SQLException {
        //when
        when(mockConnection.prepareStatement(Query.DELETE_TYPE_BY_ID.get())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        //then
        assertDoesNotThrow(()->typeRoomDAO.delete(1L));
    }

    @Test
    public void whenCallDeleteThenNothingDeleted () throws SQLException {
        //when
        when(mockConnection.prepareStatement(Query.DELETE_TYPE_BY_ID.get())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        //then
        assertEquals(typeRoomDAO.delete(1L),0);
    }

    @Test
    public void whenCallDeleteThenReturnException() throws SQLException {
        //when
        when(mockDataSource.getConnection()).thenThrow(new SQLException());
        //then
        assertThrows(DaoException.class,()-> typeRoomDAO.delete(1L));
    }

    @Test
    public void whenCallFindByIdThenReturnTypeRoom() throws SQLException, DaoException {
        //when
        when(mockConnection.prepareStatement(Query.SELECT_TYPE_BY_ID.get())).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true);
        //call test method
        typeRoom = typeRoomDAO.findById(1L);
        //then
        assertEquals(1L, typeRoom.getId());
    }

    @Test
    public void whenCallFindByIdThenReturnNothing() throws SQLException, DaoException {
        //when
        when(mockConnection.prepareStatement(Query.SELECT_TYPE_BY_ID.get())).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(false);
        //call test method
        typeRoom = typeRoomDAO.findById(1L);
        //then
        assertNull(typeRoom);
    }

    @Test
    public void whenCallFindByIdThenReturnException() throws SQLException {
        //when
        when(mockDataSource.getConnection()).thenThrow(new SQLException());
        //then
        assertThrows(DaoException.class,()->typeRoomDAO.findById(1L));
    }

    @Test
    public void whenCallFindAllThenReturnList() throws SQLException, DaoException {
        //when
        when(mockConnection.prepareStatement(Query.SELECT_ALL_TYPES.get())).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        //call test method
        List <TypeRoom> typeRooms = typeRoomDAO.findAll();
        //then
        assertEquals(2,typeRooms.size());
    }

    @Test
    public void whenCallFindAllThenReturnEmptyList() throws SQLException, DaoException {
        //when
        when(mockConnection.prepareStatement(Query.SELECT_ALL_TYPES.get())).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(false);
        //call test method
        List <TypeRoom> typeRooms = typeRoomDAO.findAll();
        //then
        assertEquals(0,typeRooms.size());
    }

    @Test
    public void whenCallFindAllThenReturnException() throws SQLException {
        //when
        when(mockDataSource.getConnection()).thenThrow(new SQLException());
        //then
        assertThrows(DaoException.class,()->typeRoomDAO.findAll());
    }
}
