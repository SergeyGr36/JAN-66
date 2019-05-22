package com.ra.hotel.entity;


import com.ra.hotel.entity.enums.StatusOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private Client client;
    private TypeRoom typeRoom;
    private Date dateIn;
    private Date dateOut;
    private StatusOrder status;
    private Date dateCreate;
    private Date dateUpdate;
    private Room room;

}
