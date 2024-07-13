package com.serbi.checkmate.services;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.service.quicksettings.TileService;

public class CreateTaskTileService extends TileService {

    private final String APPLICATION_PACKAGE = "com.serbi.checkmate";
    private final String CREATE_TASK_ACTIVITY_CLASS = "com.serbi.checkmate.activities.CreateTaskActivity";

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

    @Override
    public void onClick() {
        Intent createTaskIntent = new Intent();
        createTaskIntent.setClassName(APPLICATION_PACKAGE, CREATE_TASK_ACTIVITY_CLASS);
        createTaskIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingCreateTaskIntent = PendingIntent.getActivity(
                this,
                0,
                createTaskIntent,
                PendingIntent.FLAG_IMMUTABLE
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startActivityAndCollapse(pendingCreateTaskIntent);
        }
        super.onClick();
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
    }
}