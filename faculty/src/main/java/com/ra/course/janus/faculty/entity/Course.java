package com.ra.course.janus.faculty.entity;

import java.util.Objects;

public class Course {

    private long tid;
    private String code;
    private String description;

    public Course() {
    }

    public Course(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public Course(long tid, String code, String description) {
        this.tid = tid;
        this.code = code;
        this.description = description;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return tid == course.getTid();
    }

    @Override
    public int hashCode() {
        return Objects.hash(tid);
    }

    @Override
    public String toString() {
        return "Course{" +
                "tid=" + tid +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
