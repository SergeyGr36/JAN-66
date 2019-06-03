package com.ra.course.janus.traintickets.dao;
import com.ra.course.janus.traintickets.configuration.DataSourceFactory;
import com.ra.course.janus.traintickets.entity.Train;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.FileReader;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TrainDAOIntegrationTest {
    private static final DataSource dataSource =
            DataSourceFactory.HIKARY_H2_IN_MEMORY.getDataSource();

    private static final String SQL_SCRIPT_FILE_NAME = "src/test/resources/sql_scripts/Create_Trains_Table.sql";

    private static final TrainDAO trainDAO = new TrainDAO(dataSource);

    private static final Train testIntegrTrain =
            new Train(1L,"Test Name Train",100,90);

    @BeforeEach
    public void setUp() throws Exception{
        createTrainsTable();
    }

    @Test
    void whenWeSaveTrain() throws Exception{
        Train train = trainDAO.save(testIntegrTrain);
        Train someTrain = trainDAO.save(train);

        assertNotSame(train.getId(),someTrain.getId());
        assertEquals(train.getName(),testIntegrTrain.getName());

    }

    @Test
    void whenWeUpdateTrain()throws Exception{
        Train somethingTrain = trainDAO.save(testIntegrTrain);

        somethingTrain.setName("Test Train");
        somethingTrain.setFreePlaces(50);

        trainDAO.update(testIntegrTrain.getId(),somethingTrain);
        assertNotSame(somethingTrain,testIntegrTrain);
    }

    @Test
    void whenWeDeleteTrain()throws Exception{
        Train someTrain = trainDAO.save(testIntegrTrain);
        final long id = someTrain.getId();
        assertTrue(trainDAO.delete(id));
    }

    @Test
    void whenWeFindTrainByID()throws Exception{
        Train someTrain = trainDAO.save(testIntegrTrain);
        Train findTrain = trainDAO.findById(someTrain.getId());

        assertEquals(someTrain,findTrain);
    }

    @Test
    void whenWeUseFindAllTrains() throws Exception{
        Train someTrain = trainDAO.save(testIntegrTrain);
        List<Train> allTrains = Arrays.asList(someTrain);
        List<Train> findTrains = trainDAO.findAll();

        assertIterableEquals(allTrains,findTrains);
    }

    private static void createTrainsTable() throws Exception{
        try(Connection connection = dataSource.getConnection()){
            try (FileReader reader = new FileReader(SQL_SCRIPT_FILE_NAME)) {
                RunScript.execute(connection, reader);
            }
        }
    }
}
