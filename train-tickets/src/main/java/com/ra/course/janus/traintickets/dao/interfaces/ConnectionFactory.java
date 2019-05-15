package com.ra.course.janus.traintickets.dao.interfaces;

import java.sql.Connection;

public interface ConnectionFactory {
    Connection getConnection();
}
