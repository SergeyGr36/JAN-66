package com.ra.janus.developersteam.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Work implements BaseEntity {
    private long id;
    private String name;
    private BigDecimal price;

    public Work() {
    }

    public Work(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Work(long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Work(long id, Work work) {
        this(id, work.getName(), work.getPrice());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Work work = (Work) o;
        return id == work.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
