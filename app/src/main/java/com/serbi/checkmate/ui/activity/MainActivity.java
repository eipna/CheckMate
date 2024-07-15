package com.serbi.checkmate.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.serbi.checkmate.R;
import com.serbi.checkmate.data.local.Database;
import com.serbi.checkmate.data.model.TaskModel;
import com.serbi.checkmate.ui.adapter.TaskAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String prefsTheme;

    private ArrayList<TaskModel> taskModels;

    private Database database;
    private TaskAdapter adapter;

    private RecyclerView rv_main;
    private FloatingActionButton btn_add_task;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeComponents();
        initializeSharedPreferences();
        initializeToolbar();
        initializeDatasets();
        displayTaskItems();

        btn_add_task.setOnClickListener(v -> {
            Intent createTaskIntent = new Intent(MainActivity.this, CreateTaskActivity.class);
            startActivity(createTaskIntent);
        });
    }

    private void initializeComponents() {
        sharedPreferences = getSharedPreferences("MINDCHECK", MODE_PRIVATE);
        prefsTheme = sharedPreferences.getString("THEME", "System");

        database = new Database(this);
        taskModels = new ArrayList<>();
        rv_main = findViewById(R.id.rv_main);
        toolbar = findViewById(R.id.tb_main);
        btn_add_task = findViewById(R.id.fba_add_task);
    }

    private void initializeToolbar() {
        setSupportActionBar(toolbar);
    }

    private void initializeDatasets() {
        Cursor cursor = database.getTaskItems();
        if (cursor.getCount() == 0) {
            // Indicate to user that task items are empty
        } else {
            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                String notes = cursor.getString(1);
                int isCompleted = cursor.getInt(2);

                taskModels.add(new TaskModel(
                        name,
                        notes,
                        isCompleted
                ));
            }
        }
    }

    private void displayTaskItems() {
        adapter = new TaskAdapter(MainActivity.this, taskModels);
        rv_main.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        rv_main.setAdapter(adapter);
    }

    private void initializeSharedPreferences() {
        // Handles the application theme
        switch (prefsTheme) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.options_settings) {
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
        return true;
    }
}