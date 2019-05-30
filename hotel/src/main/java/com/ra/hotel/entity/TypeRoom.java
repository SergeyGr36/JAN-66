package com.ra.hotel.entity;

import java.io.Serializable;

public class TypeRoom implements Serializable {

    public TypeRoom() {
    }

    private static final long serialVersionUID = 1L;

    private long id;

    public TypeRoom(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
