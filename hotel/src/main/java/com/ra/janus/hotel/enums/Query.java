package com.ra.janus.hotel.enums;

public enum Query {
    //client
    CLIENT_SAVE("INSERT INTO client (  full_name, phone_number, email, birthday ) VALUES ( ?, ?, ?, ?)"),
    CLIENT_UPDATE("UPDATE client SET full_name=?, phone_number=?, email=?, birthday=? WHERE id=?"),
    CLIENT_DELETE("DELETE FROM client WHERE id=?"),
    CLIENT_FIND_BY_ID("SELECT * FROM client WHERE id=?"),
    CLIENT_FIND_ALL("SELECT * FROM client"),

    //order
    MSG_ERR_SAVE("record order not saved"),
    MSG_ERR_UPDATE("record order not updated"),
    SELECT_ORDER_BY_ID("select * from T_ORDER where ID = ?"),
    SELECT_ALL_ORDERS("select * from T_ORDER"),
    DELETE_ORDER_BY_ID("delete from T_ORDER where ID = ?"),
    INSERT_ORDER("insert into T_ORDER (ID_CLIENT, ID_TYPE_ROOM, DATE_IN, DATE_OUT, STATUS, DATE_CREATE, DATE_UPDATE, ID_ROOM) values (?, ?, ?, ?, ?, ?, ?, ?)"),
    UPDATE_ORDER_BY_ID("update T_ORDER set ID_CLIENT = ?, ID_TYPE_ROOM = ?, DATE_IN = ?, DATE_OUT = ?, STATUS = ?, DATE_CREATE = ?, DATE_UPDATE = ?, ID_ROOM = ? where ID = ?"),

    //room
    SELECT_ROOM("SELECT * FROM Rooms WHERE id = ?"),
    SELECT_ALL_ROOMS("SELECT * FROM Rooms"),
    REMOVE_ROOM("DELETE FROM Rooms WHERE id = ?"),
    ADD_ROOM("INSERT INTO Rooms (num, type_id, description) VALUES (?, ?, ?)"),
    UPDATE_ROOM("UPDATE Rooms SET num = ?, type_id = ?, description = ? WHERE id = ?"),

    //typeRoom

    UPDATE_ERR_MSG("record not updated"),
    INSERT_TYPE_BY_ID("INSERT INTO TYPE_ROOM (COUNT_PLACES, PRISE, DESCRIPTION, CLASS_OF_ROOM, ID) VALUES (?, ?, ?, ?, ?)"),
    SAVE_ERR_MSG("record not saved"),
    UPDATE_TYPE_BY_ID("UPDATE TYPE_ROOM SET COUNT_PLACES = ?, PRISE = ?, DESCRIPTION = ?, CLASS_OF_ROOM = ? WHERE ID = ?"),
    DELETE_TYPE_BY_ID("DELETE FROM TYPE_ROOM WHERE ID = ?"),
    SELECT_TYPE_BY_ID("SELECT * FROM TYPE_ROOM WHERE ID = ?"),
    SELECT_ALL_TYPES("SELECT * FROM TYPE_ROOM");

    private String s;

    Query(final String s) {
        this.s = s;
    }

    public String get() {
        return this.s;
    }
}
