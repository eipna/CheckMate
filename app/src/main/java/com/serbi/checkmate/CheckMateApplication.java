package com.serbi.checkmate;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class CheckMateApplication extends Application {

    public static SharedPreferences preferences;
    public static String prefsApplicationTheme;

    @Override
    public void onCreate() {
        super.onCreate();

        preferences = getSharedPreferences("MINDCHECK", MODE_PRIVATE);
        prefsApplicationTheme = preferences.getString("THEME", "System");

        // Handles the application theme
        switch (prefsApplicationTheme) {
            case "System":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case "Light":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "Dark":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }
    }
}