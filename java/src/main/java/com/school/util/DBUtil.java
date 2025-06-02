package com.school.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库连接工具类
 * 此类提供了数据库连接的获取和关闭数据库资源的方法，用于简化数据库操作。
 */
public class DBUtil {
    // 数据库连接参数
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver"; // JDBC驱动类名
    private static final String URL = "jdbc:mysql://localhost:3306/student_course_db?useSSL=false&serverTimezone=UTC"; // 数据库URL
    private static final String USERNAME = "root"; // 数据库用户名
    private static final String PASSWORD = "root"; // 数据库密码

    // 类加载时加载JDBC驱动
    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("加载数据库驱动失败", e);
        }
    }

    /**
     * 获取数据库连接
     * @return Connection对象，表示与数据库的连接
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD); // 通过驱动管理器获取数据库连接
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("连接数据库失败", e);
        }
        return conn;
    }

    /**
     * 关闭数据库资源
     * 关闭ResultSet、PreparedStatement和Connection对象，释放数据库资源。
     * @param conn Connection对象
     * @param ps PreparedStatement对象
     * @param rs ResultSet对象
     */
    public static void close(Connection conn, PreparedStatement ps, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close(); // 关闭ResultSet
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (ps != null) {
            try {
                ps.close(); // 关闭PreparedStatement
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conn != null) {
            try {
                conn.close(); // 关闭Connection
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭连接和语句
     * 调用close方法，关闭Connection和PreparedStatement，但不关闭ResultSet（因为此时没有ResultSet）。
     * @param conn Connection对象
     * @param ps PreparedStatement对象
     */
    public static void close(Connection conn, PreparedStatement ps) {
        close(conn, ps, null); // 调用带有ResultSet参数的close方法，传入null作为ResultSet参数
    }
}