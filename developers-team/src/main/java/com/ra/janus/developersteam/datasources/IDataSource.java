package com.ra.janus.developersteam.datasources;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

public interface IDataSource {

    DataSource get() throws SQLException, IOException;
}
