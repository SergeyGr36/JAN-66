package com.ra.janus.hotel.dao;

import com.ra.janus.hotel.entity.Client;
import com.ra.janus.hotel.exception.DaoException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDao implements GenericDao<Client> {
    private final transient DataSource dataSource;


    public ClientDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Client save(Client client) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(Query.CLIENT_SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            fillStatement(ps, client);
            ps.execute();
            client.setId(generateKeys(ps));
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
        return client;
    }

    @Override
    public Client update(Client client) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(Query.CLIENT_UPDATE_SQL)) {
            fillStatement(ps, client);
            ps.setLong(5, client.getId());
            ps.execute();
            return findById(client.getId());
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public int delete(long id) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(Query.CLIENT_DELETE_SQL)) {
            ps.setLong(1, id);
            int i = 0;
            if (ps.execute() == true) {
                i = 1;
            }
            return i;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Client findById(long id) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(Query.CLIENT_FIND_BY_ID_SQL)) {
            ps.setLong(1, id);

            Client client = null;
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    client = parseRs(resultSet);
                }
            }
            return client;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<Client> findAll() throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(Query.CLIENT_FIND_ALL)) {
            List<Client> clientList = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    clientList.add(parseRs(rs));
                }
            }
            return clientList;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    private void fillStatement(PreparedStatement prepare, Client client) throws SQLException {
        prepare.setString(1, client.getFullName());
        prepare.setString(2, client.getTelephone());
        prepare.setString(3, client.getEmail());
        prepare.setDate(4, client.getBirthday());
    }

    private Long generateKeys(PreparedStatement prepare) throws SQLException {
        ResultSet rs = prepare.getGeneratedKeys();
        rs.next();
        Long id = rs.getLong(1);
        return id;
    }

    private Client parseRs(ResultSet rs) throws SQLException {
        return new Client(rs.getLong("id"), rs.getString("fullName"), rs.getString("telephone"),
                rs.getNString("email"), rs.getDate("birthday"));
    }
}
