package com.ra.hotel.entity.enums;

public enum  Query {
    CLIENT_SAVE ( "INSERT INTO client (  full_name, phone_number, email, birthday ) VALUES ( ?, ?, ?, ?)"),
    CLIENT_UPDATE ("UPDATE client SET full_name=?, phone_number=?, email=?, birthday=? WHERE id=?"),
    CLIENT_DELETE ( "DELETE FROM client WHERE id=?"),
    CLIENT_FIND_BY_ID ( "SELECT * FROM client WHERE id=?"),
    CLIENT_FIND_ALL ( "SELECT * FROM client"),
    ORDER_FIND_BY_ID ( "select * from T_ORDER where ID = ?"),
    ORDER_FIND_ALL ( "select * from T_ORDER"),
    ORDER_DELETE ( "delete from T_ORDER where ID = ?"),
    ORDER_INSERT ( "insert into T_ORDER (ID_CLIENT, ID_TYPE_ROOM, DATE_IN, DATE_OUT, STATUS, DATE_CREATE, DATE_UPDATE, ID_ROOM, ID) values (?, ?, ?, ?, ?, ?, ?, ?, ?)"),
    ORDER_UPDATE ( "update T_ORDER set ID_CLIENT = ?, ID_TYPE_ROOM = ?, DATE_IN = ?, DATE_OUT = ?, STATUS = ?, DATE_CREATE = ?, DATE_UPDATE = ?, ID_ROOM = ? where ID = ?");
    private String s;
    Query(final String s){
        this.s = s;
    }
    public String get(){
        return this.s;
    }
}
