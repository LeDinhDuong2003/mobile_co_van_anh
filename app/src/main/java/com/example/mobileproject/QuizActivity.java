package com.example.mobileproject;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class QuizActivity extends AppCompatActivity {

    private TextView questionText, resultText, questionNumberText;
    private Button[] options = new Button[4];
    private ProgressBar timeProgress;
    private ValueAnimator progressAnimator;
    private MediaPlayer correctSound, wrongSound;
    private int currentQuestion = 0;
    private int score = 0;
    private boolean answered = false;

    private String[] questions = {
            "Hình vuông có cạnh 2 cm có diện tích là:",
            "Hình vuông có cạnh 3 cm có diện tích là:"
    };

    private String[][] optionsText = {
            {"2 cm", "4 dm²", "2 dm²", "4 cm²"},
            {"9 cm²", "6 dm²", "3 dm²", "9 dm²"}
    };

    private int[] correctAnswers = {3, 0}; // Index đúng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionNumberText = findViewById(R.id.questionNumber);
        questionText = findViewById(R.id.questionText);
        resultText = findViewById(R.id.resultText);
        timeProgress = findViewById(R.id.timeProgress);

        options[0] = findViewById(R.id.option1);
        options[1] = findViewById(R.id.option2);
        options[2] = findViewById(R.id.option3);
        options[3] = findViewById(R.id.option4);

        correctSound = MediaPlayer.create(this, R.raw.correct);
        wrongSound = MediaPlayer.create(this, R.raw.wrong);

        for (int i = 0; i < options.length; i++) {
            int index = i;
            options[i].setOnClickListener(v -> {
                if (!answered) {
                    answered = true;
                    checkAnswer(index);
                }
            });
        }

        loadQuestion();
    }

    private void loadQuestion() {
        if (currentQuestion >= questions.length) return;

        answered = false;
        questionText.setText(questions[currentQuestion]);
        resultText.setText("");
        resultText.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));

        for (int i = 0; i < 4; i++) {
            options[i].setText(optionsText[currentQuestion][i]);
            options[i].setVisibility(View.VISIBLE);
            options[i].setEnabled(true);
            options[i].setAlpha(1f);
            options[i].setBackgroundResource(getOptionBackground(i));
            options[i].setBackgroundTintList(null); // 💥 Reset lại tint màu để không bị giữ màu cũ
        }
        questionNumberText.setText("Câu " + (currentQuestion + 1) + "/" + questions.length);
        startTimer();
    }


    private void startTimer() {
        timeProgress.setMax(10000);
        timeProgress.setProgress(10000);

        if (progressAnimator != null) progressAnimator.cancel();

        progressAnimator = ValueAnimator.ofInt(10000, 0);
        progressAnimator.setDuration(10000);
        progressAnimator.addUpdateListener(animation -> {
            int progress = (int) animation.getAnimatedValue();
            timeProgress.setProgress(progress);
        });
        progressAnimator.addListener(new android.animation.Animator.AnimatorListener() {
            @Override public void onAnimationStart(android.animation.Animator animation) {}
            @Override public void onAnimationEnd(android.animation.Animator animation) {
                if (!answered) {
                    answered = true;
                    showCorrectAnswer();
                }
            }
            @Override public void onAnimationCancel(android.animation.Animator animation) {}
            @Override public void onAnimationRepeat(android.animation.Animator animation) {}
        });
        progressAnimator.start();
    }

    private void checkAnswer(int selectedIndex) {
        if (progressAnimator != null) progressAnimator.cancel();

        int correctIndex = correctAnswers[currentQuestion];

        for (int i = 0; i < 4; i++) {
            options[i].setEnabled(false);
            if (i != selectedIndex && i != correctIndex) {
                options[i].setAlpha(0.5f); // mờ
            }
        }

        if (selectedIndex == correctIndex) {
            score++;
            options[correctIndex].setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.holo_green_light));
            resultText.setText(boldText("\u2713 Đúng"));
            resultText.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_green_light));
            if (correctSound != null) {
                correctSound.seekTo(0);
                correctSound.start();
            }
        } else {
            options[selectedIndex].setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.holo_red_light));
            options[correctIndex].setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.holo_green_light));
            resultText.setText(boldText("\u2717 Sai"));
            resultText.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
            if (wrongSound != null) {
                wrongSound.seekTo(0);
                wrongSound.start();
            }
        }

        nextAfterDelay();
    }

    private void showCorrectAnswer() {
        int correctIndex = correctAnswers[currentQuestion];

        for (int i = 0; i < 4; i++) {
            options[i].setEnabled(false);
            if (i != correctIndex) {
                options[i].setAlpha(0.5f); // làm mờ đáp án sai
            }
        }

        options[correctIndex].setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.holo_green_light));
        resultText.setText(boldText("\u2717 Sai"));
        resultText.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_light));

        if (wrongSound != null) {
            wrongSound.seekTo(0);
            wrongSound.start();
        }

        nextAfterDelay();
    }

    private void nextAfterDelay() {
        new Handler().postDelayed(() -> {
            if (currentQuestion + 1 >= questions.length) {
                Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                intent.putExtra("score", score);
                intent.putExtra("total", questions.length);
                startActivity(intent);
                finish();
            } else {
                currentQuestion++;
                loadQuestion();
            }
        }, 4000);
    }

    private SpannableString boldText(String text) {
        SpannableString span = new SpannableString(text);
        span.setSpan(new StyleSpan(Typeface.BOLD), 0, 1, 0);
        return span;
    }

    private int getOptionBackground(int index) {
        switch (index) {
            case 0: return R.drawable.option1_background;
            case 1: return R.drawable.option2_background;
            case 2: return R.drawable.option3_background;
            case 3: return R.drawable.option4_background;
            default: return android.R.color.darker_gray;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressAnimator != null) progressAnimator.cancel();
        if (correctSound != null) correctSound.release();
        if (wrongSound != null) wrongSound.release();
    }
}
