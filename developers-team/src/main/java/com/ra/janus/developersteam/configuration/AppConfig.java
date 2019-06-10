package com.ra.janus.developersteam.configuration;

import com.ra.janus.developersteam.datasources.DataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.ra.janus.developersteam")
public class AppConfig {

    @Bean
    public DataSource dataSource(final DataSourceFactory dataSourceFactory) {
        return dataSourceFactory.get();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(final DataSourceFactory dataSourceFactory) {
        return new JdbcTemplate(dataSourceFactory.get());
    }
}
