package com.ra.janus.developersteam.datasources;

import com.ra.janus.developersteam.utils.PropertyReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class DataSourceFactoryTest {

    String url;
    String user;
    String pass;


    @BeforeEach
    public void before() throws IOException {

        Properties properties = PropertyReader.INSTANCE.getProperties("config.properties");
        url = properties.getProperty("db.url");
        user = properties.getProperty("db.username");
        pass = properties.getProperty("db.password");

    }


    @Test
    public void whenAskH2DataSourceFactoryReturnsThat() {

        DataSource fDataSource = (new DataSourceFactory()).get(DataSourceType.H2);

        DataSource testDataSource = H2DataSource.INSTANCE.get(url, user, pass);

        assertEquals(fDataSource, testDataSource);
    }

    @Test
    public void whenAskHikariDataSourceFactoryReturnsThat() {

        DataSource fDataSource = (new DataSourceFactory()).get(DataSourceType.HIKARI);

        DataSource testDataSource = HikDataSource.INSTANCE.get(url, user, pass);

        assertEquals(fDataSource, testDataSource);
    }
}
