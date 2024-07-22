package com.serbi.checkmate.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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
import com.serbi.checkmate.ConstantsHolder;
import com.serbi.checkmate.R;
import com.serbi.checkmate.data.interfaces.Sortable;
import com.serbi.checkmate.data.interfaces.TaskListener;
import com.serbi.checkmate.data.local.AppDatabase;
import com.serbi.checkmate.data.model.TaskModel;
import com.serbi.checkmate.ui.adapter.TaskAdapter;

import java.util.ArrayList;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements Sortable, TaskListener {

    private SharedPreferences sharedPreferences;
    private String prefsTheme;

    private ArrayList<TaskModel> taskModels;

    private AppDatabase appDatabase;
    private TaskAdapter adapter;
    private ActivityResultLauncher<Intent> createTaskIntentLauncher, editTaskIntentLauncher;

    private ConstraintLayout emptyTaskContainer;
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
            createTaskIntentLauncher.launch(createTaskIntent);
        });

        createTaskIntentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                updateTaskItems();
            }
        });

        editTaskIntentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                updateTaskItems();
            }
        });
    }

    private void initializeComponents() {
        sharedPreferences = getSharedPreferences("MINDCHECK", MODE_PRIVATE);
        prefsTheme = sharedPreferences.getString("THEME", "System");

        appDatabase = new AppDatabase(this);

        emptyTaskContainer = findViewById(R.id.cl_empty_container);
        rv_main = findViewById(R.id.rv_main);
        toolbar = findViewById(R.id.tb_main);
        btn_add_task = findViewById(R.id.fba_add_task);
    }

    private void initializeToolbar() {
        setSupportActionBar(toolbar);
    }

    private void initializeDatasets() {
        // Gets all task items from database ( 0 means false)
        taskModels = appDatabase.getTaskItems(ConstantsHolder.TASK_NOT_COMPLETED);
        handleEmptyIndicator();
    }

    private void handleEmptyIndicator() {
        if (taskModels.isEmpty()) {
            // Enables empty task list indicator if there are no task items
            emptyTaskContainer.setVisibility(View.VISIBLE);
        } else {
            // Disables empty task list indicator if task items is not empty
            emptyTaskContainer.setVisibility(View.GONE);
        }
    }

    // Displays the task items in the recyclerview
    private void displayTaskItems() {
        adapter = new TaskAdapter(MainActivity.this, MainActivity.this, taskModels);
        rv_main.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        rv_main.setAdapter(adapter);
    }

    // Updates task list on new task creation or task update
    private void updateTaskItems() {
        initializeDatasets();
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
        // Inflates the options menu for the toolbar
        getMenuInflater().inflate(R.menu.main_options, menu);

        // Sorts the items by default on application load
        MenuItem defaultSortingOption = menu.findItem(R.id.item_sort_AZ); // Sort by Ascending (Name A to Z)
        defaultSortingOption.setChecked(true);

        // Sorts the items in ascending beforehand, does not proceed if list is empty
        if (!taskModels.isEmpty()) {
            sortNameAZ();
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Gets the toolbar's current menu
        Menu mainOptionsMenu = toolbar.getMenu();

        // Refers to the sorting menu items
        MenuItem sortAZItem = mainOptionsMenu.findItem(R.id.item_sort_AZ);
        MenuItem sortZAItem = mainOptionsMenu.findItem(R.id.item_sort_ZA);

        // Go to settings activity
        if (item.getItemId() == R.id.options_settings) {
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        // Go to completed task activity
        if (item.getItemId() == R.id.item_completed_tasks) {
            Intent completedTaskIntent = new Intent(MainActivity.this, CompletedTaskActivity.class);
            startActivity(completedTaskIntent);
        }

        // Handles sorting of task items
        if (item.getItemId() == R.id.item_sort_AZ) {
            if (!taskModels.isEmpty()) {
                sortNameAZ();
            }
            sortAZItem.setChecked(true);
        }
        if (item.getItemId() == R.id.item_sort_ZA) {
            if (!taskModels.isEmpty()) {
                sortNameZA();
            }
            sortZAItem.setChecked(true);
        }
        return true;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void sortNameAZ() {
        taskModels.sort(Comparator.comparing(TaskModel::getName));
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void sortNameZA() {
        taskModels.sort((task01, task02) -> task02.getName().compareTo(task01.getName()));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onTaskClick(int position) {
        // Passes through the name and notes of the task to the edit task activity
        Intent editTaskIntent = new Intent(MainActivity.this, EditTaskActivity.class);
        editTaskIntent.putExtra("TASK_ID", taskModels.get(position).getId());
        editTaskIntent.putExtra("TASK_NAME", taskModels.get(position).getName());
        editTaskIntent.putExtra("TASK_NOTES", taskModels.get(position).getNotes());

        // Starts and listens for a result code from a parent activity
        editTaskIntentLauncher.launch(editTaskIntent);
    }

    @Override
    public void onTaskCheck(int position, boolean status) {
        appDatabase.toggleTask(taskModels.get(position).getId(), status); // Toggle's task status within database
        taskModels.remove(position); // Removes the task in the array
        adapter.notifyItemRemoved(position); // Removes the task in the adapter

        handleEmptyIndicator(); // Updates empty task indicator
    }
}