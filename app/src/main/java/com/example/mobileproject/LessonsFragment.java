package com.example.mobileproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class LessonsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout từ XML
        View view = inflater.inflate(R.layout.fragment_lessons, container, false);

        // Tìm RecyclerView từ XML
        RecyclerView recyclerView = view.findViewById(R.id.lessonsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Sample lesson data
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson("Lesson 1", "Introduction to comics drawing course","6min", R.drawable.baseline_play_lesson_24, 1, true));
        lessons.add(new Lesson("Lesson 2", "Nulia sit maurius nunc of suscipt", "3min", R.drawable.baseline_play_lesson_24,1,true));
        lessons.add(new Lesson("Lesson 3", "Tellus elementum jonys commodo nibh", "5min",  R.drawable.baseline_play_lesson_24,1,false));

        // Set up adapter
        LessonAdapter adapter = new LessonAdapter(lessons,LessonAdapter.TYPE_PAGE_1);
        recyclerView.setAdapter(adapter);

        return view;  // Trả về layout có chứa RecyclerView
    }
}
