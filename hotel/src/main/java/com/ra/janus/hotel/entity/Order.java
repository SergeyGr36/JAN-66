package com.ra.janus.hotel.entity;

import com.ra.janus.hotel.enums.StatusOrder;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private long idClient;
    private long idTypeRoom;
    private Date dateIn;
    private Date dateOut;
    private StatusOrder status = StatusOrder.NEW;
    private Date dateCreate;
    private Date dateUpdate;
    private long idRoom;

    public Order(long idClient, long idTypeRoom, Date dateIn, Date dateOut, StatusOrder status, Date dateCreate, Date dateUpdate, Long idRoom) {
        this.idClient = idClient;
        this.idTypeRoom = idTypeRoom;
        this.dateIn = dateIn;
        this.dateOut = dateOut;
        this.status = status;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.idRoom = idRoom;
    }
}
