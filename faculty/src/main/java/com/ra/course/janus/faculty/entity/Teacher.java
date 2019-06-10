package com.ra.course.janus.faculty.entity;

import java.util.Objects;

public class Teacher {
    private long id;
    private String name;
    private String course;

    public Teacher() {}

    public Teacher(long id, String name, String course) {
        this.id = id;
        this.name = name;
        this.course = course;
    }

    public Teacher (Teacher teacher) {
        this.id = teacher.getId();
        this.name = teacher.getName();
        this.course = teacher.getCourse();
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

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, course);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return id == teacher.id &&
                Objects.equals(name, teacher.name) &&
                Objects.equals(course, teacher.course);
    }

    @Override
    public String toString() {
        return "Teacher {" +
                "id = '" + id + '\'' +
                ", name = '" + name + '\'' +
                ", course = '" + course + '\'' +
                '}';
    }
}
