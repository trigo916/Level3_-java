package com.school.entity;

/**
 * 用户实体类
 * 此类用于表示系统中用户的基本属性，包括用户ID、用户名、密码和角色。
 */
public class User {
    private int id; // 用户数据库记录的唯一标识符
    private String username; // 用户名
    private String password; // 密码
    private String role; // 用户角色

    public User() {
    }

    // 构造函数，初始化用户名和密码
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // 以下是各个属性的getter和setter方法

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // 重写toString方法，返回用户信息的字符串表示，方便打印和调试
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}