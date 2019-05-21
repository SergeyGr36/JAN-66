package com.ra.course.janus.traintickets.dao.api;

import com.ra.course.janus.traintickets.dao.datasources.DataSourceFactory;
import com.ra.course.janus.traintickets.entity.Train;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class TrainDAOTest {

    private static final Long TRAIN_ID = 15L;
    private static final String TRAIN_NAME = "test_train";
    private static final DataSource DATA_SOURCE =
            DataSourceFactory.H2_IN_MEMORY.getDataSource();

    private Train train;

    @BeforeAll
    public static void setupH2Schema() {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            Statement st = connection.createStatement();
            st.execute("create table trains(id long, name varchar(44))");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setup(){
        train = new Train();
        train.setId(TRAIN_ID);
        train.setName(TRAIN_NAME);
    }

    @Test
    void save() {
        TrainDAO trainDAO = new TrainDAO(DATA_SOURCE);
        trainDAO.save(train);
        ResultSet rs;
        try(Connection connection = DATA_SOURCE.getConnection()){
            Statement statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM TRAINS");
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}