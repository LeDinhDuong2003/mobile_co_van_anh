package com.example.mobileproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Lesson> lessons;
    public static final int TYPE_PAGE_1 = 1; // For the first page (thumbnail-based)
    public static final int TYPE_PAGE_2 = 2; // For the second page (course playing)
    private int layoutType;

    // Constructor accepts a layoutType to determine which layout to use
    public LessonAdapter(List<Lesson> lessons, int layoutType) {
        this.lessons = lessons;
        this.layoutType = layoutType;
    }

    @Override
    public int getItemViewType(int position) {
        return layoutType; // Return the layout type for this adapter instance
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_PAGE_1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson, parent, false);
            return new LessonViewHolderPage1(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson_playing, parent, false);
            return new LessonViewHolderPage2(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);
        if (holder instanceof LessonViewHolderPage1) {
            // Bind data for Page 1 (thumbnail-based)
            LessonViewHolderPage1 page1Holder = (LessonViewHolderPage1) holder;
            if (page1Holder.title != null) {
                page1Holder.title.setText(lesson.getTitle() != null ? lesson.getTitle() : "");
            }
            if (page1Holder.lessonNumber != null) {
                page1Holder.lessonNumber.setText(lesson.getNumber() != null ? lesson.getNumber() : "");
            }
            if (page1Holder.thumbnail != null && lesson.getThumbnail() != 0) {
                page1Holder.thumbnail.setImageResource(lesson.getThumbnail());
            }
        } else if (holder instanceof LessonViewHolderPage2) {
            // Bind data for Page 2 (course playing)
            LessonViewHolderPage2 page2Holder = (LessonViewHolderPage2) holder;
            if (page2Holder.lessonNumber != null) {
                page2Holder.lessonNumber.setText(lesson.getNumber() != null ? lesson.getNumber() : "");
            }
            if (page2Holder.lessonTitle != null) {
                page2Holder.lessonTitle.setText(lesson.getTitle() != null ? lesson.getTitle() : "");
            }
            if (page2Holder.lessonDuration != null) {
                page2Holder.lessonDuration.setText(lesson.getDuration() != null ? lesson.getDuration() : "");
            }
//            if (page2Holder.lessonIcon != null && lesson.getIconResId() != 0) {
//                page2Holder.lessonIcon.setImageResource(lesson.getIconResId());
//            }
        }
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    // ViewHolder for Page 1 (thumbnail-based layout)
    public static class LessonViewHolderPage1 extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title;
        TextView lessonNumber;

        public LessonViewHolderPage1(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.lesson_thumbnail);
            title = itemView.findViewById(R.id.lesson_title);
            lessonNumber = itemView.findViewById(R.id.lesson_number);
        }
    }

    // ViewHolder for Page 2 (course playing layout)
    public static class LessonViewHolderPage2 extends RecyclerView.ViewHolder {
        TextView lessonNumber, lessonTitle, lessonDuration;
        ImageView lessonIcon;

        public LessonViewHolderPage2(@NonNull View itemView) {
            super(itemView);
            lessonNumber = itemView.findViewById(R.id.lessonNumber);
            lessonTitle = itemView.findViewById(R.id.lessonTitle);
            lessonDuration = itemView.findViewById(R.id.lessonDuration);
            lessonIcon = itemView.findViewById(R.id.lessonIcon);
        }
    }
}