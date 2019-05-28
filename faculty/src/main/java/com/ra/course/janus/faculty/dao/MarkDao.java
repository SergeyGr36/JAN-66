package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.entity.Mark;

import java.sql.SQLException;
import java.util.List;

public interface MarkDao {

    Mark insert(Mark mark);

    Mark update(Mark mark) throws SQLException;

    boolean delete(Mark mark) throws SQLException;

    Mark findByTid(long tid) throws SQLException;

    List<Mark> findAll() throws SQLException;
}
