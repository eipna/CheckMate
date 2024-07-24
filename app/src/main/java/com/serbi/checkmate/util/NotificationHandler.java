package com.serbi.checkmate.util;

import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import com.serbi.checkmate.R;

public class NotificationHandler {

    public static final String CHANNEL_REMINDER_ID = "reminder_channel";
    public static final String CHANNEL_REMINDER_NAME = "Reminders";
    public static final String CHANNEL_REMINDER_DESCRIPTION = "Handles reminders";

    public static void showNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_REMINDER_ID)
                .setContentTitle("Sample Notification Title")
                .setContentText("Sample Notification Description")
                .setSmallIcon(R.drawable.date)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        notificationManager.notify(0, builder.build());
    }
}