package com.ra.janus.hotel.entity;

import com.ra.janus.hotel.enums.StatusOrder;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

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

    public Order() {}

    public Order(long id, Client client, TypeRoom typeRoom, Date dateIn, Date dateOut, StatusOrder status, Date dateCreate, Date dateUpdate, Room room) {
        this.id = id;
        this.client = client;
        this.typeRoom = typeRoom;
        this.dateIn = dateIn != null ? (Date) dateIn.clone() : null;
        this.dateOut = dateOut != null ? (Date) dateOut.clone() : null;
        this.status = status;
        this.dateCreate = dateCreate != null ? (Date) dateCreate.clone() : null;
        this.dateUpdate = dateUpdate != null ? (Date) dateUpdate.clone() : null;
        this.room = room;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public TypeRoom getTypeRoom() {
        return typeRoom;
    }

    public void setTypeRoom(TypeRoom typeRoom) {
        this.typeRoom = typeRoom;
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

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id &&
                dateIn.equals(order.dateIn) &&
                dateOut.equals(order.dateOut) &&
                status == order.status &&
                dateCreate.equals(order.dateCreate) &&
                Objects.equals(dateUpdate, order.dateUpdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateIn, dateOut, status, dateCreate, dateUpdate);
    }
}
