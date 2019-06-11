package com.ra.course.janus.traintickets;

import com.ra.course.janus.traintickets.configuration.DataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.ra.course.janus.traintickets")
public class MainSpringConfig {

    @Bean
    public DataSource dataSource() {
        return DataSourceFactory.DATA_SOURCE.getInstance();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(final DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
