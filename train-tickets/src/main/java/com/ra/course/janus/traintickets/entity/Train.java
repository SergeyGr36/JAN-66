package com.ra.course.janus.traintickets.entity;



public class Train  {

    private static final int QUANTITY_PLACES = 100;

    private long id;

    private String name;

    private int freePlaces;


    public Train() {
    }

    public Train(long id, String name) {
        this.id = id;
        this.name = name;
        this.freePlaces = QUANTITY_PLACES;
    }

    public Train(long id, String name, int freePlaces) {
        this.id = id;
        this.name = name;
        this.freePlaces = freePlaces;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantityPlaces() {
        return QUANTITY_PLACES;
    }

    public int getFreePlaces() {
        return freePlaces;
    }

    public void setFreePlaces(int freePlaces) {
        this.freePlaces = freePlaces;
    }
}
