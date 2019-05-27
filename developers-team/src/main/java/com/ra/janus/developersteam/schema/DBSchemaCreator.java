package com.ra.janus.developersteam.schema;

import com.ra.janus.developersteam.utils.FilesContentReader;
import com.ra.janus.developersteam.utils.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

public enum DBSchemaCreator {
    INSTANCE;

    private static final String EXCEPTION_WARN = "An exception occurred!";
    public static final String PROP_KEY = "db.sql_schema_scripts_directories";
    private static final Logger LOGGER = LoggerFactory.getLogger(DBSchemaCreator.class);

    transient private Connection connection;

    public int createSchema(final Connection connection, String... fileNames) {
        Properties properties;
        try {
            properties = PropertyReader.INSTANCE.getProperties();
        } catch (IOException e) {
            throw new IllegalStateException("Could not read the application properties file.", e);
        }

        final String scriptsDirs = properties.getProperty(PROP_KEY);
        this.connection = connection;
        final String[] dirNames = scriptsDirs.split(";");
        int processed = 0;
        for (String  dirName:dirNames) {
            List<String> list = FilesContentReader.INSTANCE.getContent(dirName, fileNames);
            processed += list.size();
            for (String script:list) {
                processScript(script);
            }
        }
        return processed;
    }

    private void processScript(final String script) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(script);
        } catch (SQLException e) {
            LOGGER.error(EXCEPTION_WARN, e);
            throw new IllegalStateException(e);
        }
    }
}
