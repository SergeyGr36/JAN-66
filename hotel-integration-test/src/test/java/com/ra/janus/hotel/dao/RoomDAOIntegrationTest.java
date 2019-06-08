package com.ra.janus.hotel.dao;

import com.ra.janus.hotel.configuration.ConnectionUtils;
import com.ra.janus.hotel.entity.Room;
import com.ra.janus.hotel.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RoomDAOIntegrationTest {

    private static DataSource dataSource;
    private static RoomDAO roomDao;

    @BeforeEach
    public void init() throws SQLException {
        dataSource = ConnectionUtils.getDefaultDataSource();
        roomDao = new RoomDAO(dataSource);
        Connection c = dataSource.getConnection();
        Statement statement = c.createStatement();
        statement.execute("TRUNCATE TABLE rooms");
    }

    @Test
    public void updateRoomInDatabase() throws DaoException {
    Room room=new Room(1, "6", 12, "NIce room");
    roomDao.save(room);
    Room room1= new Room (room.getId(), "10", 10, "bad room");
    roomDao.update(room1);
    assertEquals(room1.getNum(), roomDao.findById(room.getId()).getNum() );
    }

    @Test
    public void addRoomToDatabase() throws DaoException {
        Room room = new Room(1,"404",12,"nice room");

        roomDao.save(room);
        Room receivedRoom = roomDao.findById(room.getId());
        assertEquals(room.getNum(), receivedRoom.getNum());

    }

    @Test
    public void removeRoomFromDatabase() throws DaoException {
        Room room = new Room(1,"404",12,"nice room");

        roomDao.save(room);
        long i = roomDao.delete(room.getId());
        assertEquals(i, 1);
    }


    @Test
    public void testFindByIdInDB() throws DaoException {
        Room room1 = new Room(1,"404",12,"nice room");
        Room room2 = new Room(2,"502",7,"comfortable room");

        roomDao.save(room1);
        roomDao.save(room2);
        Room receivedRoom = roomDao.findById(1);
        assertEquals(room1.getDescription(), receivedRoom.getDescription());
    }

    @Test
    public void findAllRoomsInDatabase() throws DaoException {
        Room room1 = new Room(1,"404",12,"nice room");
        Room room2 = new Room(2,"502",7,"comfortable room");
        List<Room> rooms = new ArrayList<>();
        rooms.add(room1);
        rooms.add(room2);

        roomDao.save(room1);
        roomDao.save(room2);
        List<Room> receivedRooms = roomDao.findAll();
        assertEquals(rooms.size(), receivedRooms.size());
    }

    @Test
    public void addRoomInDataBaseWithException() {
        Room room = new Room(1,null,12,"nice room");
        assertThrows(DaoException.class, () -> roomDao.save(room));
    }

}
