package com.serbi.checkmate.util.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.serbi.checkmate.util.NotificationHandler;

public class ReminderWorker extends Worker {
    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data retrievedDate = getInputData();
        int taskId = retrievedDate.getInt("TASK_ID", -1);
        String taskName = retrievedDate.getString("TASK_NAME");

        NotificationHandler.showNotification(getApplicationContext(), taskId, taskName);
        return Result.success();
    }
}