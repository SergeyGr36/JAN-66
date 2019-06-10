package com.ra.janus.developersteam.entity;

import java.util.Objects;

public class Qualification implements BaseEntity {

    private long id;
    private String name;
    private String responsibility;

    public Qualification(String name, String responsibility) {
        this.name = name;
        this.responsibility = responsibility;
    }

    public Qualification(long id, String name, String responsibility) {
        this(name, responsibility);
        this.id = id;
    }

    public Qualification(long id, Qualification qualification) {
        this(id, qualification.getName(), qualification.getResponsibility());
    }

    public Qualification(long id) {
        this(id, null, null);
    }

    public Qualification() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Qualification project = (Qualification) o;
        return id == project.id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }
}


