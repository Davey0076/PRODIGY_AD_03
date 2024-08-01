package com.example.stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView minuteDisplay, secondDisplay, milliDisplay;
    private Button startButton, pauseButton, resetButton;

    private Handler handler = new Handler();
    private long startTime = 0L, timeInMilliseconds = 0L, timeSwapBuff = 0L, updatedTime = 0L;
    private boolean running = false;

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            if (running) {
                timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
                updatedTime = timeSwapBuff + timeInMilliseconds;

                int secs = (int) (updatedTime / 1000);
                int mins = secs / 60;
                secs %= 60;
                int milliseconds = (int) (updatedTime % 1000) / 10;

                minuteDisplay.setText(String.format("%02d", mins));
                secondDisplay.setText(String.format("%02d", secs));
                milliDisplay.setText(String.format("%02d", milliseconds));

                handler.postDelayed(this, 0);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        minuteDisplay = findViewById(R.id.minuteDisplay);
        secondDisplay = findViewById(R.id.secondDisplay);
        milliDisplay = findViewById(R.id.milliDisplay);
        startButton = findViewById(R.id.btnStart);
        pauseButton = findViewById(R.id.btnPause);
        resetButton = findViewById(R.id.btnReset);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!running) {
                    startTime = SystemClock.uptimeMillis();
                    handler.postDelayed(updateTimerThread, 0);
                    running = true;
                }
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (running) {
                    timeSwapBuff += timeInMilliseconds;
                    handler.removeCallbacks(updateTimerThread);
                    running = false;
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTime = 0L;
                timeInMilliseconds = 0L;
                timeSwapBuff = 0L;
                updatedTime = 0L;

                minuteDisplay.setText("00");
                secondDisplay.setText("00");
                milliDisplay.setText("00");

                if (running) {
                    handler.removeCallbacks(updateTimerThread);
                    running = false;
                }
            }
        });
    }
}
