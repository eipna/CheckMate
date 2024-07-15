package com.serbi.checkmate.ui.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.serbi.checkmate.R;
import com.serbi.checkmate.data.local.Database;

public class CreateTaskActivity extends AppCompatActivity {

    private Database database;

    private MaterialToolbar toolbar;
    private MaterialButton btn_create_task;
    private TextInputEditText tiet_task_name, tiet_task_notes;

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
        initializeToolbar();

        btn_create_task.setOnClickListener(v -> createNewTask());
    }

    private void initializeComponents() {
        toolbar = findViewById(R.id.tb_create_task);
        database = new Database(this);

        btn_create_task = findViewById(R.id.btn_create_task);
        tiet_task_name = findViewById(R.id.tiet_task_name);
        tiet_task_notes = findViewById(R.id.tiet_task_notes);
    }

    private void initializeToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void createNewTask() {
        String taskName = tiet_task_name.getText().toString();
        String taskNotes = tiet_task_notes.getText().toString();

        database.createTask(taskName, taskNotes);
        finish();
    }
}