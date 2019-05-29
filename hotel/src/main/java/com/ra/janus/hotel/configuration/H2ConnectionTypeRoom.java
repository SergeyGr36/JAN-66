package com.ra.janus.hotel.configuration;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.Scanner;

public final class H2ConnectionTypeRoom {
    private static final Properties PROP = new Properties();
    private static final Logger LOGGER = LoggerFactory.getLogger(H2ConnectionTypeRoom.class);

    public static DataSource getDefaultDataSource() {
        final HikariDataSource hikariDataSource;

        try (final InputStream stream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("type_room_connection.properties")){
            if (stream != null){
                PROP.load(stream);
            }else {
                throw new MissingResourceException("type_room_connection.properties not found",H2ConnectionTypeRoom.class.getName(),"");
            }
        }catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(PROP.getProperty("db.url"));
        config.setUsername(PROP.getProperty("db.user"));
        config.setPassword(PROP.getProperty("db.pass"));
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "150");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "1000");
        hikariDataSource = new HikariDataSource(config);
        initDatabase(hikariDataSource);
        return hikariDataSource;
    }
    private static void initDatabase(final HikariDataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.createStatement().execute(new Scanner(Thread.currentThread().getContextClassLoader().getResourceAsStream("create_type_room_db.sql"), "UTF-8").useDelimiter("\\Z").next());
        } catch (SQLException e) {
            LOGGER.info(e.getMessage());
        }
    }
}
