package com.ra.janus.hotel.dao;

import com.ra.janus.hotel.entity.Client;
import com.ra.janus.hotel.exception.DaoException;

import javax.sql.DataSource;
import java.util.List;

public class ClientDAO implements IEntityDAO<Client>{

    private final String SAVE_SQL="INSERT INTO Client VALUE (id=?, firstName=?, secondName=?, lastName=?, age=?, telephone=?, email=?, birthday=?);";
    private final String UPDATE_SQL="";
    private final String DELETE_SQL="";
    private final String FIND_BY_ID_SQL="";
    private final String FIND_BY_PARAM_SQL="";
    private final String FIND_LIST_BY_PARAM_SQL="";

    private final DataSource dataSource=

    @Override
    public Client save(Client client) throws DaoException {
        return null;
    }

    @Override
    public Client update(Client client) throws DaoException {
        return null;
    }

    @Override
    public void delete(long id) throws DaoException {

    }

    @Override
    public Client findById(long id) throws DaoException {
        return null;
    }

    @Override
    public Client findByParam(Object... params) throws DaoException {
        return null;
    }

    @Override
    public List<Client> findListByParam(Object... params) throws DaoException {
        return null;
    }
}
