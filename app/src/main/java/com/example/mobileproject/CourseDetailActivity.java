// app/src/main/java/com/example/mobileproject/CourseDetailActivity.java
package com.example.mobileproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mobileproject.adapter.ReviewAdapter;
import com.example.mobileproject.adapter.TabPagerAdapter;
import com.example.mobileproject.api.ApiService;
import com.example.mobileproject.api.RetrofitClient;
import com.example.mobileproject.model.Course;
import com.example.mobileproject.model.Lesson;
import com.example.mobileproject.model.Review;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CourseDetailActivity extends AppCompatActivity {
    private int courseId;
    private Course course;
    private boolean isEnrolled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        courseId = getIntent().getIntExtra("courseId", -1);
        if (courseId == -1) {
            Toast.makeText(this, "Invalid course ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ImageView backButton = findViewById(R.id.back_button);
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        checkEnrollmentStatus();
        fetchCourseData();
    }

    private void checkEnrollmentStatus() {
        Integer userId = MockAuthManager.getInstance().getCurrentUserId();
        if (userId == null) {
            isEnrolled = false;
            setupUI();
            Toast.makeText(this, "User data not available", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getClient();
        Call<Boolean> call = apiService.checkEnrollment(courseId, userId);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    isEnrolled = response.body();
                    setupUI();
                } else {
                    isEnrolled = false;
                    setupUI();
                    Toast.makeText(CourseDetailActivity.this, "Failed to check enrollment status", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("CourseDetailActivity", "Error checking enrollment", t);
                isEnrolled = false;
                setupUI();
                Toast.makeText(CourseDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchCourseData() {
        ApiService apiService = RetrofitClient.getClient();
        Call<Course> call = apiService.getCourseById(courseId);
        call.enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, Response<Course> response) {
                if (response.isSuccessful() && response.body() != null) {
                    course = response.body();
                    setupUI();
                } else {
                    Toast.makeText(CourseDetailActivity.this, "Failed to load course data", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {
                Log.e("CourseDetailActivity", "Error fetching course data", t);
                Toast.makeText(CourseDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setupUI() {
        if (course == null) {
            return;
        }

        ImageView headerImage = findViewById(R.id.header_image);
        ImageView instructorImage = findViewById(R.id.instructor_image);
        TextView instructorName = findViewById(R.id.instructor_name);
        TextView courseTitle = findViewById(R.id.course_title);
        TextView courseDuration = findViewById(R.id.course_duration);
        TextView lessonCount = findViewById(R.id.lesson_count);
        TextView courseRating = findViewById(R.id.course_rating);
        TextView studentCount = findViewById(R.id.student_count);
        TextView courseDescription = findViewById(R.id.course_description);

        if (headerImage != null) {
            headerImage.setImageResource(R.drawable.course_image);
        }
        if (instructorImage != null) {
            instructorImage.setImageResource(R.drawable.teacher);
        }
        if (instructorName != null) {
            instructorName.setText(course.getInstructor() != null ? course.getInstructor().getFullName() : "Unknown");
        }
        if (courseTitle != null) {
            courseTitle.setText(course.getTitle() != null ? course.getTitle() : "No Title");
        }
        if (courseDuration != null) {
            courseDuration.setText(formatDuration(course.getLessons()));
        }
        if (lessonCount != null) {
            lessonCount.setText((course.getLessons() != null ? course.getLessons().size() : 0) + " Lessons");
        }
        if (courseRating != null) {
            courseRating.setText(String.format("%.1f (%d)", calculateAverageRating(course.getReviews()),
                    course.getReviews() != null ? course.getReviews().size() : 0));
        }
        if (studentCount != null) {
            studentCount.setText((course.getUsers() != null ? course.getUsers().size() : 0) + " students");
        }
        if (courseDescription != null) {
            courseDescription.setText(course.getDescription() != null ? course.getDescription() : "No Description");
        }

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 viewPager = findViewById(R.id.view_pager);

        if (tabLayout != null && viewPager != null) {
            List<String> tabTitles = new ArrayList<>();
            tabTitles.add("Overview");
            tabTitles.add("Lessons");

            TabPagerAdapter adapter = new TabPagerAdapter(this, course);
            viewPager.setAdapter(adapter);

            new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(tabTitles.get(position))).attach();
        }

        RecyclerView reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView);
        if (reviewsRecyclerView != null) {
            reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            ReviewAdapter reviewAdapter = new ReviewAdapter(course.getReviews() != null ? course.getReviews() : new ArrayList<>());
            reviewsRecyclerView.setAdapter(reviewAdapter);
        }

        RatingBar ratingBar = findViewById(R.id.ratingBar);
        EditText reviewCommentInput = findViewById(R.id.reviewCommentInput);
        Button submitReviewButton = findViewById(R.id.submitReviewButton);

        if (submitReviewButton != null) {
            submitReviewButton.setOnClickListener(v -> {
                if (!isEnrolled) {
                    Toast.makeText(this, "You must enroll in the course to leave a review", Toast.LENGTH_SHORT).show();
                    return;
                }
                float rating = ratingBar != null ? ratingBar.getRating() : 0;
                String comment = reviewCommentInput != null ? reviewCommentInput.getText().toString().trim() : "";
                if (rating > 0 && !comment.isEmpty()) {
                    Integer userId = MockAuthManager.getInstance().getCurrentUserId();
                    if (userId == null) {
                        Toast.makeText(this, "User data not available", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Review newReview = new Review();
                    newReview.setRating((int) rating);
                    newReview.setComment(comment);
                    newReview.setCourseId(courseId);
                    newReview.setUserId(userId);
                    newReview.setCreatedAt(LocalDateTime.now());
                    newReview.setUser(MockAuthManager.getInstance().getCurrentUser());
                    addReviewToServer(newReview, reviewsRecyclerView != null ? (ReviewAdapter) reviewsRecyclerView.getAdapter() : null);
                } else {
                    Toast.makeText(this, "Please provide a rating and comment", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void addReviewToServer(Review review, ReviewAdapter adapter) {
        ApiService apiService = RetrofitClient.getClient();
        Call<Review> call = apiService.addReview(review);
        call.enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (course.getReviews() == null) {
                        course.setReviews(new ArrayList<>());
                    }
                    course.getReviews().add(response.body());
                    for(Review r: course.getReviews()){
                        System.out.println(r.getReviewId());
                    }
                    if (adapter != null) {
                        adapter.notifyItemInserted(course.getReviews().size() - 1);
                    }
                    EditText reviewCommentInput = findViewById(R.id.reviewCommentInput);
                    RatingBar ratingBar = findViewById(R.id.ratingBar);
                    if (reviewCommentInput != null) {
                        reviewCommentInput.setText("");
                    }
                    if (ratingBar != null) {
                        ratingBar.setRating(0);
                    }
                    Toast.makeText(CourseDetailActivity.this, "Review submitted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CourseDetailActivity.this, "Failed to submit review", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                Log.e("CourseDetailActivity", "Error adding review", t);
                Toast.makeText(CourseDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String formatDuration(List<Lesson> lessons) {
        int totalSeconds = 0;
        if (lessons != null) {
            for (Lesson lesson : lessons) {
                totalSeconds += lesson.getDuration() != null ? lesson.getDuration() : 0;
            }
        }
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d:%02d mins", minutes, seconds);
    }

    private float calculateAverageRating(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) return 0.0f;
        float sum = 0;
        for (Review review : reviews) {
            sum += review.getRating() != null ? review.getRating() : 0;
        }
        return sum / reviews.size();
    }
}