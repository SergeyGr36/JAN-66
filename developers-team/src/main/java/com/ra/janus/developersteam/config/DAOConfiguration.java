package com.ra.janus.developersteam.config;

import com.ra.janus.developersteam.datasources.DataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.ra.janus.developersteam.dao")
public class DAOConfiguration {
    @Bean
    public DataSource dataSource() {
        return DataSourceFactory.get();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(final DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
