package com.ra.janus.developersteam.configuration;

import com.ra.janus.developersteam.schema.DBSchemaCreator;
import com.ra.janus.developersteam.utils.PropertyReaderUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Properties;

@Configuration
@ComponentScan("com.ra.janus.developersteam")
public class AppConfig {

    @Bean
    public JdbcTemplate jdbcTemplate() {
        final HikariConfig config = new HikariConfig();

        final Properties properties = PropertyReaderUtils.getProperties();

        config.setJdbcUrl(properties.getProperty("db.url"));
        config.setUsername(properties.getProperty("db.username"));
        config.setPassword(properties.getProperty("db.password"));
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        final HikariDataSource dataSource = new HikariDataSource(config);
        DBSchemaCreator.createSchema(dataSource);
        return new JdbcTemplate(dataSource);
    }
}
