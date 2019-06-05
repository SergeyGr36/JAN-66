package com.ra.course.janus.traintickets.dao;

import com.ra.course.janus.traintickets.entity.Admin;
import com.ra.course.janus.traintickets.exception.DAOException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminJdbcDaoMockTest {
    private static final String SAVE_SQL = "INSERT INTO ADMIN (NAME, LASTNAME, PASSWORD) VALUES(?,?,?)";
    private static final String UPDATE_SQL = "UPDATE ADMIN SET NAME=?,LASTNAME=?,PASSWORD=? WHERE ID=?";
    private static final String DELETE_SQL = "DELETE FROM ADMIN WHERE ID=? ";
    private static final String SELECT_ALL = "SELECT * FROM ADMIN";
    private static final String SELECT_BY_ID = "SELECT * FROM ADMIN WHERE ID=?";

    private static final long ADMIN_ID = 2L;
    private static final String ADMIN_NAME = "Roman";
    private static final String ADMIN_LASTNAME = "Hreits";
    private static final String PASSWORD = "romanHreits";

    private static final Admin ADMIN_TEST = new Admin(0, ADMIN_NAME, ADMIN_LASTNAME, PASSWORD);
    private static final Long ID_TEST = 5L;

    private static final int COLUM_ID = 1;
    private static final int COLUM_NAME = 2;
    private static final int COLUM_L_NAME = 3;
    private static final int COLUM_PASSWORD = 4;

    private DataSource mockDataSource = mock(DataSource.class);
    private AdminJdbcDao adminDao;
    private Connection mockConnection = mock(Connection.class);
    private PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
    private ResultSet mockResultSet = mock(ResultSet.class);


    @BeforeEach
    void before() throws SQLException {
        adminDao = new AdminJdbcDao(mockDataSource);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
    }

    @Test
    void whenCallSaveThenReturnNewObjectWithAnotherId() throws SQLException {

        when(mockConnection.prepareStatement(SAVE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(COLUM_ID)).thenReturn(ID_TEST);
        assertNotSame(adminDao.save(ADMIN_TEST), ADMIN_TEST);
    }
    @Test
    void whenInMethodSaveResultSetHaveNoGeneratedKeysThrowExceptions() throws SQLException {

        when(mockConnection.prepareStatement(SAVE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        assertThrows(DAOException.class, ()->adminDao.save(ADMIN_TEST));
    }

    @Test
    void whenInMethodSaveMethodsCloseThrowExceptions() throws SQLException {

        when(mockConnection.prepareStatement(SAVE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        exeptions(mockConnection, mockPreparedStatement, mockResultSet);
        assertThrows(DAOException.class, ()->adminDao.save(ADMIN_TEST));
    }
    @Test
    void whenCallMethodUpdateThanReturnTrue() throws SQLException {

        when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        assertTrue(adminDao.update(ID_TEST, ADMIN_TEST));
    }

    @Test
    void whenCallMethodUpdateThanReturnFalse() throws SQLException {

        when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        assertFalse(adminDao.update(ID_TEST, ADMIN_TEST));
    }

    @Test
    void whenCallUpdateInDbConnectionPrepareStatementAndConnectionCloseThrowException() throws SQLException {
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenThrow(new SQLException());
        doThrow(new SQLException()).when(mockConnection).close();
        assertThrows(DAOException.class, ()->adminDao.update(ID_TEST, ADMIN_TEST));
    }

    @Test
    void whenCallDeleteObjectFromDbReturnTrue() throws SQLException {

        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        assertTrue(adminDao.delete(ID_TEST));
    }

    @Test
    void whenCallDeleteObjectFromDbReturnFalse() throws SQLException {

        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        assertFalse(adminDao.delete(ID_TEST));
    }

    @Test
    void whenCallDeleteObjectFromDbConnectionPrepareStatementAndConnectionCloseThrowException() throws SQLException {

        when(mockConnection.prepareStatement(DELETE_SQL)).thenThrow(new SQLException());
        doThrow(new SQLException()).when(mockConnection).close();
        assertThrows(DAOException.class, ()-> adminDao.delete(ID_TEST));
    }

    @Test
    void whenCallMethodFindByIdThanReturnObject() throws SQLException {

        when(mockConnection.prepareStatement(SELECT_BY_ID)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        getingDataFromResultSet(mockResultSet);
        assertEquals(ADMIN_ID, adminDao.findById(ADMIN_ID).getId());
    }
    @Test
    void whenCallMethodFindByIdThanThrowExceptionsWhenResultSetNextFalse() throws SQLException {

        when(mockConnection.prepareStatement(SELECT_BY_ID)).thenReturn(mockPreparedStatement);
        mockPreparedStatement.setLong(1, ID_TEST);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        assertThrows(DAOException.class, ()->adminDao.findById(ID_TEST));
    }

    @Test
    void whenCallMethodFindByIdThanThrowExceptionsThrowException() throws SQLException {

        when(mockConnection.prepareStatement(SELECT_BY_ID)).thenReturn(mockPreparedStatement);
        mockPreparedStatement.setLong(1, ID_TEST);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        exeptions(mockConnection, mockPreparedStatement, mockResultSet);
        assertThrows(DAOException.class, ()-> adminDao.findById(ID_TEST));
    }

    @Test
    void whenCallMethodFindAllInDbThenReturnNonEmptyList() throws SQLException {

        when(mockConnection.prepareStatement(SELECT_ALL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        getingDataFromResultSet(mockResultSet);
        assertFalse(adminDao.findAll().isEmpty());
    }

    @Test
    void whenFindAllInDbThenReturnEmptyList() throws SQLException {
        when(mockConnection.prepareStatement(SELECT_ALL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        assertEquals(Collections.emptyList(), adminDao.findAll());
    }
    @Test
    void whenCallMethodFindAllInDbThenThrowExceptions() throws SQLException {

        when(mockConnection.prepareStatement(SELECT_ALL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        getingDataFromResultSet(mockResultSet);
        exeptions(mockConnection, mockPreparedStatement, mockResultSet);
        assertThrows(DAOException.class, ()-> adminDao.findAll());
    }

    private void exeptions(Connection conn, PreparedStatement pr, ResultSet rs) throws SQLException {

        doThrow(new SQLException()).when(mockConnection).close();
        doThrow(new SQLException()).when(mockResultSet).close();
        doThrow(new SQLException()).when(mockPreparedStatement).close();
    }
    private void getingDataFromResultSet(ResultSet resultSet) throws SQLException {
        when(mockResultSet.getLong(COLUM_ID)).thenReturn(ADMIN_ID);
        when(mockResultSet.getString(COLUM_NAME)).thenReturn(ADMIN_NAME);
        when(mockResultSet.getString(COLUM_L_NAME)).thenReturn(ADMIN_LASTNAME);
        when(mockResultSet.getString(COLUM_PASSWORD)).thenReturn(PASSWORD);
    }
}