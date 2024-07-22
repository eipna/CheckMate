package com.serbi.checkmate.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.serbi.checkmate.R;
import com.serbi.checkmate.data.local.AppDatabase;
import com.serbi.checkmate.util.DateHandler;

import java.util.Objects;

public class EditTaskActivity extends AppCompatActivity {

    private MaterialToolbar tb_edit_task;
    private TextInputEditText tiet_edit_task_name, tiet_edit_task_notes, tiet_edit_task_date_created;
    private MaterialButton btn_delete_task, btn_save_task;

    private AppDatabase appDatabase;

    private int taskIdExtra;
    private long taskDateCreated;
    private String taskNameExtra, taskNotesExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeExtras();
        initializeComponents();
        initializeToolbar();

        // Sets data from extras in main activity
        tiet_edit_task_name.setText(taskNameExtra);
        tiet_edit_task_notes.setText(taskNotesExtra);
        tiet_edit_task_date_created.setText(DateHandler.getDetailedDate(taskDateCreated));

        btn_delete_task.setOnClickListener(v -> deleteTask());
        btn_save_task.setOnClickListener(v -> saveTask());
    }

    private void initializeComponents() {
        appDatabase = new AppDatabase(EditTaskActivity.this);

        tb_edit_task = findViewById(R.id.tb_edit_task);
        tiet_edit_task_name = findViewById(R.id.tiet_edit_task_name);
        tiet_edit_task_notes = findViewById(R.id.tiet_edit_task_notes);
        tiet_edit_task_date_created = findViewById(R.id.tiet_edit_task_date_created);
        btn_delete_task = findViewById(R.id.btn_delete_task);
        btn_save_task = findViewById(R.id.btn_save_task);
    }

    // Retrieves extras from main activity
    private void initializeExtras() {
        taskIdExtra = getIntent().getIntExtra("TASK_ID", -1);
        taskNameExtra = getIntent().getStringExtra("TASK_NAME");
        taskNotesExtra = getIntent().getStringExtra("TASK_NOTES");
        taskDateCreated = getIntent().getLongExtra("TASK_DATE_CREATED", -1);
    }

    private void initializeToolbar() {
        setSupportActionBar(tb_edit_task);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void saveTask() {
        // Extracts the text from the name and notes field
        String newTaskName = Objects.requireNonNull(tiet_edit_task_name.getText()).toString();
        String newTaskNotes = Objects.requireNonNull(tiet_edit_task_notes.getText()).toString();

        /* If the data from the extras and the current activity are the same,
        there will be no updates in the database and will just proceed to exiting the activity */
        if (sameData(newTaskName, newTaskNotes)) {
            finish();
        } else {
            appDatabase.editTask(taskIdExtra, newTaskName, newTaskNotes, DateHandler.getCurrentTimeStamp());
            closeCurrentActivity();
        }
    }

    private void deleteTask() {
        appDatabase.deleteTask(taskIdExtra);
        closeCurrentActivity();
    }

    // Closes the current activity and sends a RESULT_OK result code to initiate task update
    private void closeCurrentActivity() {
        Intent closeCurrentActivityIntent = new Intent();
        setResult(RESULT_OK, closeCurrentActivityIntent);
        finish();
    }

    // Checks if the data from the extras and current activity are the same
    private boolean sameData(String name, String notes) {
        return name.equals(taskNameExtra) && notes.equals(taskNotesExtra);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Close current activity rather than going back to parent activity
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}