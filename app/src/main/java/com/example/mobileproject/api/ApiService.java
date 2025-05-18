package com.example.mobileproject.api;

import com.example.mobileproject.model.CourseResponse;
import com.example.mobileproject.model.PagedResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    // Lấy danh sách khóa học nổi bật
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
}