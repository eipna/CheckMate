package com.serbi.checkmate.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.serbi.checkmate.CheckMateApplication;
import com.serbi.checkmate.R;
import com.serbi.checkmate.data.interfaces.Sortable;
import com.serbi.checkmate.data.interfaces.TaskListener;
import com.serbi.checkmate.data.local.AppDatabase;
import com.serbi.checkmate.data.model.TaskModel;
import com.serbi.checkmate.databinding.ActivityMainBinding;
import com.serbi.checkmate.ui.adapter.TaskAdapter;

import java.util.ArrayList;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements Sortable, TaskListener {

    private ArrayList<TaskModel> taskModels;
    private AppDatabase appDatabase;
    private TaskAdapter adapter;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setSupportActionBar(binding.toolbar); // Sets up the toolbar
        appDatabase = new AppDatabase(this);

        initializeDatasets();
        displayTaskItems();

        binding.addTask.setOnClickListener(v -> {
            Intent createTaskIntent = new Intent(MainActivity.this, CreateTaskActivity.class);
            createTaskIntentLauncher.launch(createTaskIntent);
        });
    }

    private final ActivityResultLauncher<Intent> createTaskIntentLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            updateTaskItems();
        }
    });

    private final ActivityResultLauncher<Intent> editTaskIntentLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            updateTaskItems();
        }
    });

    private void initializeDatasets() {
        // Gets all task items from database ( 0 means false)
        taskModels = appDatabase.getTaskItems(CheckMateApplication.TASK_NOT_COMPLETED);
        handleEmptyIndicator();
    }

    private void handleEmptyIndicator() {
        if (taskModels.isEmpty()) {
            // Enables empty task list indicator if there are no task items
            binding.noTaskIndicator.setVisibility(View.VISIBLE);
        } else {
            // Disables empty task list indicator if task items is not empty
            binding.noTaskIndicator.setVisibility(View.GONE);
        }
    }

    // Displays the task items in the recyclerview
    private void displayTaskItems() {
        adapter = new TaskAdapter(MainActivity.this, MainActivity.this, taskModels);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        binding.recyclerView.setAdapter(adapter);
    }

    // Updates task list on new task creation or task update
    private void updateTaskItems() {
        initializeDatasets();
        displayTaskItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflates the options menu for the toolbar
        getMenuInflater().inflate(R.menu.main_options, menu);

        // Sorts the items by default on application load
        MenuItem defaultSortingOption = menu.findItem(R.id.item_sort_AZ); // Sort by Ascending (Name A to Z)
        defaultSortingOption.setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Gets the toolbar's current menu
        Menu mainOptionsMenu = binding.toolbar.getMenu();

        // Gets all sorting menu items
        MenuItem sortNameAZ = mainOptionsMenu.findItem(R.id.item_sort_AZ);
        MenuItem sortNameZA = mainOptionsMenu.findItem(R.id.item_sort_ZA);
        MenuItem sortDateCreatedASC = mainOptionsMenu.findItem(R.id.item_sort_date_created_asc);
        MenuItem sortDateCreatedDESC = mainOptionsMenu.findItem(R.id.item_sort_date_created_desc);
        MenuItem sortLastEditedASC = mainOptionsMenu.findItem(R.id.item_sort_edit_asc);
        MenuItem sortLastEditedDESC = mainOptionsMenu.findItem(R.id.item_sort_edit_desc);

        // Gets all filtering menu items;
        MenuItem filterPriorityHigh = mainOptionsMenu.findItem(R.id.item_filter_high);
        MenuItem filterPriorityMedium = mainOptionsMenu.findItem(R.id.item_filter_medium);
        MenuItem filterPriorityLow = mainOptionsMenu.findItem(R.id.item_filter_low);
        MenuItem filterPriorityDefault = mainOptionsMenu.findItem(R.id.item_filter_default);

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

        // Handles filtering of task items by priority level
        if (item.getItemId() == R.id.item_filter_high) {
            filterPriorityHigh.setChecked(true);
            filterTasksByPriority(CheckMateApplication.TASK_PRIORITY_HIGH);
        }
        if (item.getItemId() == R.id.item_filter_medium) {
            filterPriorityMedium.setChecked(true);
            filterTasksByPriority(CheckMateApplication.TASK_PRIORITY_MEDIUM);
        }
        if (item.getItemId() == R.id.item_filter_low) {
            filterPriorityLow.setChecked(true);
            filterTasksByPriority(CheckMateApplication.TASK_PRIORITY_LOW);
        }
        if (item.getItemId() == R.id.item_filter_default) {
            filterPriorityDefault.setChecked(true);
            filterTasksByPriority(CheckMateApplication.TASK_PRIORITY_DEFAULT);
        }

        // Handles sorting of task items
        if (item.getItemId() == R.id.item_sort_AZ) { // Sort by Name (Ascending)
            if (!taskModels.isEmpty()) {
                sortNameAZ();
            }
            sortNameAZ.setChecked(true);
        }
        if (item.getItemId() == R.id.item_sort_ZA) { // Sort by Name (Descending)
            if (!taskModels.isEmpty()) {
                sortNameZA();
            }
            sortNameZA.setChecked(true);
        }
        if (item.getItemId() == R.id.item_sort_date_created_asc) { // Sort by Date Created (Ascending)
            if (!taskModels.isEmpty()) {
                sortDateCreatedAcs();
            }
            sortDateCreatedASC.setChecked(true);
        }
        if (item.getItemId() == R.id.item_sort_date_created_desc) { // Sort by Date Created (Descending)
            if (!taskModels.isEmpty()) {
                sortDateCreatedDesc();
            }
            sortDateCreatedDESC.setChecked(true);
        }
        if (item.getItemId() == R.id.item_sort_edit_asc) { // Sort by Last Edited (Ascending)
            if (!taskModels.isEmpty()) {
                sortLastEditedAsc();
            }
            sortLastEditedASC.setChecked(true);
        }
        if (item.getItemId() == R.id.item_sort_edit_desc) { // Sort by Last Edited (Descending)
            if (!taskModels.isEmpty()) {
                sortLastEditedDesc();
            }
            sortLastEditedDESC.setChecked(true);
        }
        return true;
    }

    // Filter tasks by priority level
    private void filterTasksByPriority(int priorityLevel) {
        taskModels = appDatabase.getTasksByPriority(priorityLevel);
        displayTaskItems();
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

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void sortDateCreatedAcs() {
        taskModels.sort(Comparator.comparingLong(TaskModel::getDateCreated));
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void sortDateCreatedDesc() {
        taskModels.sort((task1, task2) -> Long.compare(task2.getDateCreated(), task1.getDateCreated()));
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void sortLastEditedAsc() {
        taskModels.sort(Comparator.comparingLong(TaskModel::getLastEdited));
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void sortLastEditedDesc() {
        taskModels.sort((task1, task2) -> Long.compare(task2.getLastEdited(), task1.getLastEdited()));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onTaskClick(int position) {
        // Passes through the name and notes of the task to the edit task activity
        Intent editTaskIntent = new Intent(MainActivity.this, EditTaskActivity.class);
        editTaskIntent.putExtra("TASK_ID", taskModels.get(position).getId());
        editTaskIntent.putExtra("TASK_NAME", taskModels.get(position).getName());
        editTaskIntent.putExtra("TASK_NOTES", taskModels.get(position).getNotes());
        editTaskIntent.putExtra("TASK_DATE_CREATED", taskModels.get(position).getDateCreated());

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