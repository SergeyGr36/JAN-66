package com.ra.janus.hotel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String num;
    private long idTypeRoom;
    private String description;

}
