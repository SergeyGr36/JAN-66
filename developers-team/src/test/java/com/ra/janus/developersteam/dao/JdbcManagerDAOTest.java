package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.dao.interfaces.ManagerDAO;
import com.ra.janus.developersteam.entity.Manager;
import com.ra.janus.developersteam.exception.DAOException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JdbcManagerDAOTest {

    private static final String SELECT_ALL_SQL = "SELECT * FROM MANAGERS";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM MANAGERS WHERE id = ?";
    private static final String SELECT_BY_MANE_SQL = "SELECT * FROM MANAGERS WHERE NAME = ?";
    private static final String INSERT_SQL = "INSERT INTO MANAGERS (name, email, phone) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE MANAGERS SET name=?,email=?,phone=? WHERE id=?";
    private static final String DELETE_SQL = "DELETE FROM MANAGERS WHERE id=?";

    private JdbcManagerDAO managerDAO;
    private DataSource dataSource;
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    @BeforeEach
    public void before() throws Exception {

        dataSource = Mockito.mock(DataSource.class);
        managerDAO = new JdbcManagerDAO(dataSource);

        conn = Mockito.mock(Connection.class);
        Mockito.when(dataSource.getConnection()).thenReturn(conn);

        ps = Mockito.mock(PreparedStatement.class);
        rs = Mockito.mock(ResultSet.class);
        Mockito.when(ps.executeQuery()).thenReturn(rs);
    }

    @Test
    public void whenCallFindAllManagersThenReturnNotEmptyList() throws Exception {

        Mockito.when(conn.prepareStatement(SELECT_ALL_SQL)).thenReturn(ps);
        Mockito.when(rs.next()).thenReturn(true).thenReturn(false);

        List<Manager> managers = managerDAO.findAll();
        assertFalse(managers.isEmpty());
    }

    @Test
    public void whenCallFindAllManagersThenReturnEmptyList() throws Exception {

        Mockito.when(conn.prepareStatement(SELECT_ALL_SQL)).thenReturn(ps);
        Mockito.when(rs.next()).thenReturn(false);

        List<Manager> managers = managerDAO.findAll();
        assertTrue(managers.isEmpty());
    }

    @Test
    public void whenCallFindAllManagersThenThrowConnException() throws Exception {

        Mockito.when(dataSource.getConnection()).thenThrow(new SQLException());

        final Executable executable = () -> managerDAO.findAll();
        assertThrows(DAOException.class, executable);
    }

    @Test
    public void whenCallFindAllManagersThenThrowPSException() throws Exception {

        Mockito.when(conn.prepareStatement(SELECT_ALL_SQL)).thenThrow(new SQLException());

        final Executable executable = () -> managerDAO.findAll();
        assertThrows(DAOException.class, executable);
    }

    @Test
    public void whenCallFindAllManagersThenThrowRSException() throws Exception {

        Mockito.when(conn.prepareStatement(SELECT_ALL_SQL)).thenReturn(ps);
        Mockito.when(ps.executeQuery()).thenThrow(new SQLException());

        final Executable executable = () -> managerDAO.findAll();
        assertThrows(DAOException.class, executable);
    }

    @Test
    public void whenCallFindAllManagersThenThrowRSNextException() throws Exception {

        Mockito.when(conn.prepareStatement(SELECT_ALL_SQL)).thenReturn(ps);
        Mockito.when(rs.next()).thenThrow(new SQLException());

        final Executable executable = () -> managerDAO.findAll();
        assertThrows(DAOException.class, executable);
    }

    @Test
    public void whenCallFindManagerByNameThenReturnEntity() throws Exception {

        String name = "test";
        Mockito.when(conn.prepareStatement(SELECT_BY_MANE_SQL)).thenReturn(ps);
        Mockito.when(rs.next()).thenReturn(true).thenReturn(false);
        Mockito.when(rs.getString("name")).thenReturn(name);

        Manager manager = managerDAO.findByName(name);

        assertEquals(name, manager.getName());

    }

    @Test
    public void whenCallFindManagerByNameThenReturnNull() throws Exception {

        String name = "test";
        Manager expectedManager = null;
        Mockito.when(conn.prepareStatement(SELECT_BY_MANE_SQL)).thenReturn(ps);
        Mockito.when(rs.next()).thenReturn(false);

        Manager manager = managerDAO.findByName(name);

        assertEquals(expectedManager, manager);

    }

    @Test
    public void whenCallFindManagerByNameThenThrowConnException() throws Exception {

        String name = "test";
        Mockito.when(dataSource.getConnection()).thenThrow(new SQLException());

        final Executable executable = () -> managerDAO.findByName(name);
        assertThrows(DAOException.class, executable);

    }

    @Test
    public void whenCallFindManagerByNameThenThrowPSException() throws Exception {

        String name = "test";
        Mockito.when(conn.prepareStatement(SELECT_BY_MANE_SQL)).thenThrow(new SQLException());

        final Executable executable = () -> managerDAO.findByName(name);
        assertThrows(DAOException.class, executable);

    }

    @Test
    public void whenCallFindManagerByNameThenThrowRSException() throws Exception {

        String name = "test";
        Mockito.when(conn.prepareStatement(SELECT_BY_MANE_SQL)).thenReturn(ps);
        Mockito.when(ps.executeQuery()).thenThrow(new SQLException());

        final Executable executable = () -> managerDAO.findByName(name);
        assertThrows(DAOException.class, executable);

    }

    @Test
    public void whenCallFindManagerByNameThenThrowRSNextException() throws Exception {

        String name = "test";
        Mockito.when(conn.prepareStatement(SELECT_BY_MANE_SQL)).thenReturn(ps);
        Mockito.when(rs.next()).thenThrow(new SQLException());

        final Executable executable = () -> managerDAO.findByName(name);
        assertThrows(DAOException.class, executable);

    }

    @Test
    public void whenCallFindManagerByIdThenReturnEntity() throws Exception {

        Long id = 1L;
        Mockito.when(conn.prepareStatement(SELECT_BY_ID_SQL)).thenReturn(ps);
        Mockito.when(rs.next()).thenReturn(true).thenReturn(false);
        Mockito.when(rs.getLong("id")).thenReturn(id);

        Manager manager = managerDAO.findById(id);

        assertEquals(id, manager.getId());

    }

    @Test
    public void whenCallFindManagerByIdThenReturnNull() throws Exception {

        Long id = 1L;
        Manager expectedManager = null;
        Mockito.when(conn.prepareStatement(SELECT_BY_ID_SQL)).thenReturn(ps);
        Mockito.when(rs.next()).thenReturn(false);

        Manager manager = managerDAO.findById(id);

        assertEquals(expectedManager, manager);

    }

    @Test
    public void whenCallFindManagerByIdThenThrowConnException() throws Exception {

        Long id = 1L;
        Mockito.when(dataSource.getConnection()).thenThrow(new SQLException());

        final Executable executable = () -> managerDAO.findById(id);
        assertThrows(DAOException.class, executable);

    }

    @Test
    public void whenCallFindManagerByIdThenThrowPSException() throws Exception {

        Long id = 1L;
        Mockito.when(conn.prepareStatement(SELECT_BY_ID_SQL)).thenThrow(new SQLException());

        final Executable executable = () -> managerDAO.findById(id);
        assertThrows(DAOException.class, executable);

    }

    @Test
    public void whenCallFindManagerByIdThenThrowRSException() throws Exception {

        Long id = 1L;
        Mockito.when(conn.prepareStatement(SELECT_BY_ID_SQL)).thenReturn(ps);
        Mockito.when(ps.executeQuery()).thenThrow(new SQLException());

        final Executable executable = () -> managerDAO.findById(id);
        assertThrows(DAOException.class, executable);

    }

    @Test
    public void whenCallFindManagerByIdThenThrowRSNextException() throws Exception {

        Long id = 1L;
        Mockito.when(conn.prepareStatement(SELECT_BY_ID_SQL)).thenReturn(ps);
        Mockito.when(rs.next()).thenThrow(new SQLException());

        final Executable executable = () -> managerDAO.findById(id);
        assertThrows(DAOException.class, executable);

    }

    @Test
    void whenCreateManagerShouldReturnManager() throws Exception {

        Long id = 1L;
        int index = 1;
        Manager testManager = new Manager(id);
        Mockito.when(conn.prepareStatement(INSERT_SQL, ps.RETURN_GENERATED_KEYS)).thenReturn(ps);
        Mockito.when(ps.getGeneratedKeys()).thenReturn(rs);
        Mockito.when(rs.next()).thenReturn(true);
        Mockito.when(rs.getLong(index)).thenReturn(id);

        Manager manager = managerDAO.create(testManager);

        assertEquals(testManager, manager);
    }

    @Test
    void whenCreateManagerIdWasNotGeneratedThrowException() throws Exception {
        Long id = 1L;
        Manager testManager = new Manager(id);
        Mockito.when(conn.prepareStatement(INSERT_SQL, ps.RETURN_GENERATED_KEYS)).thenReturn(ps);
        Mockito.when(ps.getGeneratedKeys()).thenReturn(rs);
        Mockito.when(rs.next()).thenReturn(false);

        final Executable executable = () -> managerDAO.create(testManager);

        assertThrows(DAOException.class, executable);
    }

    @Test
    void updade() {
    }

    @Test
    void delete() {
    }
}
