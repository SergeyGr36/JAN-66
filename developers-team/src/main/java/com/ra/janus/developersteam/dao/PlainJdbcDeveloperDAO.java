package com.ra.janus.developersteam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.ra.janus.developersteam.dao.interfaces.DeveloperDAO;
import com.ra.janus.developersteam.entity.Developer;
import com.ra.janus.developersteam.exception.DAOException;

public class PlainJdbcDeveloperDAO implements DeveloperDAO<Developer> {

	private static final String SELECT_ALL_SQL = "SELECT * FROM DEVELOPER";
	private static final String UPDATE_SQL = "UPDATE DEVELOPER SET NAME = ? WHERE ID = ?";
	private static final String DELETE_SQL = "DELETE FROM DEVELOPER WHERE ID = ?";
	private static final String SELECT_ONE_SQL = "SELECT * FROM DEVELOPER WHERE ID = ?";
	private static final String INSERT_SQL = "INSERT INTO DEVELOPER (ID, NAME) VALUES (?, ?)";

	private final DataSource dataSource;

	public PlainJdbcDeveloperDAO(DataSource dataSource) {

		this.dataSource = dataSource;
	}

	@Override
	public List<Developer> getAll() {

		final List<Developer> developers = new ArrayList<>();

		try {
			Connection conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				developers.add(new Developer(rs.getLong("ID"), rs.getString("NAME")));
			}

		} catch (SQLException e) {

			throw new DAOException(e);
		}

		return developers;
	}

	@Override
	public Developer getById(long id) {

		Developer result;

		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(SELECT_ONE_SQL)) {

			ps.setLong(1, id);

			try (ResultSet rs = ps.executeQuery()) {

				result = rs.next() ? new Developer(rs.getLong("ID"), rs.getNString("NAME")) : null;
			}
		} catch (SQLException e) {

			throw new DAOException(e);
		}

		return result;
	}

	@Override
	public boolean update(Developer entity) {

		try {

			Connection conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(UPDATE_SQL);
			ps.setString(1, entity.getDeveloperName());
			ps.setLong(2, entity.getId());

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
	public Developer save(Developer entity) {

		Developer result;

		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

			ps.setLong(1, entity.getId());
			ps.setString(2, entity.getDeveloperName());

			ps.executeUpdate();

			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {

				if (generatedKeys.next()) {

					result = new Developer(generatedKeys.getLong(1), entity.getDeveloperName());
				} else {

					throw new DAOException("Could not create a Developer");
				}
			}
		} catch (SQLException e) {

			throw new DAOException(e);
		}

		return result;
	}
}
