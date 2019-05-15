package com.ra.janus.developersteam.connections;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionFactory {

    public Connection getConnection(DataSourceType dataSourceType) throws SQLException, IOException {

        Connection connection = null;
        switch (dataSourceType) {
            case H2:
                connection = H2Connection.INSTANCE.getConnection();
        }
        return connection;
    }
}
