package com.example.mobileproject;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.List;

public class CourseDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_playing);

//         Set up TabLayout and ViewPager2
//        TabLayout tabLayout = findViewById(R.id.tab_layout);
//        ViewPager2 viewPager = findViewById(R.id.view_pager);
//
//        // Create fragments for tabs
//        List<String> tabTitles = new ArrayList<>();
//        tabTitles.add("Overview");
//        tabTitles.add("Lessons");
//
//        // Set up the adapter for ViewPager2
//        TabPagerAdapter adapter = new TabPagerAdapter(this);
//        viewPager.setAdapter(adapter);
//
//        // Connect TabLayout with ViewPager2
//        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
//            tab.setText(tabTitles.get(position));
//        }).attach();

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheet));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        // Điều khiển hiển thị nội dung khi kéo lên/xuống
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                View fullContent = findViewById(R.id.fullContent);
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    fullContent.setVisibility(View.VISIBLE);
                } else {
                    fullContent.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {}
        });

        RecyclerView recyclerView = findViewById(R.id.lessonPlayingRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson("Lesson 1", "Introduction to comics drawing course","6min", R.drawable.baseline_play_lesson_24, 1, true));
        lessons.add(new Lesson("Lesson 2", "Nulia sit maurius nunc of suscipt", "3min", R.drawable.baseline_play_lesson_24,1,true));
        lessons.add(new Lesson("Lesson 3", "Tellus elementum jonys commodo nibh", "5min",  R.drawable.baseline_play_lesson_24,1,false));
        LessonAdapter adapter = new LessonAdapter(lessons, LessonAdapter.TYPE_PAGE_2);
        recyclerView.setAdapter(adapter);
    }
}