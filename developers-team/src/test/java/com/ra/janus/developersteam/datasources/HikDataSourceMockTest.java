package com.ra.janus.developersteam.datasources;

import com.ra.janus.developersteam.utils.PropertyReaderUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HikDataSourceMockTest {

    String url;
    String user;
    String pass;


    @BeforeEach
    public void before() throws IOException {

        Properties properties = PropertyReaderUtils.getProperties("config.properties");
        url = properties.getProperty("db.url");
        user = properties.getProperty("db.username");
        pass = properties.getProperty("db.password");
    }

    @Test
    public void whenAskHikariDataSourceReturnsThat() {

        DataSource dataSource = HikDataSource.INSTANCE.get(url, user, pass);

        assertEquals(true, dataSource instanceof HikariDataSource);
    }

}
