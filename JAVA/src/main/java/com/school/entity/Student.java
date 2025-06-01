package com.school.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Student entity class
 */
public class Student extends User {
    private String studentId;
    private String name;
    private int age;
    private String major;
    private List<Course> courses;

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

    public void addCourse(Course course) {
        this.courses.add(course);
    }

    public void removeCourse(Course course) {
        this.courses.remove(course);
    }

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
} 