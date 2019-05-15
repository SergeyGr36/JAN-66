package com.ra.janus.faculty.entity;

public class Teacher {
    private String name;

    Teacher(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Teacher { " +
                "name = '" + name + '\'' +
                '}';
    }
}
