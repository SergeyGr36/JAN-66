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
    private static DataSource dataSource;
    private static TypeRoomDAO typeRoomDAO;

    @BeforeEach
    public void before() throws SQLException {
        dataSource = ConnectionUtils.getDefaultDataSource();
        typeRoomDAO = new TypeRoomDAO(dataSource);
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("TRUNCATE TABLE TYPE_ROOM");

    }

    @Test
    void testMethodSaveTypeRoomInDB() throws DaoException {
        TypeRoom typeRoom = new TypeRoom(1L,2,150,"test","lux");
        typeRoomDAO.save(typeRoom);
        TypeRoom find = typeRoomDAO.findById(1L);
        assertEquals(typeRoom.getId(),find.getId());
        assertEquals(typeRoom.getCountPlaces(),find.getCountPlaces());
        assertEquals(typeRoom.getPrise(),find.getPrise());
        assertEquals(typeRoom.getDescription(),find.getDescription());
        assertEquals(typeRoom.getClassOfRoom(),find.getClassOfRoom());
    }
    @Test
    void testMethodUpdateTypeRoomInDB() throws DaoException {
        TypeRoom typeRoom1 = new TypeRoom(1L,2,150,"test1","lux");
        TypeRoom typeRoom2 = new TypeRoom(1L,2,300,"test2","lux");
        typeRoomDAO.save(typeRoom1);
        typeRoomDAO.update(typeRoom2);
        TypeRoom find = typeRoomDAO.findById(1L);
        assertEquals(typeRoom2.getId(),find.getId());
        assertEquals(typeRoom2.getCountPlaces(),find.getCountPlaces());
        assertEquals(typeRoom2.getPrise(),find.getPrise());
        assertEquals(typeRoom2.getDescription(),find.getDescription());
        assertEquals(typeRoom2.getClassOfRoom(),find.getClassOfRoom());
    }
    @Test
    void testMethodDeleteTypeRoomFromDB() throws DaoException {
        TypeRoom typeRoom = new TypeRoom(1L,2,150,"test","lux");
        typeRoomDAO.save(typeRoom);
        assertDoesNotThrow (() -> typeRoomDAO.delete(1L));
        assertNull(typeRoomDAO.findById(1L));
    }
    @Test
    void testMethodFindByIdTypeRoomInDB() throws DaoException {
        TypeRoom typeRoom = new TypeRoom(1L,2,150,"test","lux");
        typeRoomDAO.save(typeRoom);
        assertNotNull(typeRoomDAO.findById(1L));
    }
    @Test
    void testMethodFindAllTypeRoomInDB() throws DaoException {
        TypeRoom typeRoom1 = new TypeRoom(1L,2,150,"test1","lux");
        TypeRoom typeRoom2 = new TypeRoom(2L,2,150,"test2","lux");
        TypeRoom typeRoom3 = new TypeRoom(3L,2,150,"test3","lux");
        typeRoomDAO.save(typeRoom1);
        typeRoomDAO.save(typeRoom2);
        typeRoomDAO.save(typeRoom3);
        assertEquals(3,typeRoomDAO.findAll().size());
    }
}
