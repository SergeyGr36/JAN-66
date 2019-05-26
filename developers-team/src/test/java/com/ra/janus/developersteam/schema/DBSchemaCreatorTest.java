package com.ra.janus.developersteam.schema;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;
import java.sql.Connection;

class DBSchemaCreatorTest {
    private DataSource mockDataSource;
    private Connection mockConnection;

    @BeforeEach
    public void before() throws Exception {
        mockDataSource = Mockito.mock(DataSource.class);
        mockConnection = Mockito.mock(Connection.class);
        Mockito.when(mockDataSource.getConnection()).thenReturn(mockConnection);
    }
    @Test
    void createSchema() {
        //given
        int notExpected = 0;

        //when
        int processed = DBSchemaCreator.INSTANCE.createSchema(mockConnection);

        //then
        assertNotEquals(notExpected, processed);
    }
}