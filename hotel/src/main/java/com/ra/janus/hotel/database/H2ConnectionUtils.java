package com.ra.janus.hotel.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class H2ConnectionUtils {

    private H2ConnectionUtils() {
    }

    public static Connection getConnection(final String url, final String user, final String pass) throws SQLException {
        return  DriverManager.getConnection(url, user, pass);
    }

}
