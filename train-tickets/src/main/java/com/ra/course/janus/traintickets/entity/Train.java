package com.ra.course.janus.traintickets.entity;

import java.util.*;

public class Train  {

    private long id;

    private String startPoint;

    private String endPoint;

    private  int numberOfVacantSeats;

    private Map <Date,String> trainTimetable;

    public Train(long id, String startPoint, String endPoint, int numberOfVacantSeats) {
        this.id = id;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.numberOfVacantSeats = numberOfVacantSeats;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public int getNumberOfVacantSeats() {
        return numberOfVacantSeats;
    }

    public void setNumberOfVacantSeats(int numberOfVacantSeats) {
        this.numberOfVacantSeats = numberOfVacantSeats;
    }
}
