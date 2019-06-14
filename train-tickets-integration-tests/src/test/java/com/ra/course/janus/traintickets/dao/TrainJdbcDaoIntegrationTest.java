package com.ra.course.janus.traintickets.dao;
import com.ra.course.janus.traintickets.MainSpringConfig;
import com.ra.course.janus.traintickets.entity.Train;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MainSpringConfig.class)
public class TrainJdbcDaoIntegrationTest {

    private static final String SQL_SCRIPT_FILE_NAME = "src/test/resources/sql_scripts/create_trains_table.sql";

    private static final Train TEST_TRAIN =
            new Train(1L,"Test Name Train",100,90);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TrainJdbcDao trainJdbcDao;



    @BeforeEach
    public void setUp() throws IOException {
        createTableTrains();
        clearTableTrains();
    }

    @Test
    public void whenWeSaveTrain() throws Exception{

        final Train createdTrain = trainJdbcDao.save(TEST_TRAIN);

        assertNotNull(createdTrain);
    }

    @Test
    public void whenWeUpdateTrain()throws Exception{
        // given
        final Long id = trainJdbcDao.save(TEST_TRAIN).getId();
        final Train newTrain = new Train(id, "new_name", 50, 20);
        // when
        trainJdbcDao.update(newTrain);
        final Train updatedTrain = trainJdbcDao.findById(id);
        // then
        assertEquals(newTrain, updatedTrain);
    }

    @Test
    public void whenWeDeleteTrain()throws Exception{
        // given
        final Long id = trainJdbcDao.save(TEST_TRAIN).getId();
        // when
        trainJdbcDao.delete(id);
        // then
        assertThrows(EmptyResultDataAccessException.class, () -> trainJdbcDao.findById(id));

    }

    @Test
    public void whenWeFindTrainByID()throws Exception{
        // given
        final Train savedTrain = trainJdbcDao.save(TEST_TRAIN);
        // when
        final Train foundTrain = trainJdbcDao.findById(savedTrain.getId());
        // then
        assertEquals(savedTrain, foundTrain);

    }

    @Test
    public void whenWeUseFindAllTrains() throws Exception{
        // given
        final Train savedTrain = trainJdbcDao.save(TEST_TRAIN);
        List<Train> savedTrains = Collections.singletonList(savedTrain);
        // when
        List<Train> foundTrains = trainJdbcDao.findAll();
        // then
        assertEquals(savedTrains, foundTrains);
    }

    ////////////////////////////////////////////////

    private static String readScriptFile() throws IOException {
        return String.join("", Files.readAllLines(Paths.get(SQL_SCRIPT_FILE_NAME)));
    }

    private void executeScript(String script){
        jdbcTemplate.execute(script);
    }

    private void createTableTrains() throws IOException {
        executeScript(readScriptFile());
    }

    private void clearTableTrains(){
        executeScript("TRUNCATE TABLE TRAINS;");
    }
}
