package com.ra.course.janus.traintickets.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;

@Configuration
public class JdbcConfig {

    @Bean
    public JdbcTemplate jdbcTemplate(final DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(final DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public SimpleJdbcInsert userJdbcInsert(final DataSource dataSource) {
        return new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
    }

    @Bean
    public SimpleJdbcInsert trainJdbcInsert(final DataSource dataSource) {
        return new SimpleJdbcInsert(dataSource)
                .withTableName("trains")
                .usingGeneratedKeyColumns("id");
    }

    @Bean
    public SimpleJdbcInsert adminJdbcInsert(final DataSource dataSource) {
        return new SimpleJdbcInsert(dataSource)
                .withTableName("admins")
                .usingGeneratedKeyColumns("id");
    }

    @Bean
    public SimpleJdbcInsert invoiceJdbcInsert(final DataSource dataSource) {
        return new SimpleJdbcInsert(dataSource)
                .withTableName("invoices")
                .usingGeneratedKeyColumns("id");
    }



}
