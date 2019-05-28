package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.entity.Mark;

import java.sql.*;
import java.util.List;

import java.util.ArrayList;

import com.ra.course.janus.faculty.exception.DaoException;
import org.apache.log4j.Logger;

public class MarkDaoJdbc implements MarkDao {

    private static final String INSERT_SQL = "INSERT INTO MARK ( SCORE, REFERENCE) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE MARK SET SCORE=?,REFERENCE=? WHERE MARK_TID=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM MARK";
    private static final String SELECT_ONE_SQL = "SELECT * FROM MARK WHERE MARK_TID = ?";
    private static final String DELETE_SQL = "DELETE FROM MARK WHERE MARK_TID=?";

    private static final String INSERT_ERR = "Error inserting Mark";

    private final static Logger LOGGER = Logger.getLogger(MarkDao.class);


    private transient final Connection connection;

    public MarkDaoJdbc(final Connection connection) {
        this.connection = connection;
    }

    @Override
    public Mark insert(final Mark mark)  {
        try {
            try (PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, mark.getScore());
                ps.setString(2, mark.getReference());
                ps.executeUpdate();

                try (ResultSet markTid = ps.getGeneratedKeys()) {
                    markTid.next();
                    mark.setTid(markTid.getLong(1));
                    return mark;
                }
            }
        }
        catch (SQLException e){
            LOGGER.error(INSERT_ERR,e);
            throw new DaoException(INSERT_ERR,e);
        }
    }

    @Override
    public Mark update(final Mark mark) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_SQL)) {

            ps.setInt(1, mark.getScore());
            ps.setString(2, mark.getReference());
            ps.setLong(3, mark.getTid());
            return ps.executeUpdate() > 0 ? mark : null;

        }

    }

    @Override
    public boolean delete(final Mark mark) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(DELETE_SQL)) {
            ps.setLong(1, mark.getTid());
            return ps.executeUpdate() > 0 ? true : false;
        }
    }


    @Override
    public Mark findByTid(final long tid) throws SQLException {

        try  {
            final PreparedStatement ps = connection.prepareStatement(SELECT_ONE_SQL);
            ps.setLong(1, tid);
            final ResultSet rs = ps.executeQuery();
            try {
                if (rs.next()) {
                    return toMark(rs);
                } else {
                    return null;
                }
            } finally {
                rs.close();
            }
        } catch (SQLException e) {
                LOGGER.error("SQL exception during findByTid - ".concat(String.valueOf(tid)),e);
            throw e;
        }

    }

    @Override
    public List<Mark> findAll() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            final List<Mark> marks = new ArrayList<>();

            while (rs.next()) {
                marks.add(toMark(rs));
            }
            return marks;
        }
    }


    private Mark toMark(final ResultSet rs) throws SQLException {
        final Mark result  = new Mark();
        result.setTid(rs.getLong("MARK_TID"));
        result.setScore(rs.getInt("SCORE"));
        result.setReference(rs.getString("REFERENCE"));
        return result;
    }

}
