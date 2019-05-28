package com.ra.hotel.dao;

import com.ra.hotel.configuration.H2ConnectionUtils;
import com.ra.hotel.entity.Client;
import com.ra.hotel.entity.enums.Query;
import com.ra.hotel.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDao implements GenericDao<Client> {

    private final transient DataSource dataSource;
    private static final Logger LOGGER = LoggerFactory.getLogger(H2ConnectionUtils.class);

    public ClientDao(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Client save(final Client client) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(Query.CLIENT_SAVE.get(), Statement.RETURN_GENERATED_KEYS)) {
            fillStatement(ps, client);
            ps.execute();
            try (
                    ResultSet generatedKeys = ps.getGeneratedKeys()) {
                generatedKeys.next();
                client.setId(generatedKeys.getLong(1));
            }
            return client;
        } catch (SQLException e) {
            LOGGER.info(e.getMessage());
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public Client update(final Client client) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(Query.CLIENT_UPDATE.get())) {
            fillStatement(ps, client);
            ps.setLong(5, client.getId());
            ps.executeUpdate();
            return findById(client.getId());
        } catch (SQLException e) {
            LOGGER.info(e.getMessage());
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public int delete(final Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(Query.CLIENT_DELETE.get())) {
            ps.setLong(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.info(e.getMessage());
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public Client findById(final Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(Query.CLIENT_FIND_BY_ID.get())) {
            ps.setLong(1, id);
            final Client client;
            try (ResultSet resultSet = ps.executeQuery()) {
                resultSet.next();
                client = parseRs(resultSet);
            }
            return client;
        } catch (final SQLException e) {
            LOGGER.info(e.getMessage());
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public List<Client> findAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(Query.CLIENT_FIND_ALL.get())) {
            final List<Client> clientList = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    clientList.add(parseRs(rs));
                }
            }
            return clientList;
        } catch (SQLException e) {
            LOGGER.info(e.getMessage());
            throw new DaoException(e.getMessage(), e);
        }
    }

    private void fillStatement(final PreparedStatement prepare, final Client client) throws SQLException {
        prepare.setString(1, client.getFullName());
        prepare.setString(2, client.getTelephone());
        prepare.setString(3, client.getEmail());
        prepare.setDate(4, client.getBirthday());
    }

    private Client parseRs(final ResultSet rs) throws SQLException {
        return new Client(rs.getLong("id"), rs.getString("full_name"), rs.getString("phone_number"),
                rs.getNString("email"), rs.getDate("birthday"));
    }
}