package com.ra.janus.developersteam.datasources;

import javax.sql.DataSource;

public interface IDataSource {

    DataSource get(final String url, final String user, final String pass);
}
