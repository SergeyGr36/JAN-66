package com.ra.janus.hotel.entity;

import com.ra.janus.hotel.enums.StatusOrder;

import java.util.Date;
import java.util.Objects;

public class Order {

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
        this.dateIn = dateIn;
        this.dateOut = dateOut;
        this.status = status;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
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
        return dateIn;
    }

    public void setDateIn(Date dateIn) {
        this.dateIn = dateIn;
    }

    public Date getDateOut() {
        return dateOut;
    }

    public void setDateOut(Date dateOut) {
        this.dateOut = dateOut;
    }

    public StatusOrder getStatus() {
        return status;
    }

    public void setStatus(StatusOrder status) {
        this.status = status;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
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
