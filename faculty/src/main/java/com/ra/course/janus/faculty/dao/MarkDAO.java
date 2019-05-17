package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.entity.Mark;
import java.util.Collection;
import java.util.List;

public interface MarkDAO {

    void insert(Mark mark);

    default void insert(Collection<Mark> marks) {
        marks.forEach(this::insert);
    }

    void update(Mark mark);

    void delete(Mark mark);

    List<Mark> findAll();
}
