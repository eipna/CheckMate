package com.serbi.checkmate.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.serbi.checkmate.R;
import com.serbi.checkmate.data.interfaces.Sortable;
import com.serbi.checkmate.data.local.Database;
import com.serbi.checkmate.data.model.TaskModel;
import com.serbi.checkmate.ui.adapter.TaskAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Sortable {

    private SharedPreferences sharedPreferences;
    private String prefsTheme;

    private List<TaskModel> taskModels;

    private Database database;
    private TaskAdapter adapter;

    private ConstraintLayout empty_task_container;
    private RecyclerView rv_main;
    private FloatingActionButton btn_add_task;
    private MaterialToolbar toolbar;

    private static final int CREATE_TASK_REQUEST_CODE = 420;

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
            startActivityForResult(createTaskIntent, CREATE_TASK_REQUEST_CODE);
        });
    }

    private void initializeComponents() {
        sharedPreferences = getSharedPreferences("MINDCHECK", MODE_PRIVATE);
        prefsTheme = sharedPreferences.getString("THEME", "System");

        database = new Database(this);

        empty_task_container = findViewById(R.id.cl_empty_container);
        rv_main = findViewById(R.id.rv_main);
        toolbar = findViewById(R.id.tb_main);
        btn_add_task = findViewById(R.id.fba_add_task);
    }

    private void initializeToolbar() {
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // New task creation operation which updates the task list
        if (requestCode == CREATE_TASK_REQUEST_CODE && resultCode == RESULT_OK) {
            updateTaskItems();
        }
    }

    private void initializeDatasets() {
        // Gets all task items from database
        taskModels = database.getTaskItems();

        if (taskModels.isEmpty()) {
            // Enables empty task list indicator if there are no task items
            empty_task_container.setVisibility(View.VISIBLE);
        } else {
            // Disables empty task list indicator if task items is not empty
            empty_task_container.setVisibility(View.GONE);
        }
    }

    private void displayTaskItems() {
        adapter = new TaskAdapter(MainActivity.this, taskModels);
        rv_main.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        rv_main.setAdapter(adapter);
    }

    // Updates task list on new task creation
    private void updateTaskItems() {
        taskModels = database.getTaskItems();
        displayTaskItems();
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
        // Go to settings activity
        if (item.getItemId() == R.id.options_settings) {
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        // Handles sorting of task items
        if (item.getItemId() == R.id.item_sort_AZ) {
            sortNameAZ();
        }
        if (item.getItemId() == R.id.item_sort_ZA) {
            sortNameZA();
        }
        return true;
    }

    @Override
    public void sortNameAZ() {
        taskModels.sort(Comparator.comparing(TaskModel::getName));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void sortNameZA() {
        taskModels.sort((task01, task02) -> task02.getName().compareTo(task01.getName()));
        adapter.notifyDataSetChanged();
    }
}