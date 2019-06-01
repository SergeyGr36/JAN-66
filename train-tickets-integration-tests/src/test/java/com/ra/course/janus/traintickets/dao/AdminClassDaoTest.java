package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.configuration.DataSourceFactory;
import com.ra.course.janus.traintickets.exception.DAOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminClassDaoTest {

    private static final DataSource DATA_SOURCE = DataSourceFactory.HIKARY_H2_IN_MEMORY.getDataSource();
    private static final JdbcAdminDao daoAdmin = new JdbcAdminDao(DATA_SOURCE);

    @BeforeAll
    public void setUp() throws SQLException {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute("CREATE table ADMIN (ID INT PRIMARY KEY AUTO_INCREMENT, NAME VARCHAR (20), " +
                    "LASTNAME VARCHAR (20), PASSWORD VARCHAR (30)) ");
        } catch (SQLException e){
            throw new DAOException(e);
        }
    }

    @Test
    public void testSaveMethod(){

    }
}
