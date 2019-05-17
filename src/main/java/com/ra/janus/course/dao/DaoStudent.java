package com.ra.janus.course.dao;

import com.ra.janus.course.entity.Student;

import java.util.Collection;
import java.util.List;

public interface DaoStudent {
    void insert(Student student);

    default void insert(Collection<Student> students) {
        students.forEach(this::insert);
    }

    void update(Student student);

    void delete(Student student);

    Student findByStudentId(int Id);

    List<Student> findAll();

    default String getName(String name) {
        throw new IllegalStateException("Method is not implemented!");
    }

    default int countAll() {
        throw new IllegalStateException("Method is not implemented!");
    }
}


