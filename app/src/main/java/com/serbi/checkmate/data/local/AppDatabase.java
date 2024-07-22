package com.serbi.checkmate.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.serbi.checkmate.Constants;
import com.serbi.checkmate.data.model.TaskModel;

import java.util.ArrayList;

public class AppDatabase extends SQLiteOpenHelper {

    // Database Credentials
    private static final String DATABASE_NAME = "mindcheck.db";
    private static final int DATABASE_VERSION = 1;

    // Database Tables
    private static final String TABLE_TASK = "tasks";

    // Task Table Columns
    private static final String TABLE_TASK_ID = "task_id";
    private static final String TABLE_TASK_NAME = "name";
    private static final String TABLE_TASK_NOTES = "notes";
    private static final String TABLE_TASK_IS_COMPLETED = "is_completed";

    public AppDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Table Query for Tasks
        String createTaskTable = "CREATE TABLE IF NOT EXISTS " + TABLE_TASK + "(" +
                TABLE_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TABLE_TASK_NAME + " TEXT," +
                TABLE_TASK_NOTES + " TEXT," +
                TABLE_TASK_IS_COMPLETED + " INTEGER DEFAULT 0)";

        // Executes the queries for creating tables
        db.execSQL(createTaskTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drops all tables then recreates the whole database
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        onCreate(db);
    }

    // Creates a new task
    public void createTask(String name, String notes) {
        ContentValues values = new ContentValues();
        values.put(TABLE_TASK_NAME, name);
        values.put(TABLE_TASK_NOTES, notes);
        getWritableDatabase().insert(TABLE_TASK, null, values);
        close();
    }

    // Retrieves all task items based if completed or not
    public ArrayList<TaskModel> getTaskItems(int completed) {
        ArrayList<TaskModel> taskItems = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_TASK + " WHERE " + TABLE_TASK_IS_COMPLETED + " = " + completed;
        Cursor cursor = getReadableDatabase().rawQuery(query, null);

        while (cursor.moveToNext()) {
            taskItems.add(new TaskModel(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3)
            ));
        }
        cursor.close();
        return taskItems;
    }

    // Toggle's a task's is_completed value to be completed(1) or not(0)
    public void toggleTask(int id, boolean status) {
        ContentValues values = new ContentValues();
        if (status) {
            values.put(TABLE_TASK_IS_COMPLETED, 1);
        } else {
            values.put(TABLE_TASK_IS_COMPLETED, 0);
        }
        getWritableDatabase().update(TABLE_TASK, values, TABLE_TASK_ID + " = ?", new String[]{String.valueOf(id)});
        close();
    }

    // Updates a task
    public void editTask(int id, String name, String notes) {
        ContentValues values = new ContentValues();
        values.put(TABLE_TASK_NAME, name);
        values.put(TABLE_TASK_NOTES, notes);
        getWritableDatabase().update(TABLE_TASK, values, TABLE_TASK_ID + " = ?", new String[]{String.valueOf(id)});
        close();
    }

    // Deletes a task
    public void deleteTask(int id) {
        getWritableDatabase().delete(TABLE_TASK, TABLE_TASK_ID + " = ?", new String[]{String.valueOf(id)});
        close();
    }

    // Clears all completed tasks
    public void clearCompletedTasks() {
        getWritableDatabase().delete(TABLE_TASK, TABLE_TASK_IS_COMPLETED + " = ?", new String[]{String.valueOf(Constants.TASK_COMPLETED)});
        close();
    }
}