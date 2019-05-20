package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.entity.Mark;
import java.util.Collection;
import java.util.List;

public interface MarkDao {

    Mark insert(Mark mark);

    Mark update(Mark mark);

    boolean delete(Mark mark);

    Mark findByTid(Integer tid);

    List<Mark> findAll();
}
