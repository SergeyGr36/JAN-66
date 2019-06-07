package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.entity.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PlainJDBCTeacherDaoTest {
    private static final String INSERT = "INSERT INTO TEACHER (ID, NAME, COURSE) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE TEACHER SET NAME = ?, COURSE = ? WHERE ID = ?";
    private static final String SELECT = "SELECT * FROM TEACHER";
    private static final String DELETE = "DELETE FROM TEACHER WHERE ID = ?";

    private Teacher teacher;
    private Connection mockConnection;
    private DataSource mockDataSource;
    private PlainJDBCTeacherDao mockTeacherDao;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    void before() throws SQLException {
        teacher = new Teacher(1, "Roma", "JavaEE");
        mockDataSource = mock(DataSource.class);
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        mockTeacherDao = new PlainJDBCTeacherDao(mockDataSource);
    }

    @Test
    void whenCalledInsertShouldReturnTeacher() throws SQLException {
        when(mockConnection.prepareStatement(INSERT)).thenReturn(mockPreparedStatement);
        Teacher mockTeacher = mockTeacherDao.insert(teacher);
        assertEquals(teacher, mockTeacher);
    }

    @Test
    void whenCalledInsertAndThenThrowsException() throws SQLException {
        when(mockConnection.prepareStatement(INSERT)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException());
        Executable executable = () -> mockTeacherDao.insert(teacher);
        assertThrows(RuntimeException.class, executable);
    }

    @Test
    void whenCalledUpdateThenReturnTrue() throws SQLException {
        int rows = 1;
        when(mockConnection.prepareStatement(UPDATE)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(rows);
        boolean isUpdated = mockTeacherDao.update(teacher);
        assertTrue(isUpdated);
    }

    @Test
    void whenCalledUpdateThenReturnFalse() throws SQLException {
        int rows = 0;
        when(mockConnection.prepareStatement(UPDATE)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(rows);
        boolean isUpdated = mockTeacherDao.update(teacher);
        assertFalse(isUpdated);
    }

    @Test
    void whenCalledUpdateThenThrowException() throws SQLException {
        when(mockConnection.prepareStatement(UPDATE)).thenThrow(new SQLException());
        Executable executable = () -> mockTeacherDao.update(teacher);
        assertThrows(RuntimeException.class, executable);
    }

    @Test
    void whenCalledSelectThenReturnNotEmptyList() throws SQLException {
        when(mockConnection.prepareStatement(SELECT)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        List<Teacher> list = mockTeacherDao.select();
        assertFalse(list.isEmpty());
    }

    @Test
    void whenCalledSelectThenThrowsException() throws SQLException {
        when(mockConnection.prepareStatement(SELECT)).thenThrow(new SQLException());
        Executable executable = () -> mockTeacherDao.select();
        assertThrows(RuntimeException.class, executable);
    }

    @Test
    void whenCalledDeleteThenReturnTrue() throws SQLException {
        int rows = 1;
        when(mockConnection.prepareStatement(DELETE)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(rows);
        boolean isDeleted = mockTeacherDao.delete(1);
        assertTrue(isDeleted);
    }

    @Test
    void whenCalledDeleteThenReturnFalse() throws SQLException {
        int rows = 0;
        when(mockConnection.prepareStatement(DELETE)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(rows);
        boolean isDeleted = mockTeacherDao.delete(1);
        assertFalse(isDeleted);
    }

    @Test
    void whenCalledDeleteThenThrowException() throws SQLException {
        when(mockConnection.prepareStatement(DELETE)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException());
        Executable executable = () -> mockTeacherDao.delete(1);
        assertThrows(RuntimeException.class, executable);
    }

    @Test
    void whenCalledToTeacherThenReturnNewTeacherObject() throws SQLException {
        when(mockResultSet.getLong("id")).thenReturn(teacher.getId());
        when(mockResultSet.getString("name")).thenReturn(teacher.getName());
        when(mockResultSet.getString("course")).thenReturn(teacher.getCourse());
        Teacher testTeacher = mockTeacherDao.toTeacher(mockResultSet);
        assertEquals(teacher, testTeacher);
    }
}
