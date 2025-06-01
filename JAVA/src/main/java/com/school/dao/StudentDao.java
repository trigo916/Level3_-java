package com.school.dao;

import com.school.entity.Student;
import com.school.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Student entity
 */
public class StudentDao {
    /**
     * Find student by user ID
     * @param userId User ID of the student
     * @return Student object or null if not found
     */
    public Student findByUserId(int userId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Student student = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT s.id, s.student_id, s.name, s.age, s.major, u.username, u.password, u.role " +
                         "FROM students s " +
                         "JOIN users u ON s.user_id = u.id " +
                         "WHERE s.user_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                student = new Student();
                student.setId(rs.getInt("id"));
                student.setStudentId(rs.getString("student_id"));
                student.setName(rs.getString("name"));
                student.setAge(rs.getInt("age"));
                student.setMajor(rs.getString("major"));
                student.setUsername(rs.getString("username"));
                student.setPassword(rs.getString("password"));
                student.setRole(rs.getString("role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        
        return student;
    }
    
    /**
     * Register a new student
     * @param student Student to register
     * @return ID of the newly added student, or -1 if failed
     */
    public int register(Student student) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int studentId = -1;
        
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // Insert user record first
            String userSql = "INSERT INTO users (username, password, role) VALUES (?, ?, 'student')";
            ps = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, student.getUsername());
            ps.setString(2, student.getPassword());
            
            int userRows = ps.executeUpdate();
            if (userRows == 0) {
                conn.rollback();
                return -1;
            }
            
            // Get the generated user ID
            rs = ps.getGeneratedKeys();
            int userId = -1;
            if (rs.next()) {
                userId = rs.getInt(1);
            } else {
                conn.rollback();
                return -1;
            }
            
            // Insert student record
            String studentSql = "INSERT INTO students (user_id, student_id, name, age, major) VALUES (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(studentSql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, userId);
            ps.setString(2, student.getStudentId());
            ps.setString(3, student.getName());
            ps.setInt(4, student.getAge());
            ps.setString(5, student.getMajor());
            
            int studentRows = ps.executeUpdate();
            if (studentRows == 0) {
                conn.rollback();
                return -1;
            }
            
            // Get the generated student ID
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                studentId = rs.getInt(1);
            } else {
                conn.rollback();
                return -1;
            }
            
            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DBUtil.close(conn, ps, rs);
        }
        
        return studentId;
    }
    
    /**
     * Find student by username
     * @param username Username of the student
     * @return Student object or null if not found
     */
    public Student findByUsername(String username) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Student student = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT s.id, s.student_id, s.name, s.age, s.major, u.id as user_id, u.username, u.password, u.role " +
                         "FROM students s " +
                         "JOIN users u ON s.user_id = u.id " +
                         "WHERE u.username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                student = new Student();
                student.setId(rs.getInt("user_id"));
                student.setStudentId(rs.getString("student_id"));
                student.setName(rs.getString("name"));
                student.setAge(rs.getInt("age"));
                student.setMajor(rs.getString("major"));
                student.setUsername(rs.getString("username"));
                student.setPassword(rs.getString("password"));
                student.setRole(rs.getString("role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        
        return student;
    }
} 