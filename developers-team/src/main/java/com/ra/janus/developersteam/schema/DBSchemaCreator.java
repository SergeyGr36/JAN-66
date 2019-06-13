package com.ra.janus.developersteam.schema;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class DBSchemaCreator {

    private static final String SQL_SCRIPT_PATH = "sql_schema_scripts";

    private DBSchemaCreator() {
    }

    public static int createSchema(final DataSource dataSource) {
        return createSchema(dataSource, "");
    }

    public static int createSchema(final DataSource dataSource, final String... scripts) {
            Arrays.asList(scripts).forEach(s -> {
                try {
                    final Connection connection = dataSource.getConnection();
                    processScript(connection, readAllScripts(s).collect(Collectors.joining()));
                } catch (IOException | URISyntaxException | SQLException e) {
                    throw new IllegalStateException("Could not read the application properties file.", e);
                }
            });
            return 1;
    }

    private static Stream<String> readAllScripts(final String script) throws IOException, URISyntaxException {
        return Files.walk(Paths.get(ClassLoader.getSystemResource(SQL_SCRIPT_PATH).toURI()))
                .filter(path -> path.toString().endsWith(script + ".sql"))
                .flatMap((Function<Path, Stream<String>>) path -> {
                    try {
                        return Files.lines(path);
                    } catch (IOException e) {
                        throw new IllegalArgumentException(e);
                    }
                });
    }


    private static void processScript(final Connection connection, final String script) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(script);
        }
    }
}
