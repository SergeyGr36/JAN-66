package com.ra.course.janus.traintickets.entity;



public class Train  {

    private long id;

    private String name;

    private int quanyityPlaces;

    private int freePlaces;


    public Train() {
    }

    public Train(long id, String name, int quanyityPlaces, int freePlaces) {
        this.id = id;
        this.name = name;
        this.quanyityPlaces = quanyityPlaces;
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

    public int getQuanyityPlaces() {
        return quanyityPlaces;
    }

    public void setQuanyityPlaces(int quanyityPlaces) {
        this.quanyityPlaces = quanyityPlaces;
    }

    public int getFreePlaces() {
        return freePlaces;
    }

    public void setFreePlaces(int freePlaces) {
        this.freePlaces = freePlaces;
    }
}
