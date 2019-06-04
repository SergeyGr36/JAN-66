package com.ra.course.janus.faculty.sqlExecute;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RunScriptTest {
    private RunScript runScript;
    private Connection mockConnection;
    private String fileName;

    @BeforeEach
    void before() {
        runScript = mock(RunScript.class);
        mockConnection = mock(Connection.class);
        fileName = "./janus/faculty/src/test/resources/create_teacher_table.sql";
    }

    @Test
    void whenCalledExecuteScriptThenReturnPreparedStatement() {
         PreparedStatement mockPreparedStatement = runScript.executeScript(mockConnection, fileName);
         assertEquals(mockPreparedStatement, runScript.executeScript(mockConnection, fileName));
    }

    @Test
    void whenCalledExecuteScriptThenThrowSqlException() {
        when(runScript.executeScript(mockConnection, fileName)).thenThrow(new SQLException());
        Executable executable = () -> runScript.executeScript(mockConnection, fileName);
        assertThrows(RuntimeException.class, executable);
    }
}
