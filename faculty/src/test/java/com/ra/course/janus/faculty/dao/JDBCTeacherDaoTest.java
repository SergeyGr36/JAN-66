package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.entity.Teacher;
import com.ra.course.janus.faculty.exception.DaoException;
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

class JDBCTeacherDaoTest {
    private static final String INSERT_TEACHER = "INSERT INTO TEACHER (ID, NAME, COURSE) VALUES (?, ?, ?)";
    private static final String UPDATE_TEACHER = "UPDATE TEACHER SET NAME = ?, COURSE = ? WHERE ID = ?";
    private static final String SELECT_TEACHER = "SELECT * FROM TEACHER";
    private static final String SELECT_BY_ID = "SELECT * FROM TEACHER WHERE ID = ?";
    private static final String DELETE_TEACHER = "DELETE FROM TEACHER WHERE ID = ?";
    private static final long ID = 0;
    private static final Teacher TEST_TEACHER = new Teacher(ID, "Roma", "JavaEE");

    private JDBCTeacherDao mockTeacherDao;
    private DataSource mockDataSource = mock(DataSource.class);
    private Connection mockConnection = mock(Connection.class);
    private PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
    private ResultSet mockResultSet = mock(ResultSet.class);

    @BeforeEach
    public void before() throws SQLException {
        mockTeacherDao = new JDBCTeacherDao(mockDataSource);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockResultSet.next()).thenReturn(false);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    public void whenCalledInsertShouldReturnTeacher() throws SQLException {
        when(mockConnection.prepareStatement(INSERT_TEACHER)).thenReturn(mockPreparedStatement);
        Teacher mockTeacher = mockTeacherDao.insert(TEST_TEACHER);
        assertEquals(TEST_TEACHER, mockTeacher);
    }

    @Test
    public void whenCalledInsertAndThenThrowsException() throws SQLException {
        when(mockConnection.prepareStatement(INSERT_TEACHER)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException());
        Executable executable = () -> mockTeacherDao.insert(TEST_TEACHER);
        assertThrows(DaoException.class, executable);
    }

    @Test
    public void whenCalledUpdateThenReturnTrue() throws SQLException {
        int rows = 1;
        when(mockConnection.prepareStatement(UPDATE_TEACHER)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(rows);
        boolean isUpdated = mockTeacherDao.update(TEST_TEACHER);
        assertTrue(isUpdated);
    }

    @Test
    public void whenCalledUpdateThenReturnFalse() throws SQLException {
        int rows = 0;
        when(mockConnection.prepareStatement(UPDATE_TEACHER)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(rows);
        boolean isUpdated = mockTeacherDao.update(TEST_TEACHER);
        assertFalse(isUpdated);
    }

    @Test
    public void whenCalledUpdateThenThrowException() throws SQLException {
        when(mockConnection.prepareStatement(UPDATE_TEACHER)).thenThrow(new SQLException());
        Executable executable = () -> mockTeacherDao.update(TEST_TEACHER);
        assertThrows(DaoException.class, executable);
    }

    @Test
    public void whenCalledSelectThenReturnNotEmptyList() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_TEACHER)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        List<Teacher> list = mockTeacherDao.select();
        assertFalse(list.isEmpty());
    }

    @Test
    public void whenCalledSelectThenThrowsException() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_TEACHER)).thenThrow(new SQLException());
        Executable executable = () -> mockTeacherDao.select();
        assertThrows(DaoException.class, executable);
    }

    @Test
    public void whenCalledSelectByIdThenReturnIt() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_BY_ID)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getLong("id")).thenReturn(ID);
        Teacher teacher = mockTeacherDao.selectById(ID);
        assertEquals(ID, teacher.getId());
    }

    @Test
    public void whenCalledSelectByIdThenReturnNull() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_BY_ID)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(false);
        Teacher teacher = mockTeacherDao.selectById(ID);
        assertNull(teacher);
    }

    @Test
    public void whenCalledSelectByIdThenThrowException() throws Exception {
        when(mockConnection.prepareStatement(SELECT_BY_ID)).thenThrow(new SQLException());
        final Executable executable = () -> mockTeacherDao.selectById(ID);
        assertThrows(DaoException.class, executable);
    }

    @Test
    public void whenCalledDeleteThenReturnTrue() throws SQLException {
        int rows = 1;
        when(mockConnection.prepareStatement(DELETE_TEACHER)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(rows);
        boolean isDeleted = mockTeacherDao.delete(1);
        assertTrue(isDeleted);
    }

    @Test
    public void whenCalledDeleteThenReturnFalse() throws SQLException {
        int rows = 0;
        when(mockConnection.prepareStatement(DELETE_TEACHER)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(rows);
        boolean isDeleted = mockTeacherDao.delete(1);
        assertFalse(isDeleted);
    }

    @Test
    public void whenCalledDeleteThenThrowException() throws SQLException {
        when(mockConnection.prepareStatement(DELETE_TEACHER)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException());
        Executable executable = () -> mockTeacherDao.delete(1);
        assertThrows(DaoException.class, executable);
    }

    @Test
    public void whenCalledToTeacherThenReturnNewTeacherObject() throws SQLException {
        when(mockResultSet.getLong("id")).thenReturn(TEST_TEACHER.getId());
        when(mockResultSet.getString("name")).thenReturn(TEST_TEACHER.getName());
        when(mockResultSet.getString("course")).thenReturn(TEST_TEACHER.getCourse());
        Teacher testTeacher = mockTeacherDao.toTeacher(mockResultSet);
        assertEquals(TEST_TEACHER, testTeacher);
    }
}
