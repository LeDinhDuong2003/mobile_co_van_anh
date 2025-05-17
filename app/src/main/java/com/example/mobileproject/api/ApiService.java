// app/src/main/java/com/example/mobileproject/api/ApiService.java
package com.example.mobileproject.api;

import com.example.mobileproject.model.Comment;
import com.example.mobileproject.model.Course;
import com.example.mobileproject.model.Lesson;
import com.example.mobileproject.model.Review;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("courses/{id}")
    Call<Course> getCourseById(@Path("id") int id);

    @GET("lessons/{id}")
    Call<Lesson> getLessonById(@Path("id") int id);

    @GET("courses/{courseId}/lessons")
    Call<List<Lesson>> getLessonsByCourseId(@Path("courseId") int courseId);

    @GET("lessons/{lessonId}/comments")
    Call<List<Comment>> getCommentsByLessonId(@Path("lessonId") int lessonId);

    @POST("reviews")
    Call<Review> addReview(@Body Review review);

    @POST("comments")
    Call<Comment> addComment(@Body Comment comment);

    @GET("courses/{courseId}/users/{userId}/enrollment")
    Call<Boolean> checkEnrollment(@Path("courseId") int courseId, @Path("userId") int userId);
}