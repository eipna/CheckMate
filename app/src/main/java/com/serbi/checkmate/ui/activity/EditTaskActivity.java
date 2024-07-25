package com.serbi.checkmate.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
import com.serbi.checkmate.databinding.ActivityEditTaskBinding;
import com.serbi.checkmate.util.DateHandler;
import com.serbi.checkmate.util.VibrationHandler;

import java.util.Objects;

public class EditTaskActivity extends AppCompatActivity {

    private AppDatabase appDatabase;
    private ActivityEditTaskBinding binding;

    private int taskIdExtra;
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
        appDatabase = new AppDatabase(this);

        initializeExtras();
        initializeToolbar();

        // Sets data from extras in main activity
        binding.tietEditTaskName.setText(taskNameExtra);
        if (taskNotesExtra.equals(getResources().getString(R.string.empty_notes))) {
            binding.tietEditTaskNotes.setText("");
        } else {
            binding.tietEditTaskNotes.setText(taskNotesExtra);
        }
        binding.tietEditTaskDateCreated.setText(DateHandler.getDetailedDate(taskDateCreated));

        binding.btnDeleteTask.setOnClickListener(v -> deleteTask());
        binding.btnSaveTask.setOnClickListener(v -> saveTask());

        // Shows date and time picker dialogs
        binding.tietEditTaskReminder.setOnClickListener(v -> {
            if (binding.switchTaskEditReminder.isChecked()) {
                Toast.makeText(EditTaskActivity.this, "Due reminder field is active", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EditTaskActivity.this, "Due reminder field is not active", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Retrieves extras from main activity
    private void initializeExtras() {
        taskIdExtra = getIntent().getIntExtra("TASK_ID", -1);
        taskNameExtra = getIntent().getStringExtra("TASK_NAME");
        taskNotesExtra = getIntent().getStringExtra("TASK_NOTES");
        taskDateCreated = getIntent().getLongExtra("TASK_DATE_CREATED", -1);
    }

    private void initializeToolbar() {
        setSupportActionBar(binding.tbEditTask);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void saveTask() {
        // Extracts the text from the name and notes field
        String newTaskName = Objects.requireNonNull(binding.tietEditTaskName.getText()).toString();
        String newTaskNotes = Objects.requireNonNull(binding.tietEditTaskNotes.getText()).toString();

        /* If the data from the extras and the current activity are the same,
        there will be no updates in the database and will just proceed to exiting the activity */
        if (sameData(newTaskName, newTaskNotes)) {
            finish();
        } else {
            TaskModel editedTask = new TaskModel(
                    taskIdExtra,
                    newTaskName,
                    newTaskNotes,
                    -1, // Does not need to be updated
                    DateHandler.getCurrentTimeStamp(),
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
            VibrationHandler.vibrate(this, CheckMateApplication.VIBRATION_DEFAULT_DURATION);
            finish();
        }

        // Show share task notes intent
        if (item.getItemId() == R.id.item_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, Objects.requireNonNull(binding.tietEditTaskNotes.getText()).toString());
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