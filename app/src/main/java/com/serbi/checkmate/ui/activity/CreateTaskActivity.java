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

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.serbi.checkmate.R;
import com.serbi.checkmate.data.local.AppDatabase;
import com.serbi.checkmate.data.model.TaskModel;
import com.serbi.checkmate.util.ConstantsHolder;
import com.serbi.checkmate.util.DateHandler;

import java.util.Objects;

public class CreateTaskActivity extends AppCompatActivity {

    private AppDatabase appDatabase;

    private MaterialToolbar toolbar;
    private MaterialButton btn_create_task;
    private TextInputEditText tiet_task_name, tiet_task_notes, tiet_task_date_created;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeComponents();
        setFocusOnNameField();
        initializeToolbar();

        // Sets date created input field text to current date
        tiet_task_date_created.setText(DateHandler.getDetailedDate());

        btn_create_task.setOnClickListener(v -> createNewTask());
    }

    private void initializeComponents() {
        toolbar = findViewById(R.id.tb_create_task);
        appDatabase = new AppDatabase(this);

        btn_create_task = findViewById(R.id.btn_create_task);
        tiet_task_name = findViewById(R.id.tiet_task_name);
        tiet_task_notes = findViewById(R.id.tiet_task_notes);
        tiet_task_date_created = findViewById(R.id.tiet_task_date_created);
    }

    private void initializeToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    // Set focus on task name on load
    private void setFocusOnNameField() {
        tiet_task_name.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(tiet_task_name, InputMethodManager.SHOW_IMPLICIT);
    }

    private void createNewTask() {
        String taskName = Objects.requireNonNull(tiet_task_name.getText()).toString();
        String taskNotes = Objects.requireNonNull(tiet_task_notes.getText()).toString();

        // Checks if the task name field is empty
        if (taskName.isEmpty()) {
            tiet_task_name.setError(getResources().getString(R.string.empty_name));
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
                ConstantsHolder.TASK_NOT_COMPLETED
        );
        appDatabase.createTask(newTask); // Creates new task in database

        Intent createNewTaskIntent = new Intent();
        setResult(RESULT_OK, createNewTaskIntent);
        finish();
    }
}