package com.ra.janus.hotel.dao;

import com.ra.janus.hotel.configuration.ConnectionUtils;
import com.ra.janus.hotel.entity.TypeRoom;
import com.ra.janus.hotel.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class TypeRoomDAOIntegrationTest {
    private static TypeRoomDAO typeRoomDAO;
    private static TypeRoom typeRoom;

    @BeforeEach
    void before() throws SQLException {
        DataSource dataSource = ConnectionUtils.getDefaultDataSource();
        typeRoomDAO = new TypeRoomDAO(dataSource);
        typeRoom = new TypeRoom(1L,2,150,"test1","lux");
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("TRUNCATE TABLE TYPE_ROOM");
    }

    @Test
    void testMethodSaveTypeRoomInDB() throws DaoException {
        typeRoomDAO.save(typeRoom);
        TypeRoom find = typeRoomDAO.findById(typeRoom.getId());
        assertEquals(typeRoom,find);
    }
    @Test
    void testMethodUpdateTypeRoomInDB() throws DaoException {
        typeRoomDAO.save(typeRoom);
        TypeRoom typeRoom2 = new TypeRoom(typeRoom.getId(),2,300,"test2","lux");
        typeRoomDAO.update(typeRoom2);
        TypeRoom find = typeRoomDAO.findById(typeRoom.getId());
        assertEquals(typeRoom2,find);
    }
    @Test
    void testMethodDeleteTypeRoomFromDB() throws DaoException {
        typeRoomDAO.save(typeRoom);
        assertDoesNotThrow (() -> typeRoomDAO.delete(1L));
        assertNull(typeRoomDAO.findById(typeRoom.getId()));
    }
    @Test
    void testMethodFindByIdTypeRoomInDB() throws DaoException {
        typeRoomDAO.save(typeRoom);
        assertNotNull(typeRoomDAO.findById(typeRoom.getId()));
    }
    @Test
    void testMethodFindAllTypeRoomInDB() throws DaoException {
        TypeRoom typeRoom2 = new TypeRoom(2L,2,150,"test2","lux");
        TypeRoom typeRoom3 = new TypeRoom(3L,2,150,"test3","lux");
        typeRoomDAO.save(typeRoom);
        typeRoomDAO.save(typeRoom2);
        typeRoomDAO.save(typeRoom3);
        assertEquals(3,typeRoomDAO.findAll().size());
    }
}
