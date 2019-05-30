package com.ra.hotel.entity;

import java.io.Serializable;
import java.util.Objects;

public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String num;
    private TypeRoom type;
    private String description;

    public Room() {
        type = new TypeRoom();
    }

    public Room(String num, TypeRoom type, String description) {
        this.num = num;
        this.type = type;
        this.description = description;
    }

    public Room(long id, String num, long type, String description) {
        this.id = id;
        this.num = num;
        this.type = new TypeRoom(type);
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public TypeRoom getType() {
        return type;
    }

    public void setType(TypeRoom type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return id == room.id &&
                Objects.equals(num, room.num) &&
                Objects.equals(type, room.type) &&
                Objects.equals(description, room.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, num, type, description);
    }
}