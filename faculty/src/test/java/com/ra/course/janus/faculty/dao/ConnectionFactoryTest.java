package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.dao.ConnectionFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionFactoryTest {

/*    BeforeEach
    public void resetSingleton() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field connFactory = ConnectionFactory.class.getDeclaredField("connFactory");
        connFactory.setAccessible(true);
        connFactory.set(null, null);
    }*/

    @Test
    public void getConnectionTest() throws SQLException, IOException {
        ConnectionFactory singleton = ConnectionFactory.getInstance();
        assertNotNull(singleton.getConnection());
    }

}