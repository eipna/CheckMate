package com.serbi.checkmate.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Credentials
    private Context context;
    private static final String DATABASE_NAME = "mindcheck.db";
    private static int DATABASE_VERSION = 1;

    // Database Tables
    private static final String TABLE_TASK = "tables";

    // Task Table Data
    private static final String TABLE_TASK_NAME = "name";
    private static final String TABLE_TASK_NOTES = "notes";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Table Query for Tasks
        String createTaskTable = "CREATE TABLE IF NOT EXISTS " + TABLE_TASK + "(" +
                TABLE_TASK_NAME + " TEXT," +
                TABLE_TASK_NOTES + " TEXT)";

        // Executes the queries for creating tables
        db.execSQL(createTaskTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drops all tables then recreates the whole database
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        onCreate(db);
    }
}
