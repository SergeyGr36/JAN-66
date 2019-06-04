package com.ra.course.janus.faculty.sqlExecute;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class RunScript {
    private final static Logger logger = Logger.getLogger(RunScript.class);

    PreparedStatement executeScript(Connection connection, String fileName) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
            return connection.prepareStatement(stringBuilder.toString());
        } catch (IOException | SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }
}
