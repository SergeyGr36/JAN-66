package com.ra.course.janus.traintickets.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:config.properties")
public class DataSourceConfig {

    @Autowired
    private Environment env;

    @Bean
    public DataSource getDataSource() {
        HikariDataSource ds = new HikariDataSource();

        ds.setJdbcUrl(env.getProperty("db.url"));
        ds.setUsername(env.getProperty("db.user"));
        ds.setPassword(env.getProperty("db.password"));

        DatabasePopulatorUtils.execute(createDatabasePopulator(), ds);
        return ds;
    }

    private DatabasePopulator createDatabasePopulator() {
        final ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScript(new ClassPathResource("create_schema.sql"));
        return databasePopulator;
    }
}
