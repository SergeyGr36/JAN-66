package com.ra.janus.hotel.entity;

import java.io.Serializable;
import java.util.Objects;

public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String num;
    private long idTypeRoom;
    private String description;

    public Room() {
    }

    public Room(String num, long idTypeRoom, String description) {
        this.num = num;
        this.idTypeRoom = idTypeRoom;
        this.description = description;
    }

    public Room(long id, String num, long idTypeRoom, String description) {
        this.id = id;
        this.num = num;
        this.idTypeRoom = idTypeRoom;
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

    public long getIdTypeRoom() {
        return idTypeRoom;
    }

    public void setIdTypeRoom(long idTypeRoom) {
        this.idTypeRoom = idTypeRoom;
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
                Objects.equals(idTypeRoom, room.idTypeRoom) &&
                Objects.equals(description, room.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, num, idTypeRoom, description);
    }
}
