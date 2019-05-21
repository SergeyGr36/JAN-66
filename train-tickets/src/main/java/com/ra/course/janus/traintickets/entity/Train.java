package com.ra.course.janus.traintickets.entity;



public class Train  {

    private long id;

    private String name;

    private int numberOfPlaces;

    public Train(long id, String name, int numberOfPlaces) {
        this.id = id;
        this.name = name;
        this.numberOfPlaces = numberOfPlaces;
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

    public int getNumberOfPlaces() {
        return numberOfPlaces;
    }

    public void setNumberOfPlaces(int numberOfPlaces) {
        this.numberOfPlaces = numberOfPlaces;
    }
}
