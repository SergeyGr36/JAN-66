package com.ra.janus.developersteam.schema;

import com.ra.janus.developersteam.utils.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.Properties;
import java.util.stream.Stream;

public class DBSchemaCreator {
    public static final String EXCEPTION_WARN = "An exception occurred!";
    private static final String DEFAULT_DIRS = "sql_schema_scripts";
    private static final Logger LOGGER = LoggerFactory.getLogger(DBSchemaCreator.class);
    private String scriptsDirectories;

    public DBSchemaCreator() {
        Properties properties;
        try {
            properties = PropertyReader.INSTANCE.getProperties();
        } catch (IOException e) {
            LOGGER.error(EXCEPTION_WARN, e);
            throw new IllegalStateException("Could not read the application properties file.");
        }

        scriptsDirectories = properties.getProperty("db.sql_schema_scripts_directories");
        if (scriptsDirectories == null) {
            scriptsDirectories = DEFAULT_DIRS;
        }
    }

    public void createSchema(Connection connection) {
        String[] dirNames = scriptsDirectories.split(";");
        for (String dirName : dirNames) {
            Path path;
            try {
                path = Paths.get(ClassLoader.getSystemResource(dirName).toURI());
            } catch (URISyntaxException e) {
                LOGGER.error(EXCEPTION_WARN, e);
                throw new IllegalStateException("A syntax URI rule violation occurred");
            }

            if (!Files.exists(path)) {
                IllegalStateException e = new IllegalStateException("The path doesn't exists " + path);
                LOGGER.error(EXCEPTION_WARN, e);
                throw e;
            }

            if (!Files.isDirectory(path)) {
                IllegalStateException e = new IllegalStateException("The path is not a directory " + path);
                LOGGER.error(EXCEPTION_WARN, e);
                throw e;
            }

            processDirectory(path);
        }
    }

    private void processDirectory(Path directoryPath) {
        //try (Stream<Path> entries = Files.find(directoryPath)){

        //}
    }
}
