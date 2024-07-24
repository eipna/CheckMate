package com.serbi.checkmate;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.appcompat.app.AppCompatDelegate;

import com.serbi.checkmate.util.NotificationHandler;

public class CheckMateApplication extends Application {

    // Shared Preferences
    public static SharedPreferences preferences;
    public static String prefsApplicationTheme;

    // Task status
    public static final int TASK_COMPLETED = 1;
    public static final int TASK_NOT_COMPLETED = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel reminderChannel = new NotificationChannel(
                    NotificationHandler.CHANNEL_REMINDER_ID,
                    NotificationHandler.CHANNEL_REMINDER_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(reminderChannel);
        }

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