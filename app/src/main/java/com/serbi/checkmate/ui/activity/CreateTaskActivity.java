package com.serbi.checkmate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.serbi.checkmate.CheckMateApplication;
import com.serbi.checkmate.R;
import com.serbi.checkmate.data.local.AppDatabase;
import com.serbi.checkmate.data.model.TaskModel;
import com.serbi.checkmate.databinding.ActivityCreateTaskBinding;
import com.serbi.checkmate.util.DateHandler;
import com.serbi.checkmate.util.VibrationHandler;

import java.util.Objects;

public class CreateTaskActivity extends AppCompatActivity {

    private AppDatabase appDatabase;
    private ActivityCreateTaskBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateTaskBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize new database
        appDatabase = new AppDatabase(this);

        setFocusOnNameField();
        initializeToolbar();

        // Sets date created input field text to current date
        binding.inputDateCreated.setText(DateHandler.getDetailedDate());

        binding.createTask.setOnClickListener(v -> createNewTask());
    }

    private void initializeToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    // Set focus on task name on load
    private void setFocusOnNameField() {
        binding.inputName.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(binding.inputName, InputMethodManager.SHOW_IMPLICIT);
    }

    private void createNewTask() {
        String taskName = Objects.requireNonNull(binding.inputName.getText()).toString();
        String taskNotes = Objects.requireNonNull(binding.inputName.getText()).toString();

        // Checks if the task name field is empty
        if (taskName.isEmpty()) {
            binding.inputName.setError(getResources().getString(R.string.empty_name));
            return;
        }

        // Sets notes to default if no notes input is found
        if (taskNotes.isEmpty()) {
            taskNotes = getResources().getString(R.string.empty_notes);
        }

        // Container for the new created task
        TaskModel newTask = new TaskModel(
                -1, // Will not be used anyway
                taskName,
                taskNotes,
                getPriorityLevel(),
                DateHandler.getCurrentTimeStamp(),
                DateHandler.getCurrentTimeStamp(),
                CheckMateApplication.TASK_NOT_COMPLETED
        );
        appDatabase.createTask(newTask); // Creates new task in database

        Intent createNewTaskIntent = new Intent();
        setResult(RESULT_OK, createNewTaskIntent);
        finish();
    }

    // Get the selected priority level
    private int getPriorityLevel() {
        if (binding.chipHigh.isChecked()) {
            return CheckMateApplication.TASK_PRIORITY_HIGH;
        } else if (binding.chipMedium.isChecked()) {
            return CheckMateApplication.TASK_PRIORITY_MEDIUM;
        } else if (binding.chipLow.isChecked()) {
            return CheckMateApplication.TASK_PRIORITY_LOW;
        } else {
            return CheckMateApplication.TASK_PRIORITY_NO_PRIORITY;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            VibrationHandler.vibrate(this, CheckMateApplication.VIBRATION_DEFAULT_DURATION);
            return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}