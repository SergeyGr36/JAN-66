package com.ra.janus.hotel.database;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class H2ConnectionFactoryTest {

    @Test
    void whenCallGetDataSourceThenReturnNotNull() throws IOException {
        assertNotNull(H2ConnectionFactory.getInstance().getDataSource());
    }

    @Test
    void whenCallGetInstanceThenReturnNotNull() throws IOException {
        assertNotNull(H2ConnectionFactory.getInstance());
    }
}
