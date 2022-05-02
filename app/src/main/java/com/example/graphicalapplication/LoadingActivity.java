package com.example.graphicalapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class LoadingActivity extends AppCompatActivity {
    int counter = 0;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        startLoading();
    }

    private void startLoading() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                counter++;
                progressBar.setProgress(counter);

                if (counter >= 100) {
                    timer.cancel();

                    Intent intent = new Intent(LoadingActivity.this, GamingActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 100, 50);
    }
}