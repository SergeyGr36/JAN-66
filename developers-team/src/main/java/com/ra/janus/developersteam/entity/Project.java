package com.ra.janus.developersteam.entity;

import java.sql.Date;
import java.util.Objects;

public class Project {

    private long id;
    private String name;
    private String description;
    private String status;
    private Date eta;

    public Project(String name, String description, String status, Date eta) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.eta = eta;
    }

    public Project(long id, String name, String description, String status, Date eta) {
        this(name, description, status, eta);
        this.id = id;
    }

    public Project(long id, Project project) {
        this(id, project.getName(), project.getDescription(), project.getStatus(), project.getEta());
    }

    public Project(long id) {
        this(id, null, null, null, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getEta() {
        return eta;
    }

    public void setEta(Date eta) {
        this.eta = eta;
    }
}
