package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.entity.TeacherEntity;

import java.util.List;

public interface TeacherDao {

    void insert(TeacherEntity teacherEntity);

    void update(TeacherEntity teacherEntity);

    List<TeacherEntity> select();

    void delete(TeacherEntity teacherEntity);

}
