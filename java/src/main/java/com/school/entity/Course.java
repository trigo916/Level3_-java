package com.school.entity;

/**
 * 课程实体类
 * 此类用于表示课程的基本信息，包括课程ID、名称、教师、学分、时间表、地点和状态等。
 */
public class Course {
    private int id; // 课程数据库记录的唯一标识符
    private String courseId; // 课程编号
    private String courseName; // 课程名称
    private String teacherName; // 授课教师姓名
    private int credits; // 课程学分
    private String schedule; // 课程时间表
    private String location; // 课程地点
    private String status; // 课程状态
    private int studentId; // 注册该课程的学生id

    public Course() {
    }

    // 以下是各个属性的getter和setter方法

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

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    @Override
    public String toString() {
        // 返回课程对象的字符串表示，方便打印和调试
        return "Course{" +
                "id=" + id +
                ", courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", credits=" + credits +
                ", schedule='" + schedule + '\'' +
                ", location='" + location + '\'' +
                ", status='" + status + '\'' +
                ", studentId=" + studentId +
                '}';
    }
}