package com.school.dao;

import com.school.entity.User;
import com.school.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 用户实体的数据访问对象（DAO）
 * 此类负责与数据库进行交互，执行与用户相关的数据操作，如查询、验证、添加用户等。
 */
public class UserDao {
    /**
     * 根据用户名查找用户信息
     * @param username 用户名，用于在数据库中搜索对应的用户记录
     * @return 返回一个User对象，包含找到的用户信息；如果没有找到，返回null
     */
    public User findByUsername(String username) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;

        try {
            // 获取数据库连接
            conn = DBUtil.getConnection();
            // 准备SQL查询语句，查询users表中username字段匹配的记录
            String sql = "SELECT id, username, password, role FROM users WHERE username = ?";
            // 创建PreparedStatement对象
            ps = conn.prepareStatement(sql);
            // 设置查询参数
            ps.setString(1, username);
            // 执行查询并获取结果集
            rs = ps.executeQuery();

            // 检查结果集中是否有数据
            if (rs.next()) {
                // 如果有数据，创建User对象并填充属性
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
            }
        } catch (SQLException e) {
            // 打印SQL异常信息
            e.printStackTrace();
        } finally {
            // 关闭数据库资源，包括连接、PreparedStatement和ResultSet
            DBUtil.close(conn, ps, rs);
        }

        // 返回查询到的用户信息
        return user;
    }

    /**
     * 验证用户身份
     * @param username 用户名，用于在数据库中搜索对应的用户记录
     * @param password 密码，用于验证用户身份
     * @return 返回一个User对象，如果用户名和密码匹配则包含用户信息；如果不匹配，返回null
     */
    public User authenticate(String username, String password) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;

        try {
            // 获取数据库连接
            conn = DBUtil.getConnection();
            // 准备SQL查询语句，查询users表中username和password字段匹配的记录
            String sql = "SELECT id, username, password, role FROM users WHERE username = ? AND password = ?";
            // 创建PreparedStatement对象
            ps = conn.prepareStatement(sql);
            // 设置查询参数
            ps.setString(1, username);
            ps.setString(2, password);
            // 执行查询并获取结果集
            rs = ps.executeQuery();

            // 检查结果集中是否有数据
            if (rs.next()) {
                // 如果有数据，创建User对象并填充属性
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
            }
        } catch (SQLException e) {
            // 打印SQL异常信息
            e.printStackTrace();
        } finally {
            // 关闭数据库资源，包括连接、PreparedStatement和ResultSet
            DBUtil.close(conn, ps, rs);
        }

        // 返回验证通过的用户信息
        return user;
    }

    /**
     * 添加新用户到数据库
     * @param user 要添加的User对象，包含新用户的详细信息
     * @return 如果添加成功，返回true；如果添加失败，返回false
     */
    public boolean add(User user) {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean success = false;

        try {
            // 获取数据库连接
            conn = DBUtil.getConnection();
            // 准备SQL插入语句，向users表中插入新用户记录
            String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            // 创建PreparedStatement对象
            ps = conn.prepareStatement(sql);
            // 设置插入参数
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());

            // 执行插入操作
            int rows = ps.executeUpdate();
            // 检查是否成功插入了一行数据
            success = rows > 0;
        } catch (SQLException e) {
            // 打印SQL异常信息
            e.printStackTrace();
        } finally {
            // 关闭数据库资源，包括连接和PreparedStatement
            DBUtil.close(conn, ps);
        }

        // 返回添加操作的结果
        return success;
    }

    /**
     * 检查数据库中是否已存在指定的用户名
     * @param username 要检查的用户名
     * @return 如果用户名已存在，返回true；如果不存在，返回false
     */
    public boolean usernameExists(String username) {
        // 直接调用findByUsername方法，如果能找到用户，则说明用户名已存在
        return findByUsername(username) != null;
    }
}