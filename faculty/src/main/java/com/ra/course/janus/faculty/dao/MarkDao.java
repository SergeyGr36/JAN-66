package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.entity.Mark;

import java.sql.SQLException;
import java.util.List;

public interface MarkDao {

    Mark insert(Mark mark) throws SQLException;

    Mark update(Mark mark) throws SQLException;

    boolean delete(Mark mark) throws SQLException;

    Mark findByTid(Integer tid) throws SQLException;

    List<Mark> findAll() throws SQLException;
}
