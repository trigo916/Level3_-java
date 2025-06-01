package com.school.entity;

/**
 * Course entity class
 */
public class Course {
    private int id;
    private String courseId;
    private String name;
    private String description;
    private int credit;
    private int maxStudents;
    private int currentStudents;

    public Course() {
    }

    public Course(int id, String courseId, String name, String description, int credit, int maxStudents) {
        this.id = id;
        this.courseId = courseId;
        this.name = name;
        this.description = description;
        this.credit = credit;
        this.maxStudents = maxStudents;
        this.currentStudents = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public int getCurrentStudents() {
        return currentStudents;
    }

    public void setCurrentStudents(int currentStudents) {
        this.currentStudents = currentStudents;
    }
    
    public boolean isAvailable() {
        return currentStudents < maxStudents;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseId='" + courseId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", credit=" + credit +
                ", maxStudents=" + maxStudents +
                ", currentStudents=" + currentStudents +
                '}';
    }
} 