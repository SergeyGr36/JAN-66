package com.ra.janus.developersteam.schema;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum DBSchemaCreator {
    INSTANCE;

    public static final String PROP_KEY = "db.sql_schema_scripts_directories";
    private static final String SQL_SCRIPT_PATH = "sql_schema_scripts";
    transient private Connection connection;

    public int createSchema(final Connection connection, final String... fileNames) {
        try {
            this.connection = connection;
            final String script = readAllScripts().collect(Collectors.joining());
            processScript(script);
            return 1;
        } catch (IOException | URISyntaxException | SQLException e) {
            throw new IllegalStateException("Could not read the application properties file.", e);
        }
    }

    private Stream<String> readAllScripts() throws IOException, URISyntaxException {
        return Files.walk(Paths.get(ClassLoader.getSystemResource(SQL_SCRIPT_PATH).toURI()))
                .filter(path -> path.toString().endsWith(".sql"))
                .flatMap((Function<Path, Stream<String>>) path -> {
                    try {
                        return Files.lines(path);
                    } catch (IOException e) {
                        throw new IllegalArgumentException(e);
                    }
                });
    }


    private void processScript(final String script) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(script);
        }
    }
}
