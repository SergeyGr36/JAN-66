package com.ra.janus.developersteam.entity;

public class Customer {
    private long id;
    private final String name;

    public Customer(String name) {
        this.name = name;
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
}
