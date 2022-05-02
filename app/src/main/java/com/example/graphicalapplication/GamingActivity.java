package com.example.graphicalapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class GamingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameView(this));
    }
}