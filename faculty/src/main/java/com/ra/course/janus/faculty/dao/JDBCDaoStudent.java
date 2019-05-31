
package com.ra.course.janus.faculty.dao;


import com.ra.course.janus.faculty.entity.Student;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class JDBCDaoStudent implements DaoStudent {
    private static final String INSERT_SQL = "INSERT INTO STUDENT (NAME,SURNAME,ID) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE STUDENT SET NAME=?,SURNAME=?,WHERE ID=?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM STUDENT";
    private static final String SELECT_ONE_SQL = "SELECT * FROM STUDENT WHERE ID= ?";
    private static final String DELETE_SQL = "DELETE FROM STUDENT WHERE ID=?";


    private final DataSource dataSource;

    public JDBCDaoStudent(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void insert(Student student) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
            prepareStatement(ps, student);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

@Override
    public Student findByStudentId(String id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ONE_SQL)) {
            ps.setString(1, id);

            Student student = null;
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    student = toStudent(rs);
                }
            }
            return student;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Student> findAll() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            List<Student> students = new ArrayList<>();
            while (rs.next()) {
                students.add(toStudent(rs));
            }
            return students;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Student toStudent(ResultSet rs) throws SQLException {
        return new Student(rs.getString("NAME"),
                rs.getString("SURNAME"), rs.getInt("ID"));

    }

    private void prepareStatement(PreparedStatement ps, Student student) throws SQLException {
        ps.setInt(1, student.getId());
        ps.setString(2, student.getName());
        ps.setString(3, student.getSurname());

    }

    @Override
    public void update(Student student) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            prepareStatement(ps, student);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Student student) {
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {
        ps.setInt(1, Student.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

