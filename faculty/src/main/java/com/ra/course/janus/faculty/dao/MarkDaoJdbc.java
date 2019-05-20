package com.ra.course.janus.faculty.dao;

import com.ra.course.janus.faculty.entity.Mark;

import java.sql.*;
import java.util.List;

import javax.sql.DataSource;
import java.util.ArrayList;

public class MarkDaoJdbc implements MarkDao  {

    private static final String INSERT_SQL = "INSERT INTO MARK ( SCORE, REFERENCE) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE MARK SET SCORE=?,REFERENCE=? WHERE MARK_TID=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM MARK";
    private static final String SELECT_ONE_SQL = "SELECT * FROM MARK WHERE MARK_TID = ?";
    private static final String DELETE_SQL = "DELETE FROM MARK WHERE MARK_TID=?";


    private transient final DataSource dataSource;

    public MarkDaoJdbc(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Mark insert(final Mark mark) {
        try (Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            try {
                ps.setInt(1,mark.getScore());
                ps.setString(2,mark.getReference());
                final int n = ps.executeUpdate();

                if (n>0) {
                    final ResultSet markTid = ps.getGeneratedKeys();
                    markTid.next();
                    mark.setTid(markTid.getInt(1));
                    markTid.close();
                    return mark;
                }
                else{
                    return null;
                }
            }  finally {
                if (ps != null){ ps.close();}
            }

        } catch (SQLException e) {
            throw new MarkDaoException("Mark inserting " + e.getMessage());
        }

    }

    @Override
    public Mark update(final Mark mark) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            try {
                ps.setInt(1,mark.getScore());
                ps.setString(2,mark.getReference());
                ps.setInt(3,mark.getTid());
                return ps.executeUpdate()>0?mark:null;

            }  finally {
                if (ps != null){ ps.close();}
            }

        } catch (SQLException e) {
            throw new MarkDaoException("Mark update " + e.getMessage());
        }

    }

    public Mark update1(final Mark mark) throws SQLException {
        final PreparedStatement ps = dataSource.getConnection().prepareStatement(UPDATE_SQL);
        ps.setInt(1, mark.getScore());
        ps.setString(2, mark.getReference());
        ps.setInt(3, mark.getTid());
        final int n = ps.executeUpdate();
        ps.close();
        return n > 0 ? mark : null;
    }

    @Override
    public boolean delete(final Mark mark) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL))
        {
            try {
            ps.setInt(1,mark.getTid());
            final int n = ps.executeUpdate();

            return n>0?true:false;
            }  finally {
                if (ps != null){ ps.close();}
            }

        } catch (SQLException e) {
            throw new MarkDaoException("Mark delete " + e.getMessage());
        }
    }

    @Override
    public Mark findByTid(final Integer tid) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ONE_SQL))
        {
             try {
                 ps.setInt(1, tid.intValue());
                 final ResultSet rs = ps.executeQuery();

                 if (rs.next()) {
                     return toMark(rs);
                 }
                 else {
                     return null;
                 }
             }  finally {
                 if (ps != null){ ps.close();}
             }
        } catch (SQLException e) {
            throw new MarkDaoException("Find by tid " +tid+ e.getMessage());
        }
    }

    @Override
    public List<Mark> findAll() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            try {
                final List<Mark> marks = new ArrayList<>();

                while (rs.next()) {
                    marks.add(toMark(rs));
                }
                return marks;
            }  finally {
                if (ps != null){ ps.close();}
            }
        } catch (SQLException e) {
            throw new MarkDaoException("Find all " + e.getMessage());
        }
    }

    private Mark toMark(final ResultSet rs) throws SQLException {
        final Mark result  = new Mark();
        result.setTid(rs.getInt("MARK_TID"));
        result.setScore(rs.getInt("SCORE"));
        result.setReference(rs.getString("REFERENCE"));
        return result;
    }

}
