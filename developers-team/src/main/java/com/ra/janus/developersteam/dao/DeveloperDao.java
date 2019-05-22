package com.ra.janus.developersteam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.ra.janus.developersteam.entity.Developer;
import com.ra.janus.developersteam.exception.DAOException;
import com.ra.janus.developersteam.interfaces.IBaseDAO;

public class DeveloperDao implements IBaseDAO<Developer> {

    private static final String SELECT_ALL_SQL = "SELECT * FROM DEVELOPER";
    private static final String UPDATE_SQL = "UPDATE DEVELOPER SET NAME = ? WHERE ID = ?";
    private static final String DELETE_SQL = "DELETE FROM DEVELOPER WHERE ID = ?";
    
    private final DataSource dataSource;

    public DeveloperDao(DataSource dataSource) {

    	this.dataSource = dataSource;
    }
    
	@Override
	public List<Developer> getAll() {

		try {
			   Connection conn = dataSource.getConnection();
               PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
               ResultSet rs = ps.executeQuery();

               final List<Developer> developers = new ArrayList<>();
               while (rs.next()) {
            	   //developers.add(toWork(rs));
               }

               return developers;
           } catch (SQLException e) {

        	   throw new DAOException(e);
           }
	}

	@Override
	public Developer getById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(Developer entity) {

		try {
        	  Connection conn = dataSource.getConnection();
              PreparedStatement ps = conn.prepareStatement(UPDATE_SQL);
              prepareStatement(ps, entity);

               return ps.executeUpdate() != 0;
           } catch (SQLException e) {

        	   throw new DAOException(e);
           }
        }

	@Override
	public boolean delete(long id) {

		try {
                Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(DELETE_SQL);
                ps.setLong(1, id);

            return ps.executeUpdate() != 0;
        } catch (SQLException e) {

        	throw new DAOException(e);
        }
    }

	@Override
	public boolean create(Developer entity) {
		// TODO Auto-generated method stub
		return false;
	}
}
