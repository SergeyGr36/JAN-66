package com.ra.janus.hotel.entity;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TypeRoom implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private int countPlaces;
    private int prise;
    private String description;
    private String classOfRoom;

    public TypeRoom(int countPlaces, int prise, String description, String classOfRoom) {
        this.countPlaces = countPlaces;
        this.prise = prise;
        this.description = description;
        this.classOfRoom = classOfRoom;
    }
}