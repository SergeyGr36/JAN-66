package com.ra.course.janus.faculty.entity;

public class TeacherEntity {

    private String id;
    private String name;
    private String course;

    public TeacherEntity() {

    }

    public TeacherEntity(String id, String name, String course) {
        this.id = id;
        this.name = name;
        this.course = course;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
    public String toString() {
        return "TeacherEntity { " +
                "id = '" + id + '\'' +
                ", name = '" + name + '\'' +
                ", course = '" + course + '\'' +
                '}';
    }
}
