package com.ra.janus.developersteam.entity;

import java.util.Objects;

public class TechnicalTask {
    private long id;
    private String title;
    private String description;

    public TechnicalTask() {
    }

    public TechnicalTask(long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public TechnicalTask(long id, TechnicalTask task) {
        this(id, task.getTitle(), task.getDescription());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TechnicalTask that = (TechnicalTask) o;
        return id == that.id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
