package com.ra.course.janus.traintickets.entity;


import java.util.Objects;

public class Train  {

    private long id;

    private String name;

    private int seating;

    private transient int freeSeats;


    public Train() {
    }

    public Train(long id, String name, int seating, int freeSeats) {
        this.id = id;
        this.name = name;
        this.seating = seating;
        this.freeSeats = freeSeats;
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

    public int getSeating() {
        return seating;
    }

    public void setSeating(int seating) {
        this.seating = seating;
    }

    public int getFreeSeats() {
        return freeSeats;
    }

    public void setFreePlaces(int freeSeats) {
        this.freeSeats = freeSeats;
    }

}
