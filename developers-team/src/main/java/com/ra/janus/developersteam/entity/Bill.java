package com.ra.janus.developersteam.entity;

import java.sql.Date;
import java.util.Objects;

public class Bill {
    private long id;
    private Date docDate;

    public Bill() {
    }

    public Bill(Date docDate) {
        this.docDate = docDate;
    }

    public Bill(long id, Date docDate) {
        this.id = id;
        this.docDate = docDate;
    }

    public Bill(long id, Bill bill) {
        this(id, bill.getDocDate());
    }

    public Date getDocDate() {
        return docDate;
    }

    public void setDocDate(Date docDate) {
        this.docDate = docDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bill work = (Bill) o;
        return id == work.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
