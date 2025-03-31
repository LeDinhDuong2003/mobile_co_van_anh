package com.example.mobileproject;

public class Lesson {
    private String number;       // Lesson number (e.g., "01")
    private String title;        // Lesson title (e.g., "Welcome to the Course")
    private String duration;     // Lesson duration (e.g., "6:10 mins")
    private int thumbnail;       // Thumbnail resource ID for the first page
    private int iconResId;       // Play/pause/lock icon for the second page
    private boolean isCompleted; // Completion status for the second page

    public Lesson(String number, String title, String duration, int thumbnail, int iconResId, boolean isCompleted) {
        this.number = number;
        this.title = title;
        this.duration = duration;
        this.thumbnail = thumbnail;
        this.iconResId = iconResId;
        this.isCompleted = isCompleted;
    }

    public String getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return duration;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public int getIconResId() {
        return iconResId;
    }

    public boolean isCompleted() {
        return isCompleted;
    }
}