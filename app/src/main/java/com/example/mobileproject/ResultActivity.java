package com.example.mobileproject;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView resultText = findViewById(R.id.resultText);
        TextView scoreText = findViewById(R.id.scoreText);

        // Lấy điểm từ Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int score = extras.getInt("score", 0);
            int total = extras.getInt("total", 0);
            scoreText.setText(String.format("Điểm: %d/%d", score, total));
        }
    }
}