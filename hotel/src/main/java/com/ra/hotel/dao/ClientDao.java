package com.ra.hotel.dao;

import com.ra.hotel.entity.Client;
import com.ra.hotel.exceptions.DaoException;

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
            return client;
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Client update(Client client) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(Query.CLIENT_UPDATE_SQL)) {
            fillStatement(ps, client);
            ps.setLong(5, client.getId());
            ps.executeUpdate();
            return findById(client.getId());
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public int delete(Long id) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(Query.CLIENT_DELETE_SQL)) {
            ps.setLong(1, id);
            return ps.executeUpdate();
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Client findById(Long id) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(Query.CLIENT_FIND_BY_ID_SQL)) {
            ps.setLong(1, id);
            Client client;
            try (ResultSet resultSet = ps.executeQuery()) {
                resultSet.next();
                client = parseRs(resultSet);
            }
            return client;
        } catch (Exception e) {
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
        } catch (Exception e) {
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
        return rs.getLong(1);

    }

    private Client parseRs(ResultSet rs) throws SQLException {
        return new Client(rs.getLong("id"), rs.getString("full_name"), rs.getString("phone_number"),
                rs.getNString("email"), rs.getDate("birthday"));
    }
}