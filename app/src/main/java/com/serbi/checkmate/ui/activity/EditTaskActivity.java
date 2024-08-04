package com.serbi.checkmate.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.serbi.checkmate.App;
import com.serbi.checkmate.R;
import com.serbi.checkmate.data.local.Database;
import com.serbi.checkmate.data.model.TaskModel;
import com.serbi.checkmate.databinding.ActivityEditTaskBinding;
import com.serbi.checkmate.util.DateUtil;
import com.serbi.checkmate.util.VibrationUtil;

import java.util.Objects;

public class EditTaskActivity extends AppCompatActivity {

    private Database appDatabase;
    private ActivityEditTaskBinding binding;

    private int taskIdExtra, taskPriorityExtra;
    private long taskDateCreated;
    private String taskNameExtra, taskNotesExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditTaskBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize new database instance
        appDatabase = new Database(this);

        initializeExtras();
        initializeToolbar();

        // Sets data from extras in main activity
        binding.inputName.setText(taskNameExtra);
        if (taskNotesExtra.equals(getResources().getString(R.string.empty_notes))) {
            binding.inputNotes.setText("");
        } else {
            binding.inputNotes.setText(taskNotesExtra);
        }
        binding.inputDateCreated.setText(DateUtil.getDetailedDate(taskDateCreated));

        // Sets the checked priority level chip base on task priority level
        switch (taskPriorityExtra) {
            case App.TASK_PRIORITY_HIGH:
                binding.chipHigh.setChecked(true);
                break;
            case App.TASK_PRIORITY_MEDIUM:
                binding.chipMedium.setChecked(true);
                break;
            case App.TASK_PRIORITY_LOW:
                binding.chipLow.setChecked(true);
                break;
            case App.TASK_PRIORITY_NO_PRIORITY:
                binding.chipDefault.setChecked(true);
                break;
        }

        binding.deleteTask.setOnClickListener(v -> deleteTask());
        binding.saveTask.setOnClickListener(v -> saveTask());
    }

    // Retrieves extras from main activity
    private void initializeExtras() {
        taskIdExtra = getIntent().getIntExtra("TASK_ID", -1);
        taskNameExtra = getIntent().getStringExtra("TASK_NAME");
        taskNotesExtra = getIntent().getStringExtra("TASK_NOTES");
        taskPriorityExtra = getIntent().getIntExtra("TASK_PRIORITY", -1);
        taskDateCreated = getIntent().getLongExtra("TASK_DATE_CREATED", -1);
    }

    private void initializeToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void saveTask() {
        // Extracts the text from the name and notes field
        String newTaskName = Objects.requireNonNull(binding.inputName.getText()).toString();
        String newTaskNotes = Objects.requireNonNull(binding.inputNotes.getText()).toString();

        /* If the data from the extras and the current activity are the same,
        there will be no updates in the database and will just proceed to exiting the activity */
        if (sameData(newTaskName, newTaskNotes, taskPriorityExtra)) {
            finish();
        } else {
            TaskModel editedTask = new TaskModel(
                    taskIdExtra,
                    newTaskName,
                    newTaskNotes,
                    getPriorityLevel(),
                    -1, // Does not need to be updated
                    DateUtil.getCurrentTimeStamp(),
                    -1 // Does not need to be updated
            );
            appDatabase.editTask(editedTask);
            closeCurrentActivity();
        }
    }

    private void deleteTask() {
        appDatabase.deleteTask(taskIdExtra);
        closeCurrentActivity();
    }

    // Get the selected priority level
    private int getPriorityLevel() {
        if (binding.chipHigh.isChecked()) {
            return App.TASK_PRIORITY_HIGH;
        } else if (binding.chipMedium.isChecked()) {
            return App.TASK_PRIORITY_MEDIUM;
        } else if (binding.chipLow.isChecked()) {
            return App.TASK_PRIORITY_LOW;
        } else {
            return App.TASK_PRIORITY_NO_PRIORITY;
        }
    }

    // Closes the current activity and sends a RESULT_OK result code to initiate task update
    private void closeCurrentActivity() {
        Intent closeCurrentActivityIntent = new Intent();
        setResult(RESULT_OK, closeCurrentActivityIntent);
        finish();
    }

    // Checks if the data from the extras and current activity are the same
    private boolean sameData(String name, String notes, int priority) {
        return name.equals(taskNameExtra) && notes.equals(taskNotesExtra) && priority == taskPriorityExtra;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Close current activity rather than going back to parent activity
        if (item.getItemId() == android.R.id.home) {
            VibrationUtil.vibrate(this, App.VIBRATION_DEFAULT_DURATION);
            finish();
        }

        // Show share task notes intent
        if (item.getItemId() == R.id.item_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, Objects.requireNonNull(binding.inputNotes.getText()).toString());
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_task_options, menu);
        return true;
    }
}