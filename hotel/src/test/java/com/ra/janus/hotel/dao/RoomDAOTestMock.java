package com.ra.janus.hotel.dao;

import com.ra.janus.hotel.entity.Room;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


public class RoomDAOTestMock {

    private static Room mockRoom;
    private static RoomDAO roomDAO;
    private static DataSource mockDataSource;
    private static Connection mockConnection;
    private static PreparedStatement mockStatement;
    private static ResultSet mockResultSet;
    private static Room room;

    @BeforeEach
    public void init() throws SQLException {
        mockDataSource = Mockito.mock(DataSource.class);
        roomDAO = new RoomDAO(mockDataSource);
        mockConnection = Mockito.mock(Connection.class);
        mockStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        mockRoom = Mockito.mock(Room.class);
        room = new Room();
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
    }

    @Test
    public void whenInsertRoomInDatabaseThenSaveIt() throws SQLException, DaoException {
        when(mockConnection.prepareStatement(Query.ADD_ROOM.get(), PreparedStatement.RETURN_GENERATED_KEYS)).thenReturn(mockStatement);
        when(mockStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(1L);
        assertEquals(1, roomDAO.save(room).getId());
    }

    @Test
    public void whenSearchRoomThenReturnRoom() throws SQLException, DaoException {
        when(mockConnection.prepareStatement(Query.SELECT_ROOM.get())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("id")).thenReturn(1L);
        assertEquals(1L, roomDAO.findById(1L).getId());
    }

    @Test
    public void whenDeleteRoomFromDatabaseThenReturnOneIfDone() throws SQLException, DaoException {
        when(mockConnection.prepareStatement(Query.REMOVE_ROOM.get())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);
        assertEquals(1, roomDAO.delete(1L));
    }

    @Test
    public void whenUpdateRoomInDatabaseThenReturnUpdatedRoom() throws SQLException, DaoException {
        when(mockConnection.prepareStatement(Query.UPDATE_ROOM.get())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);
        when(mockRoom.getId()).thenReturn(1L);
        when(mockRoom.getIdTypeRoom()).thenReturn(1L);
        PreparedStatement statement = Mockito.mock(PreparedStatement.class);
        ResultSet rs = Mockito.mock(ResultSet.class);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(Query.SELECT_ROOM.get()))
                .thenReturn(statement);
        when(statement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getLong("id")).thenReturn(1L);
        assertEquals(1L, roomDAO.findById(1L).getId());
        assertEquals(mockRoom.getId(), roomDAO.update(mockRoom).getId());
    }

    @Test
    public void whenFindAllThanReturnRoomList() throws SQLException, DaoException {
        when(mockConnection.prepareStatement(Query.SELECT_ALL_ROOMS.get())).thenReturn(mockStatement);
        List<Room> mockList = Mockito.mock(ArrayList.class);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getLong("id")).thenReturn(1l);
        when(mockList.add(mockRoom)).thenReturn(true);
        assertNotNull(roomDAO.findAll());
    }

    @Test
    public void whenFindAllThenThrowException() throws SQLException {
        when(mockDataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DaoException.class, () -> roomDAO.findAll());

    }

    @Test
    public void whenUpdateRoomInDatabaseThenThrowException() throws SQLException {
        when(mockDataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DaoException.class, () -> roomDAO.update(mockRoom));
    }

    @Test
    public void whenDeleteRoomFromDatabaseThenThrowException() throws SQLException {
        when(mockDataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DaoException.class, () -> roomDAO.delete(1L));
    }

    @Test
    public void whenFindByIdRoomByIdThenThrowException() throws SQLException {
        when(mockDataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DaoException.class, () -> roomDAO.findById(1L));
    }

    @Test
    public void whenSaveRoomInDatabaseThenThrowException() throws SQLException {
        when(mockDataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(DaoException.class, () -> roomDAO.save(mockRoom));

    }
}
