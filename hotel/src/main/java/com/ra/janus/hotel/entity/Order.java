package com.ra.janus.hotel.entity;

import com.ra.janus.hotel.enums.StatusOrder;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    //private Client client;
    private long idClient;
    //private TypeRoom typeRoom;
    private long idTypeRoom;
    private Date dateIn;
    private Date dateOut;
    private StatusOrder status = StatusOrder.NEW;
    private Date dateCreate;
    private Date dateUpdate;
    //private Room room;
    private Long idRoom;

    public Order() {}

    public Order(long idClient, long idTypeRoom, Date dateIn, Date dateOut, StatusOrder status, Date dateCreate, Date dateUpdate, Long idRoom) {
        this.idClient = idClient;
        this.idTypeRoom = idTypeRoom;
        this.dateIn = dateIn != null ? (Date) dateIn.clone() : null;
        this.dateOut = dateOut != null ? (Date) dateOut.clone() : null;
        this.status = status;
        this.dateCreate = dateCreate != null ? (Date) dateCreate.clone() : null;
        this.dateUpdate = dateUpdate != null ? (Date) dateUpdate.clone() : null;
        this.idRoom = idRoom;
    }

    public Order(long id, long idClient, long idTypeRoom, Date dateIn, Date dateOut, StatusOrder status, Date dateCreate, Date dateUpdate, Long idRoom) {
        this.id = id;
        this.idClient = idClient;
        this.idTypeRoom = idTypeRoom;
        this.dateIn = dateIn != null ? (Date) dateIn.clone() : null;
        this.dateOut = dateOut != null ? (Date) dateOut.clone() : null;
        this.status = status;
        this.dateCreate = dateCreate != null ? (Date) dateCreate.clone() : null;
        this.dateUpdate = dateUpdate != null ? (Date) dateUpdate.clone() : null;
        this.idRoom = idRoom;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateIn() {
        return dateIn != null ? new Date(dateIn.getTime()) : null;
    }

    public void setDateIn(Date dateIn) {
        this.dateIn = dateIn != null ? (Date) dateIn.clone() : null;
    }

    public Date getDateOut() {
        return dateOut != null ? new Date(dateOut.getTime()) : null;
    }

    public void setDateOut(Date dateOut) {
        this.dateOut = dateOut != null ? (Date) dateOut.clone() : null;
    }

    public StatusOrder getStatus() {
        return status;
    }

    public void setStatus(StatusOrder status) {
        this.status = status;
    }

    public Date getDateCreate() {
        return dateCreate != null ? new Date(dateCreate.getTime()) : null;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate != null ? (Date) dateCreate.clone() : null;
    }

    public Date getDateUpdate() {
        return dateUpdate != null ? new Date(dateUpdate.getTime()) : null;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate != null ? (Date) dateUpdate.clone() : null;
    }

    public long getIdClient() {
        return idClient;
    }

    public void setIdClient(long idClient) {
        this.idClient = idClient;
    }

    public long getIdTypeRoom() {
        return idTypeRoom;
    }

    public void setIdTypeRoom(long idTypeRoom) {
        this.idTypeRoom = idTypeRoom;
    }

    public Long getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(Long idRoom) {
        this.idRoom = idRoom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id &&
                idClient == order.idClient &&
                idTypeRoom == order.idTypeRoom &&
                Objects.equals(dateIn, order.dateIn) &&
                Objects.equals(dateOut, order.dateOut) &&
                status == order.status &&
                Objects.equals(dateCreate, order.dateCreate) &&
                Objects.equals(dateUpdate, order.dateUpdate) &&
                Objects.equals(idRoom, order.idRoom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idClient, idTypeRoom, dateIn, dateOut, status, dateCreate, dateUpdate, idRoom);
    }
}
