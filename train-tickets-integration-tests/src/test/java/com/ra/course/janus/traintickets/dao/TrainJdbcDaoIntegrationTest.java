package com.ra.course.janus.traintickets.dao;
import com.ra.course.janus.traintickets.MainSpringConfig;
import com.ra.course.janus.traintickets.entity.Train;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MainSpringConfig.class)
@Sql("classpath:sql_scripts/create_trains_table.sql")
public class TrainJdbcDaoIntegrationTest {

    private static final Train TEST_TRAIN =
            new Train(1L,"Test Name Train",100,90);

    @Autowired
    private TrainJdbcDao trainJdbcDao;

    @Test
    public void whenWeSaveTrain(){
        // when
        final Train createdTrain = trainJdbcDao.save(TEST_TRAIN);
        final Train someTrain = trainJdbcDao.save(TEST_TRAIN);
        // then
        assertNotSame(createdTrain.getId(),someTrain.getId());
        assertNotNull(createdTrain);
    }

    @Test
    public void whenWeUpdateTrain(){
        // given
        Train someTrain = trainJdbcDao.save(TEST_TRAIN);
        Train updateTrain = someTrain;
        // when
        updateTrain.setName("update train");
        // then
        assertTrue(trainJdbcDao.update(updateTrain));
    }

    @Test
    public void whenWeDeleteTrain(){
        // given
        final Long id = trainJdbcDao.save(TEST_TRAIN).getId();
        // when
        trainJdbcDao.delete(id);
        // then
        assertThrows(EmptyResultDataAccessException.class, () -> trainJdbcDao.findById(id));
    }

    @Test
    public void whenWeFindTrainByID(){
        // given
        final Train savedTrain = trainJdbcDao.save(TEST_TRAIN);
        // when
        final Train foundTrain = trainJdbcDao.findById(savedTrain.getId());
        // then
        assertEquals(savedTrain, foundTrain);
    }

    @Test
    public void whenWeUseFindAllTrains(){
        // given
        final Train savedTrain1 = trainJdbcDao.save(TEST_TRAIN);
        final Train savedTrain2 = trainJdbcDao.save(TEST_TRAIN);
        List<Train> expectedTrains = Arrays.asList(savedTrain1,savedTrain2);
        // when
        List<Train> actualTrains = trainJdbcDao.findAll();
        // then
        assertEquals(expectedTrains,actualTrains);
    }
}
