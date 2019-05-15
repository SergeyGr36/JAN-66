package com.ra.janus.hotel.entity;

public class TypeRoom {
    private long id;
    private int countPlaces;
    private int prise;
    private String discription;
    private String classOfRoom;//Enum?

    public TypeRoom(long id, int countPlaces, int prise, String discription, String classOfRoom) {
        this.id = id;
        this.countPlaces = countPlaces;
        this.prise = prise;
        this.discription = discription;
        this.classOfRoom = classOfRoom;
    }

    public long getId() {
        return id;
    }

    public int getCountPlaces() {
        return countPlaces;
    }

    public int getPrise() {
        return prise;
    }

    public String getDiscription() {
        return discription;
    }

    public String getClassOfRoom() {
        return classOfRoom;
    }
}
