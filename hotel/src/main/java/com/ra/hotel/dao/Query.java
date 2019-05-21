package com.ra.hotel.dao;

public interface Query {
    String CLIENT_SAVE_SQL = "INSERT INTO client (  full_name, phone_number, email, birthday ) VALUES ( ?, ?, ?, ?)";
    String CLIENT_UPDATE_SQL = "UPDATE client SET full_name=?, phone_number=?, email=?, birthday=? WHERE id=?";
    String CLIENT_DELETE_SQL = "DELETE FROM client WHERE id=?";
    String CLIENT_FIND_BY_ID_SQL = "SELECT * FROM client WHERE id=?";
    String CLIENT_FIND_ALL="SELECT * FROM client";
    String TRUNCATE_CLIENT_TABLE="TRUNCATE TABLE client";
}
