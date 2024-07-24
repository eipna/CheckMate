package com.serbi.checkmate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import androidx.activity.EdgeToEdge;
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

        // Initialize database
        appDatabase = new AppDatabase(this);

        setFocusOnNameField();
        initializeToolbar();

        // Sets date created input field text to current date
        binding.tietTaskDateCreated.setText(DateHandler.getDetailedDate());

        binding.btnCreateTask.setOnClickListener(v -> createNewTask());
    }

    private void initializeToolbar() {
        setSupportActionBar(binding.tbCreateTask);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    // Set focus on task name on load
    private void setFocusOnNameField() {
        binding.tietTaskName.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(binding.tietTaskName, InputMethodManager.SHOW_IMPLICIT);
    }

    private void createNewTask() {
        String taskName = Objects.requireNonNull(binding.tietTaskName.getText()).toString();
        String taskNotes = Objects.requireNonNull(binding.tietTaskNotes.getText()).toString();

        // Checks if the task name field is empty
        if (taskName.isEmpty()) {
            binding.tietTaskName.setError(getResources().getString(R.string.empty_name));
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
                DateHandler.getCurrentTimeStamp(),
                DateHandler.getCurrentTimeStamp(),
                CheckMateApplication.TASK_NOT_COMPLETED
        );
        appDatabase.createTask(newTask); // Creates new task in database

        Intent createNewTaskIntent = new Intent();
        setResult(RESULT_OK, createNewTaskIntent);
        finish();
    }
}