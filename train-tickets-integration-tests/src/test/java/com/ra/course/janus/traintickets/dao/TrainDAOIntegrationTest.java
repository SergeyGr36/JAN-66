package com.ra.course.janus.traintickets.dao;


import com.ra.course.janus.traintickets.configuration.DataSourceFactory;
import com.ra.course.janus.traintickets.entity.Train;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import com.sun.source.tree.AssertTree;
import org.h2.bnf.context.DbSchema;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.FileReader;
import java.sql.Connection;

public class TrainDAOIntegrationTest {
    private static final DataSource dataSource =
            DataSourceFactory.HIKARY_H2_IN_MEMORY.getDataSource();

    private static final String SQL_SCRIPT_FILE_NAME = "src/test/resources/sql_scripts/Create_Trains_Table.sql";

    private static final TrainDAO trainDAO = new TrainDAO(dataSource);

    private static Train testIntegrTrain =
            new Train(1L,"Test Name Train",100,90);

    @BeforeEach
    public void setUp() throws Exception{
        createTrainsTable();
    }

    @Test
    public void whenWeSaveTrainInTable(){
        Train train = trainDAO.save(testIntegrTrain);

        assertEquals(train,testIntegrTrain);



    }

    private static void createTrainsTable() throws Exception{
        try(Connection connection = dataSource.getConnection()){
            try (FileReader reader = new FileReader(SQL_SCRIPT_FILE_NAME)) {
                RunScript.execute(connection, reader);
            }
        }
    }

}
