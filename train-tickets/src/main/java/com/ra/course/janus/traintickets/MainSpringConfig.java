package com.ra.course.janus.traintickets;

import com.ra.course.janus.traintickets.configuration.DataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.ra.course.janus.traintickets")
public class MainSpringConfig {

    @Bean
    public DataSource dataSource() {
        return DataSourceFactory.DATA_SOURCE.getInstance();
    }

}
