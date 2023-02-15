package com.supportivehands.salonmanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;

import com.supportivehands.salonmanagementapp.registeration.SelectionMode;

import java.util.ArrayList;
import java.util.Collections;

public class SplashScreen extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);
        new Handler().postDelayed(new Runnable(){
        @Override
        public void run() {
            Intent mainIntent = new Intent(SplashScreen.this, SelectionMode.class);
            startActivity(mainIntent);
            finish();
        }
    }, SPLASH_DISPLAY_LENGTH);

    }

}