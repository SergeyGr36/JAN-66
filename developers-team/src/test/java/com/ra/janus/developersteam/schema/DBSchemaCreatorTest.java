package com.ra.janus.developersteam.schema;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

class DBSchemaCreatorTest {
    DBSchemaCreator creator = DBSchemaCreator.INSTANCE;
    DataSource mockDataSource = Mockito.mock(DataSource.class);

    @Test
    void createSchema() {

    }
}