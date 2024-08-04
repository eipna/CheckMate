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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.serbi.checkmate.App;
import com.serbi.checkmate.R;
import com.serbi.checkmate.data.interfaces.TaskListener;
import com.serbi.checkmate.data.local.Database;
import com.serbi.checkmate.data.model.TaskModel;
import com.serbi.checkmate.databinding.ActivityCompletedTaskBinding;
import com.serbi.checkmate.ui.adapter.TaskAdapter;
import com.serbi.checkmate.util.VibrationUtil;

import java.util.ArrayList;

public class CompletedTaskActivity extends AppCompatActivity implements TaskListener {

    private ArrayList<TaskModel> taskModels;

    private TaskAdapter adapter;
    private Database database;
    private ActivityCompletedTaskBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompletedTaskBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize new database instance
        database = new Database(this);

        initializeToolbar();
        initializeDatasets();
        displayTaskItems();
    }

    private final ActivityResultLauncher<Intent> editTaskIntentLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            updateTaskItems();
        }
    });

    // Updates task list on new task creation or task update
    private void updateTaskItems() {
        initializeDatasets();
        displayTaskItems();
    }

    // Displays the task items in the recyclerview
    private void displayTaskItems() {
        adapter = new TaskAdapter(CompletedTaskActivity.this, CompletedTaskActivity.this, taskModels);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(CompletedTaskActivity.this));
        binding.recyclerView.setAdapter(adapter);
    }

    private void initializeToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void initializeDatasets() {
        // Gets all task items from database ( 1 means true)
        taskModels = database.getTaskItems(App.TASK_COMPLETED);
        handleEmptyIndicator();
        invalidateOptionsMenu(); // Updates toolbar menu options
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.completed_task_options, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Toggles visibility of clear task menu item and empty completed task indicator
        MenuItem clearCompletedTaskItem = menu.findItem(R.id.item_clear_completed_task);
        clearCompletedTaskItem.setVisible(!taskModels.isEmpty());

        return super.onPrepareOptionsMenu(menu);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Goes back to parent activity
        if (item.getItemId() == android.R.id.home) {
            VibrationUtil.vibrate(this, App.VIBRATION_DEFAULT_DURATION);
            return super.onOptionsItemSelected(item);
        }

        // Initiates clear all completed tasks dialog
        if (item.getItemId() == R.id.item_clear_completed_task) {
            clearCompletedTasksDialog();
        }
        return true;
    }

    // Clear all completed tasks dialog
    private void clearCompletedTasksDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Dialog configuration for clearing all completed tasks
        builder.setTitle(R.string.dialog_name)
                .setMessage(R.string.dialog_message)
                .setNegativeButton(R.string.dialog_cancel, null)
                .setPositiveButton(R.string.dialog_clear, (dialog, which) -> clearCompletedTask())
                .create();
        builder.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void clearCompletedTask() {
        database.clearCompletedTasks(); // Clears all completed tasks in database
        taskModels.clear(); // Clears all items in completed tasks list
        adapter.notifyDataSetChanged(); // Updates adapter

        binding.noTaskIndicator.setVisibility(View.VISIBLE); // Makes empty completed tasks indicator visible
        invalidateOptionsMenu(); // Updates toolbar menu
    }

    @Override
    public void onTaskClick(int position) {
        // Passes through the name and notes of the task to the edit task activity
        Intent editTaskIntent = new Intent(CompletedTaskActivity.this, EditTaskActivity.class);
        editTaskIntent.putExtra("TASK_ID", taskModels.get(position).getId());
        editTaskIntent.putExtra("TASK_NAME", taskModels.get(position).getName());
        editTaskIntent.putExtra("TASK_NOTES", taskModels.get(position).getNotes());

        // Starts and listens for a result code from a parent activity
        editTaskIntentLauncher.launch(editTaskIntent);
    }

    @Override
    public void onTaskMoreOptionsClick(int position, View view) {
        
    }
}