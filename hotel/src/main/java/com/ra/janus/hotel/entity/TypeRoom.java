package com.ra.janus.hotel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}