package com.ra.janus.developersteam.dao;

import com.ra.janus.developersteam.dao.interfaces.ManagerDAO;
import com.ra.janus.developersteam.entity.Manager;
import com.ra.janus.developersteam.exception.DAOException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcManagerDAO implements ManagerDAO {

    private static final String SELECT_ALL_SQL = "SELECT * FROM MANAGERS";
    private static final String SELECT_ONE_SQL = "SELECT * FROM MANAGERS WHERE id = ?";
    private static final String INSERT_SQL = "INSERT INTO MANAGERS (name, address, phone) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE MANAGERS SET name=?,address=?,phone=? WHERE id=?";
    private static final String DELETE_SQL = "DELETE FROM MANAGERS WHERE id=?";

    private DataSource dataSource;

    public JdbcManagerDAO(DataSource dataSource) {

        this.dataSource = dataSource;
    }

    @Override
    public List<Manager> findAll() {

        try (Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
            ResultSet rs = ps.executeQuery()) {

                final List<Manager> managers = new ArrayList<>();
                while (rs.next()) {
                    managers.add(toManager(rs));
                }
                return managers;

        } catch (SQLException ex) {
            throw new DAOException(ex);
        }
    }

    @Override
    public Manager findById(Long id) {
        return null;
    }

    @Override
    public Manager findByName(String name) {
        return null;
    }

    @Override
    public void create(Long id) {

    }

    @Override
    public void updade(Long id) {

    }

    @Override
    public void delete(Long id) {

    }

    private Manager toManager(ResultSet rs) {

        try {

            Manager manager = new Manager();
            manager.setId(rs.getLong("id"));
            manager.setName(rs.getString("name"));
            manager.setEmail(rs.getString("email"));
            return manager;
        } catch (SQLException ex) {
            throw  new DAOException(ex);
        }

    }
}
