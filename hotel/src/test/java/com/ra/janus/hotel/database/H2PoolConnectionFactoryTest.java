package com.ra.janus.hotel.database;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class H2PoolConnectionFactoryTest {

    @Test
    void whenCallGetInstanceThenReturnNotNull() throws IOException {
       assertNotNull(H2PoolConnectionFactory.getInstance());
    }

    @Test
    void whenCallGetDataSourceThenReturnNotNull() throws IOException {
        assertNotNull(H2PoolConnectionFactory.getInstance().getDataSource());
    }
}
