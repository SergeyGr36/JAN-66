package com.ra.janus.developersteam;


import com.ra.janus.developersteam.configuration.AppConfig;
import com.ra.janus.developersteam.schema.DBSchemaCreator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SuppressWarnings("PMD")
public class DevTeamMain {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        DataSource dataSource = (DataSource) context.getBean("DataSourceFactory");
        try(Connection connection = dataSource.getConnection()) {

            DBSchemaCreator.createSchema(connection);

        } catch (SQLException ex)
        {
            System.out.println("Some shit happened!");
        }
    }

}
