package com.ra.course.janus.traintickets.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Train  {

    private long id;

    private String name;

    private int seating;

    private transient int freeSeats;

    public Train(Long id, Train train) {
        this (id,train.getName(),train.getSeating(),train.getFreeSeats());
    }
}
