// app/src/main/java/com/example/mobileproject/fragment/LessonsFragment.java
package com.example.mobileproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.R;
import com.example.mobileproject.VideoPlayerActivity;
import com.example.mobileproject.adapter.LessonAdapter;
import com.example.mobileproject.model.Course;

public class LessonsFragment extends Fragment {

    private Course course;

    public static LessonsFragment newInstance(Course course) {
        LessonsFragment fragment = new LessonsFragment();
        Bundle args = new Bundle();
        args.putSerializable("course", course);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            course = (Course) getArguments().getSerializable("course");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lessons, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.lessonsRecyclerView);
        if (recyclerView == null) {
            Log.e("LessonsFragment", "lessonsRecyclerView not found");
            TextView errorText = new TextView(getContext());
            errorText.setText("Error: RecyclerView not found");
            errorText.setTextColor(0xFFFFFFFF);
            errorText.setTextSize(16);
            return errorText;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (course == null || course.getLessons() == null) {
            Log.e("LessonsFragment", "Course or lessons is null");
            TextView errorText = new TextView(getContext());
            errorText.setText("No lessons available");
            errorText.setTextColor(0xFFFFFFFF);
            errorText.setTextSize(16);
            return errorText;
        }

        LessonAdapter adapter = new LessonAdapter(course.getLessons(), LessonAdapter.TYPE_PAGE_1, lesson -> {
            if (getActivity() == null) {
                Log.e("LessonsFragment", "getActivity() is null");
                Toast.makeText(getContext(), "Error: Activity not available", Toast.LENGTH_SHORT).show();
                return;
            }
            if (lesson == null || lesson.getLessonId() == null || lesson.getVideoUrl() == null) {
                Log.e("LessonsFragment", "Lesson or its fields are null");
                Toast.makeText(getContext(), "Error: Invalid lesson data", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
            intent.putExtra("lessonId", lesson.getLessonId());
            intent.putExtra("courseId", course.getCourseId());
            try {
                startActivity(intent);
            } catch (Exception e) {
                Log.e("LessonsFragment", "Error starting VideoPlayerActivity", e);
                Toast.makeText(getContext(), "Error opening video", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);

        return view;
    }
}