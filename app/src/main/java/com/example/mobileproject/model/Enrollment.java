package com.example.mobileproject.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Enrollment implements Serializable {
    private Integer enrollmentId;
    private Integer userId;
    private Integer courseId;
    private LocalDateTime enrolledAt;
    private Float progress;
    private User user;
    private Course course;

    // Constructor
    public Enrollment() {
    }

    // Getters and Setters
    public Integer getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(Integer enrollmentId) { this.enrollmentId = enrollmentId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }
    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }
    public Float getProgress() { return progress; }
    public void setProgress(Float progress) { this.progress = progress; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
}
