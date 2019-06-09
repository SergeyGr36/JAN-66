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

    private static final Admin ADMIN_TEST = new Admin(ADMIN_ID, ADMIN_NAME, ADMIN_LASTNAME, PASSWORD);
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
    public void before() throws SQLException {
        adminDao = new AdminJdbcDao(mockDataSource);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    // Test method Save---------------------------------------------------------
    @Test
    public void whenCallSaveThenReturnNewObjectWithAnotherId() throws SQLException {
        // when
        when(mockConnection.prepareStatement(SAVE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        // then
        assertNotSame(adminDao.save(ADMIN_TEST), ADMIN_TEST);
    }
    
    @Test
    public void whenInMethodSaveResultSetHaveNoGeneratedKeysThrowExceptions() throws SQLException {
        // when
        when(mockConnection.prepareStatement(SAVE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        // then
        assertThrows(DAOException.class, ()->adminDao.save(ADMIN_TEST));
    }

    @Test
    public void whenInMethodSaveCallMethodsCloseThrowExceptions() throws SQLException {
        // when
        when(mockConnection.prepareStatement(SAVE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        exeptions();
        // then
        assertThrows(DAOException.class, ()->adminDao.save(ADMIN_TEST));
    }

    // Test method update-------------------------------------------------------------
    @Test
    public void whenCallMethodUpdateThanReturnTrue() throws SQLException {
        // when
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        // then
        assertTrue(adminDao.update(ADMIN_TEST));
    }

    @Test
    public void whenCallMethodUpdateThanReturnFalse() throws SQLException {
        // when
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        // then
        assertFalse(adminDao.update(ADMIN_TEST));
    }

    @Test
    public void whenCallUpdateInDbConnectionPrepareStatementAndConnectionCloseThrowException() throws SQLException {
        // when
        when(mockConnection.prepareStatement(UPDATE_SQL)).thenThrow(new SQLException());
        doThrow(new SQLException()).when(mockConnection).close();
        // then
        assertThrows(DAOException.class, ()->adminDao.update(ADMIN_TEST));
    }

    // Test method delete-------------------------------------------------------------------
    @Test
    public void whenCallDeleteObjectFromDbReturnTrue() throws SQLException {
        // when
        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        // then
        assertTrue(adminDao.delete(ID_TEST));
    }

    @Test
    public void whenCallDeleteObjectFromDbReturnFalse() throws SQLException {
        // when
        when(mockConnection.prepareStatement(DELETE_SQL)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        // then
        assertFalse(adminDao.delete(ID_TEST));
    }

    @Test
    public void whenCallDeleteFromDbConnectionPrepareStatementAndConnectionCloseThrowExceptions() throws SQLException {
        // when
        when(mockConnection.prepareStatement(DELETE_SQL)).thenThrow(new SQLException());
        doThrow(new SQLException()).when(mockConnection).close();
        // then
        assertThrows(DAOException.class, ()-> adminDao.delete(ID_TEST));
    }

    // Test method findById-----------------------------------------------------------------------
    @Test
    public void whenCallMethodFindByIdThanReturnNotNullObject() throws SQLException {
        // when
        when(mockConnection.prepareStatement(SELECT_BY_ID)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true);
        getingDataFromResultSet();
        // then
        assertEquals(ADMIN_ID, adminDao.findById(ADMIN_ID).getId());
    }
    @Test
    public void whenCallMethodFindByIdThanThrowExceptionsWhenResultSetNextFalse() throws SQLException {
        // when
        when(mockConnection.prepareStatement(SELECT_BY_ID)).thenReturn(mockPreparedStatement);
        mockPreparedStatement.setLong(1, ID_TEST);
        when(mockResultSet.next()).thenReturn(false);
        // then
        assertThrows(DAOException.class, ()->adminDao.findById(ID_TEST));
    }

    @Test
    public void whenCallMethodFindByIdThanThrowExceptionsOnCallClose() throws SQLException {
        // when
        when(mockConnection.prepareStatement(SELECT_BY_ID)).thenReturn(mockPreparedStatement);
        mockPreparedStatement.setLong(1, ID_TEST);
        when(mockResultSet.next()).thenReturn(true);
        exeptions();
        // then
        assertThrows(DAOException.class, ()-> adminDao.findById(ID_TEST));
    }

    // Test method findAll-------------------------------------------------------------------
    @Test
    public void whenCallMethodFindAllInDbThenReturnNonEmptyList() throws SQLException {
        // when
        when(mockConnection.prepareStatement(SELECT_ALL)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        getingDataFromResultSet();
        // then
        assertFalse(adminDao.findAll().isEmpty());
    }

    @Test
    public void whenCallFindAllInDbThenReturnEmptyList() throws SQLException {
        // when
        when(mockConnection.prepareStatement(SELECT_ALL)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(false);
        // then
        assertEquals(Collections.emptyList(), adminDao.findAll());
    }
    @Test
    public void whenCallMethodFindAllInDbThenThrowExceptionsOnCallClose() throws SQLException {
        // when
        when(mockConnection.prepareStatement(SELECT_ALL)).thenReturn(mockPreparedStatement);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        exeptions();
        // then
        assertThrows(DAOException.class, ()-> adminDao.findAll());
    }

    //--------------------------------------------------------------------------------------
    private void exeptions() throws SQLException {
        doThrow(new SQLException()).when(mockConnection).close();
        doThrow(new SQLException()).when(mockResultSet).close();
        doThrow(new SQLException()).when(mockPreparedStatement).close();
    }
    private void getingDataFromResultSet() throws SQLException {
        when(mockResultSet.getLong(COLUM_ID)).thenReturn(ADMIN_ID);
        when(mockResultSet.getString(COLUM_NAME)).thenReturn(ADMIN_NAME);
        when(mockResultSet.getString(COLUM_L_NAME)).thenReturn(ADMIN_LASTNAME);
        when(mockResultSet.getString(COLUM_PASSWORD)).thenReturn(PASSWORD);
    }
}