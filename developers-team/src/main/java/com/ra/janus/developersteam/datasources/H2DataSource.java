package com.ra.janus.developersteam.datasources;

import org.h2.jdbcx.JdbcDataSource;
import javax.sql.DataSource;

public enum H2DataSource implements IDataSource {

    INSTANCE;

    private JdbcDataSource dataSource;

    @Override
    public DataSource get(final String url, final String user, final String pass) {

        if (dataSource == null) {
            dataSource = new JdbcDataSource();
            dataSource.setURL(url);
            dataSource.setUser(user);
            dataSource.setPassword(pass);
        }
        return dataSource;
    }
}
