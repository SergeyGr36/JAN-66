package com.ra.course.janus.traintickets.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Train  {

    private long id;

    private String name;

    private int seating;

    private transient int freeSeats;
}
