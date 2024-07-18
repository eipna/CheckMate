package com.serbi.checkmate.services;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.service.quicksettings.TileService;

import com.serbi.checkmate.ui.activity.CreateTaskActivity;

public class CreateTaskTileService extends TileService {

    @Override
    public void onTileAdded() {
        super.onTileAdded();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
    }

    @SuppressLint("StartActivityAndCollapseDeprecated")
    @Override
    public void onClick() {
        super.onClick();

        Intent intent = new Intent(this, CreateTaskActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setAction(Intent.ACTION_MAIN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            int flags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, flags);
            startActivityAndCollapse(pendingIntent);
        } else {
            startActivityAndCollapse(intent);
        }
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
    }
}