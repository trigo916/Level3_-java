package com.school.dao;

import com.school.entity.Course;
import com.school.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程实体的数据访问对象（DAO）
 * 此类提供了与数据库交互以执行课程相关操作的方法。
 */
public class CourseDao {
    /**
     * 根据学生用户名查找课程
     * @param username 学生的用户名
     * @return 学生注册的课程列表
     */
    public List<Course> findCoursesByUsername(String username) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Course> courses = new ArrayList<>();

        try {
            // 建立数据库连接
            conn = DBUtil.getConnection();
            // SQL查询语句，根据学生用户名选择课程
            String sql = "SELECT c.id, c.course_id, c.course_name, c.teacher_name, c.credits, " +
                    "c.schedule, c.location, sc.status " +
                    "FROM courses c " +
                    "JOIN student_courses sc ON c.id = sc.course_id " +
                    "JOIN students s ON s.id = sc.student_id " +
                    "WHERE s.username = ? " +
                    "ORDER BY c.course_id";
            // 准备SQL语句
            ps = conn.prepareStatement(sql);
            // 设置用户名参数
            ps.setString(1, username);
            // 执行查询
            rs = ps.executeQuery();

            // 处理结果集
            while (rs.next()) {
                Course course = new Course();
                course.setId(rs.getInt("id"));
                course.setCourseId(rs.getString("course_id"));
                course.setCourseName(rs.getString("course_name"));
                course.setTeacherName(rs.getString("teacher_name"));
                course.setCredits(rs.getInt("credits"));
                course.setSchedule(rs.getString("schedule"));
                course.setLocation(rs.getString("location"));
                course.setStatus(rs.getString("status"));
                courses.add(course);
            }
        } catch (SQLException e) {
            // 处理SQL异常
            e.printStackTrace();
        } finally {
            // 关闭数据库资源
            DBUtil.close(conn, ps, rs);
        }

        return courses;
    }

    /**
     * 根据学生ID查找课程
     * @param studentId 学生的ID
     * @return 学生注册的课程列表
     */
    public List<Course> findCoursesByStudentId(int studentId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Course> courses = new ArrayList<>();

        try {
            // 建立数据库连接
            conn = DBUtil.getConnection();
            // SQL查询语句，根据学生ID选择课程
            String sql = "SELECT c.id, c.course_id, c.course_name, c.teacher_name, c.credits, " +
                    "c.schedule, c.location, sc.status " +
                    "FROM courses c " +
                    "JOIN student_courses sc ON c.id = sc.course_id " +
                    "WHERE sc.student_id = ? " +
                    "ORDER BY c.course_id";
            // 准备SQL语句
            ps = conn.prepareStatement(sql);
            // 设置学生ID参数
            ps.setInt(1, studentId);
            // 执行查询
            rs = ps.executeQuery();

            // 处理结果集
            while (rs.next()) {
                Course course = new Course();
                course.setId(rs.getInt("id"));
                course.setCourseId(rs.getString("course_id"));
                course.setCourseName(rs.getString("course_name"));
                course.setTeacherName(rs.getString("teacher_name"));
                course.setCredits(rs.getInt("credits"));
                course.setSchedule(rs.getString("schedule"));
                course.setLocation(rs.getString("location"));
                course.setStatus(rs.getString("status"));
                courses.add(course);
            }
        } catch (SQLException e) {
            // 处理SQL异常
            e.printStackTrace();
        } finally {
            // 关闭数据库资源
            DBUtil.close(conn, ps, rs);
        }

        return course;
    }
}