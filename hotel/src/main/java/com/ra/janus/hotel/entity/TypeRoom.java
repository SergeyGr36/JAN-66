package com.ra.janus.hotel.entity;

import java.io.Serializable;

public class TypeRoom implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private int countPlaces;
    private int prise;
    private String description;
    private String classOfRoom;//Enum?

    public TypeRoom(long id, int countPlaces, int prise, String description, String classOfRoom) {
        this.id = id;
        this.countPlaces = countPlaces;
        this.prise = prise;
        this.description = description;
        this.classOfRoom = classOfRoom;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCountPlaces() {
        return countPlaces;
    }

    public void setCountPlaces(int countPlaces) {
        this.countPlaces = countPlaces;
    }

    public int getPrise() {
        return prise;
    }

    public void setPrise(int prise) {
        this.prise = prise;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClassOfRoom() {
        return classOfRoom;
    }

    public void setClassOfRoom(String classOfRoom) {
        this.classOfRoom = classOfRoom;
    }
}
