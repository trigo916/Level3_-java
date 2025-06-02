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
 * 学生实体的数据访问对象（DAO）
 */
public class StudentDao {
    /**
     * 根据用户ID查找学生
     * @param userId 学生的用户ID
     * @return 学生对象，如果未找到则返回null
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
                    "JOIN users u ON s.username = u.username " +
                    "WHERE u.id = ?";
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
     * 注册新学生
     * @param student 要注册的学生
     * @return 新添加的学生ID，如果失败则返回-1
     */
    public int register(Student student) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int studentId = -1;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false); // 开始事务

            // 首先插入用户记录
            String userSql = "INSERT INTO users (username, password, role) VALUES (?, ?, 'student')";
            ps = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, student.getUsername());
            ps.setString(2, student.getPassword());

            int userRows = ps.executeUpdate();
            if (userRows == 0) {
                conn.rollback();
                return -1;
            }

            // 获取生成的用户ID
            rs = ps.getGeneratedKeys();
            int userId = -1;
            if (rs.next()) {
                userId = rs.getInt(1);
            } else {
                conn.rollback();
                return -1;
            }

            // 插入学生记录
            String studentSql = "INSERT INTO students (student_id, name, age, major, username) VALUES (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(studentSql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, student.getStudentId());
            ps.setString(2, student.getName());
            ps.setInt(3, student.getAge());
            ps.setString(4, student.getMajor());
            ps.setString(5, student.getUsername());

            int studentRows = ps.executeUpdate();
            if (studentRows == 0) {
                conn.rollback();
                return -1;
            }

            // 获取生成的学生ID
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                studentId = rs.getInt(1);
            } else {
                conn.rollback();
                return -1;
            }

            conn.commit(); // 提交事务
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
     * 根据用户名查找学生
     * @param username 学生的用户名
     * @return 学生对象，如果未找到则返回null
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
                    "JOIN users u ON s.username = u.username " +
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

    /**
     * 更新学生信息（不更改密码）
     * @param student 带有更新信息的学生对象
     * @return 如果更新成功返回true，否则返回false
     */
    public boolean update(Student student) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE students SET name = ?, age = ?, major = ? WHERE username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, student.getName());
            ps.setInt(2, student.getAge());
            ps.setString(3, student.getMajor());
            ps.setString(4, student.getUsername());

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, ps, null);
        }
    }

    /**
     * 更新学生信息（包括密码）
     * @param student 带有更新信息的学生对象，包括密码
     * @return 如果更新成功返回true，否则返回false
     */
    public boolean updateWithPassword(Student student) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false); // 开始事务

            // 更新学生信息
            String studentSql = "UPDATE students SET name = ?, age = ?, major = ? WHERE username = ?";
            ps = conn.prepareStatement(studentSql);
            ps.setString(1, student.getName());
            ps.setInt(2, student.getAge());
            ps.setString(3, student.getMajor());
            ps.setString(4, student.getUsername());

            int studentRows = ps.executeUpdate();
            if (studentRows == 0) {
                conn.rollback();
                return false;
            }

            // 更新用户密码
            String userSql = "UPDATE users SET password = ? WHERE username = ?";
            ps = conn.prepareStatement(userSql);
            ps.setString(1, student.getPassword());
            ps.setString(2, student.getUsername());

            int userRows = ps.executeUpdate();
            if (userRows == 0) {
                conn.rollback();
                return false;
            }

            conn.commit(); // 提交事务
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DBUtil.close(conn, ps, null);
        }
    }
}