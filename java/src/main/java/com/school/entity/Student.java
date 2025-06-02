package com.school.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生实体类
 * 此类继承自User类，表示学生的基本信息，包括学号、姓名、年龄、专业以及所选课程列表。
 */
public class Student extends User {
    private String studentId; // 学号
    private String name; // 姓名
    private int age; // 年龄
    private String major; // 专业
    private List<Course> courses; // 学生所选课程列表

    // 无参构造函数，初始化courses为空的ArrayList
    public Student() {
        super();
        this.courses = new ArrayList<>();
    }

    public Student(String username, String password) {
        super(username, password);
        this.courses = new ArrayList<>();
    }

    public Student(int id, String username, String password, String role, String studentId, String name, int age, String major) {
        super(id, username, password, role);
        this.studentId = studentId;
        this.name = name;
        this.age = age;
        this.major = major;
        this.courses = new ArrayList<>();
    }

    // 以下是各个属性的getter和setter方法

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    // 向课程列表中添加一门课程
    public void addCourse(Course course) {
        this.courses.add(course);
    }

    // 从课程列表中移除一门课程
    public void removeCourse(Course course) {
        this.courses.remove(course);
    }

    // 重写toString方法，返回学生信息的字符串表示
    @Override
    public String toString() {
        return "Student{" +
                "id=" + getId() +
                ", studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", major='" + major + '\'' +
                ", username='" + getUsername() + '\'' +
                '}';
    }
}}