package com.ra.course.janus.traintickets.dao.datasources;

import com.ra.course.janus.traintickets.entity.Train;

import javax.sql.DataSource;

public class TrainDAOTest {

    private static final Long TRAIN_ID = 15L;
    private static final String TRAIN_NAME = "test_train";
    private static final DataSource DATA_SOURCE =
            DataSourceFactory.H2_IN_MEMORY.dataSource;

    private Train train;


}
