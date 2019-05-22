package com.ra.course.janus.traintickets.entity;



public class Train  {

    private static final int QUANTITY_OF_PLACES_IN_TRAIN = 100;

    private long id;

    private String name;

    private int numberOfFreePlacesInTheTrain;


    public Train() {
    }

    public Train(long id, String name) {
        this.id = id;
        this.name = name;
        this.numberOfFreePlacesInTheTrain = QUANTITY_OF_PLACES_IN_TRAIN;
    }

    public Train(long id, String name, int numberOfFreePlacesInTheTrain) {
        this.id = id;
        this.name = name;
        this.numberOfFreePlacesInTheTrain = numberOfFreePlacesInTheTrain;
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

    public int getQuantityOfPlacesInTrain() {
        return QUANTITY_OF_PLACES_IN_TRAIN;
    }

    public int getNumberOfFreePlacesInTheTrain() {
        return numberOfFreePlacesInTheTrain;
    }

    public void setNumberOfFreePlacesInTheTrain(int numberOfFreePlacesInTheTrain) {
        this.numberOfFreePlacesInTheTrain = numberOfFreePlacesInTheTrain;
    }
}
