package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.entity.Student;

import java.util.Collection;
import java.util.List;

public interface DaoStudent {
    Student insert(Student student);
    boolean update(Student student);

    boolean delete(Student student);

    Student findByStudentId(int Id);

    List<Student> findAll();

}
