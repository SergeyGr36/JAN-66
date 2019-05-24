package com.ra.janus.developersteam.schema;

import com.ra.janus.developersteam.utils.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.function.BiPredicate;

public class DBSchemaCreator {
    private static final String EXCEPTION_WARN = "An exception occurred!";
    private static final String DEFAULT_DIRS = "sql_schema_scripts";
    private static final Logger LOGGER = LoggerFactory.getLogger(DBSchemaCreator.class);

    transient private String scriptsDirs;
    transient private Connection connection;

    public DBSchemaCreator() {
        Properties properties;
        try {
            properties = PropertyReader.INSTANCE.getProperties();
        } catch (IOException e) {
            LOGGER.error(EXCEPTION_WARN, e);
            throw new IllegalStateException("Could not read the application properties file.", e);
        }

        scriptsDirs = properties.getProperty("db.sql_schema_scripts_directories");
        if (scriptsDirs == null) {
            scriptsDirs = DEFAULT_DIRS;
        }
    }

    public void createSchema(final Connection connection) {
        this.connection = connection;
        final String[] dirNames = scriptsDirs.split(";");
        Arrays.stream(dirNames).map(this::getDirectoryPath).forEach(this::processDirectory);
    }

    private Path getDirectoryPath(final String dirName) {
        try {
            final Path path = Paths.get(ClassLoader.getSystemResource(dirName).toURI());
            validatePath(path);
            return path;
        } catch (URISyntaxException e) {
            LOGGER.error(EXCEPTION_WARN, e);
            throw new IllegalStateException("A syntax URI rule violation occurred", e);
        }
    }

    private void validatePath(final Path path) {
        if (!Files.exists(path)) {
            final IllegalStateException e = new IllegalStateException("The path doesn't exists " + path);
            LOGGER.error(EXCEPTION_WARN, e);
            throw e;
        }

        if (!Files.isDirectory(path)) {
            final IllegalStateException e = new IllegalStateException("The path is not a directory " + path);
            LOGGER.error(EXCEPTION_WARN, e);
            throw e;
        }
    }

    private void processDirectory(final Path path) {
        try {
            Files.find(path, 100,
                    new BiPredicate<Path, BasicFileAttributes>() {
                        @Override
                        public boolean test(final Path filePath, final BasicFileAttributes fileAttributes) {
                            final File file = filePath.toFile();
                            return !file.isDirectory() &&
                                    file.getName().endsWith(".sql");
                        }
                    }).forEach(this::processFile);
        } catch (IOException e) {
            LOGGER.error(EXCEPTION_WARN, e);
            throw new UncheckedIOException(e);
        }
    }

    private void processFile(final Path path) {
        try {
            final List<String> lines = Files.readAllLines(path);
            lines.forEach(this::processLine);
        } catch (IOException e) {
            LOGGER.error(EXCEPTION_WARN, e);
            throw new UncheckedIOException(e);
        }
    }

    private void processLine(final String line) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(line);
        } catch (SQLException e) {
            LOGGER.error(EXCEPTION_WARN, e);
            throw new IllegalStateException(e);
        }
    }
}
