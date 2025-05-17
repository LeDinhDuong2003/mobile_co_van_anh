// app/src/main/java/com/example/mobileproject/adapter/TabPagerAdapter.java
package com.example.mobileproject.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mobileproject.fragment.LessonsFragment;
import com.example.mobileproject.fragment.OverviewFragment;
import com.example.mobileproject.model.Course;

public class TabPagerAdapter extends FragmentStateAdapter {

    private final Course course;

    public TabPagerAdapter(@NonNull FragmentActivity fragmentActivity, Course course) {
        super(fragmentActivity);
        this.course = course;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return OverviewFragment.newInstance(course);
            case 1:
                return LessonsFragment.newInstance(course);
            default:
                return OverviewFragment.newInstance(course);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}