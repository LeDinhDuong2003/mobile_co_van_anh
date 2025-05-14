package com.example.mobileproject.model;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

public class Lesson {
    private Integer lessonId;
    private Integer courseId;
    private String title;
    private String videoUrl;
    private Integer duration;
    private Integer position;
    private Course course;
    private List<Comment> comments = new ArrayList<>();
    private List<Quiz> quizzes = new ArrayList<>();

    // Constructor
    public Lesson() {}

    // Getters and Setters
    public Integer getLessonId() { return lessonId; }
    public void setLessonId(Integer lessonId) { this.lessonId = lessonId; }
    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }
    public List<Quiz> getQuizzes() { return quizzes; }
    public void setQuizzes(List<Quiz> quizzes) { this.quizzes = quizzes; }
}