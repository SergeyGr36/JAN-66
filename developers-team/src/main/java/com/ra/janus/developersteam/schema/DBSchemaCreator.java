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

    private static final String SQL_SCRIPT_PATH = "sql_schema_scripts";

    public int createSchema(final Connection connection) {
        try {
            final String script = readAllScripts().collect(Collectors.joining());
            processScript(connection, script);
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


    private void processScript(final Connection connection, final String script) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(script);
        }
    }
}
