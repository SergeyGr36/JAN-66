package com.ra.janus.developersteam.connections;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public interface iConnection {

    Connection getConnection() throws SQLException, IOException;
}
