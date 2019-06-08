package com.ra.janus.developersteam.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config.properties")
public class DBProperties {

    @Getter
    @Value("${db.url}")
    public String url;

    @Getter
    @Value("${db.username}")
    public String username;

    @Getter
    @Value("${db.password}")
    public String password;

}
