// app/src/main/java/com/example/mobileproject/api/ApiService.java
package com.example.mobileproject.api;

import com.example.mobileproject.model.ChangePassword;
import com.example.mobileproject.model.Comment;
import com.example.mobileproject.model.Course;
import com.example.mobileproject.model.CourseCreateRequest;
import com.example.mobileproject.model.CourseResponse;
import com.example.mobileproject.model.Enrollment;
import com.example.mobileproject.model.FCMTokenRequest;
import com.example.mobileproject.model.FCMTokenResponse;
import com.example.mobileproject.model.Lesson;
import com.example.mobileproject.model.LessonCreateRequest;
import com.example.mobileproject.model.NotificationCreate;
import com.example.mobileproject.model.NotificationModel;
import com.example.mobileproject.model.PagedResponse;
import com.example.mobileproject.model.PaginatedCommentsResponse;
import com.example.mobileproject.model.PaginatedReviewsResponse;
import com.example.mobileproject.model.Review;
import com.example.mobileproject.model.ScoreResponse;
import com.example.mobileproject.model.User;
import com.example.mobileproject.model.WishlistRequest;
import com.example.mobileproject.model.WishlistResponse;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("courses/{id}")
    Call<Course> getCourseById(@Path("id") int id);

    @GET("lessons/{id}")
    Call<Lesson> getLessonById(@Path("id") int id);

    @GET("courses/{courseId}/lessons")
    Call<List<Lesson>> getLessonsByCourseId(@Path("courseId") int courseId);

    @GET("courses/{courseId}/reviews")
    Call<PaginatedReviewsResponse> getReviewsByCourseId(
            @Path("courseId") int courseId,
            @Query("page") int page,
            @Query("size") int size
    );

    @GET("lessons/{lessonId}/comments")
    Call<PaginatedCommentsResponse> getCommentsByLessonId(
            @Path("lessonId") int lessonId,
            @Query("page") int page,
            @Query("size") int size
    );

    @POST("reviews")
    Call<Review> addReview(@Body Review review);

    @POST("comments")
    Call<Comment> addComment(@Body Comment comment);

    @GET("courses/{courseId}/users/{userId}/enrollment")
    Call<Boolean> checkEnrollment(@Path("courseId") int courseId, @Path("userId") int userId);

    @GET("api/courses/top")
    Call<List<CourseResponse>> getTopCourses();

    // Lấy chi tiết một khóa học
    @GET("api/courses/{course_id}")
    Call<CourseResponse> getCourseById(@Path("course_id") String courseId);

    // API chung: lấy danh sách khóa học, có thể tìm kiếm, lọc theo danh mục và phân trang
    @GET("api/courses")
    Call<PagedResponse<CourseResponse>> getCourses(
            @Query("page") int page,
            @Query("page_size") int pageSize,
            @Query("category") String category,
            @Query("query") String query);

    //phần api của quân
    @POST("auth/login")
    Call<User> login(@Body User user);

    @POST("auth/google")
    Call<User> googleLogin(@Body User user);

    @POST("auth/register")
    Call<User> register(@Body User user);

    @POST("password-recovery/check-phone")
    Call<ResponseBody> checkPhone(@Body User user);

    @POST("password-recovery/reset")
    Call<ResponseBody> resetPassword(@Body User user);

    @POST("change-password")
    Call<ResponseBody> changePassword(@Body ChangePassword res);

    @POST("update-profile")
    Call<ResponseBody> updateProfile(@Body User user);

    @GET("api/scores")
    Call<PagedResponse<ScoreResponse>> getScores(
            @Query("user_id") int userId,
            @Query("page") int page,
            @Query("page_size") int pageSize,
            @Query("query") String query
    );

    @POST("courses/{courseId}/enrollments")
    Call<Enrollment> enrollInCourse(@Path("courseId") int courseId, @Body Map<String, Integer> requestBody);

    @GET("courses/{courseId}/enrollment-count")
    Call<Integer> getEnrollmentCount(@Path("courseId") int courseId);

    @GET("users/{userId}/notifications")
    Call<List<NotificationModel>> getUserNotifications(@Path("userId") int userId);

    @POST("users/{userId}/notifications/{notificationId}/read")
    Call<Void> markNotificationAsRead(@Path("userId") int userId, @Path("notificationId") int notificationId);

    @POST("users/{userId}/fcm-token")
    Call<FCMTokenResponse> updateFCMToken(
            @Path("userId") int userId,
            @Body FCMTokenRequest request
    );

    @GET("users/{userId}/wishlists")
    Call<List<CourseResponse>> getUserWishlists(@Path("userId") int userId);

    @GET("users/{userId}/enrollments")
    Call<List<CourseResponse>> getUserEnrollments(@Path("userId") int userId);

    @POST("wishlists/add")
    Call<WishlistResponse> addToWishlist(@Body WishlistRequest request);

    @POST("wishlists/remove")
    Call<Void> removeFromWishlist(@Body WishlistRequest request);

    @GET("wishlists/check")
    Call<Boolean> checkWishlist(@Query("userId") int userId, @Query("courseId") int courseId);
    @POST("notifications/create-for-users")
    Call<NotificationModel> createNotificationForUsers(@Body NotificationCreate notification);

    // Course Management APIs for Instructors
    @GET("api/instructors/{instructorId}/courses")
    Call<List<CourseResponse>> getInstructorCourses(@Path("instructorId") int instructorId);

    @POST("api/courses")
    Call<CourseResponse> createCourse(@Body CourseCreateRequest request);

    @PUT("api/courses/{courseId}")
    Call<CourseResponse> updateCourse(@Path("courseId") int courseId, @Body CourseCreateRequest request);

    @DELETE("api/courses/{courseId}")
    Call<Void> deleteCourse(@Path("courseId") int courseId);

    // Lesson Management APIs for Instructors
    @POST("api/lessons")
    Call<Lesson> createLesson(@Body LessonCreateRequest request);

    @PUT("api/lessons/{lessonId}")
    Call<Lesson> updateLesson(@Path("lessonId") int lessonId, @Body LessonCreateRequest request);

    @DELETE("api/lessons/{lessonId}")
    Call<Void> deleteLesson(@Path("lessonId") int lessonId);
}