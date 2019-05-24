package com.ra.course.janus.traintickets.dao;
import com.ra.course.janus.traintickets.datasources.DataSourceFactory;
import com.ra.course.janus.traintickets.entity.Train;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TrainDAOTest {

    private final String INSERT_TRAIN = "INSERT into TRAINS (id, name, quantity_plases, free_plases) values (?, ?, ?, ?)";
    private final String SELECT_TRAIN_ID = "SELECT * FROM TRAINS WHERE id = ?";
    private final String UPDATE_TRAIN = "UPDATE TRAINS SET name = ?, quantity_plases = ?, free_plases = ? WHERE id = ?";
    private final String DELETE_TRAIN = "DELETE * FROM TRAINS WHERE id = ?";
    private final String SELECT_TRAIN_ALL = "SELECT * FROM TRAINS";

    private static final Long TRAIN_ID = 15L;
    private static final String TRAIN_NAME = "test_train";
    private static final int QUANTITY_PLACES = 100;
    private static final int FREE_PLACES = 99;
    private static final DataSource DATA_SOURCE =
            DataSourceFactory.H2_IN_MEMORY.getDataSource();

    private Train train;
    private TrainDAO trainDAO;
    private DataSource mokeDataSourse;

    @BeforeAll
    public static void setupH2Schema() {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            Statement st = connection.createStatement();
            st.execute("create table trains(id long, name varchar(44), quantity_plases int, free_plases int)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setup(){
        mokeDataSourse = mock(DataSource.class);
        trainDAO = new TrainDAO(mokeDataSourse);
        train = new Train();
        train.setId(TRAIN_ID);
        train.setName(TRAIN_NAME);
        train.setQuanyityPlaces(QUANTITY_PLACES);
        train.setFreePlaces(FREE_PLACES);
    }

    @Test
    void save() throws SQLException {
        TrainDAO trainDAO = new TrainDAO(DATA_SOURCE);
        trainDAO.save(train);
        ResultSet rs;
        try(Connection connection = DATA_SOURCE.getConnection()) {
            Statement statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM TRAINS");
        }
    }

    @Test
    void update() throws SQLException {
        TrainDAO trainDAO = new TrainDAO(DATA_SOURCE);
        trainDAO.update(5L,train);
        ResultSet rs;
    }

    @Test
   public void blDbTest() throws Exception{
        var connection = mock(Connection.class);
        when(mokeDataSourse.getConnection()).thenReturn(connection);


    }

}