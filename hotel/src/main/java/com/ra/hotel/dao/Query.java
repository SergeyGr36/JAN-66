package com.ra.hotel.dao;

public interface Query {
    String CLIENT_SAVE_SQL = "INSERT INTO client (  full_name, phone_number, email, birthday ) VALUES ( ?, ?, ?, ?)";
    String CLIENT_UPDATE_SQL = "UPDATE client SET full_name=?, phone_number=?, email=?, birthday=? WHERE id=?";
    String CLIENT_DELETE_SQL = "DELETE FROM client WHERE id=?";
    String CLIENT_FIND_BY_ID_SQL = "SELECT * FROM client WHERE id=?";
    String CLIENT_FIND_ALL = "SELECT * FROM client";
    String TRUNCATE_CLIENT_TABLE = "TRUNCATE TABLE client";

    String ORDER_SELECT_BY_ID = "select * from T_ORDER where ID = ?";
    String ORDER_SELECT_ALL = "select * from T_ORDER";
    String ORDER_DELETE_BY_ID = "delete from T_ORDER where ID = ?";
    String ORDER_INSERT = "insert into T_ORDER (ID_CLIENT, ID_TYPE_ROOM, DATE_IN, DATE_OUT, STATUS, DATE_CREATE, DATE_UPDATE, ID_ROOM, ID) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    String ORDER_UPDATE_BY_ID = "update T_ORDER set ID_CLIENT = ?, ID_TYPE_ROOM = ?, DATE_IN = ?, DATE_OUT = ?, STATUS = ?, DATE_CREATE = ?, DATE_UPDATE = ?, ID_ROOM = ? where ID = ?";
}
