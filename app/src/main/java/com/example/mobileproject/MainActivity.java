// app/src/main/java/com/example/mobileproject/MainActivity.java
package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, CourseDetailActivity.class);
        intent.putExtra("courseId", 1);
        startActivity(intent);
    }
}