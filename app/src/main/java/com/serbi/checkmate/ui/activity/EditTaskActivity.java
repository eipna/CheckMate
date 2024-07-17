package com.serbi.checkmate.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

public class EditTaskActivity extends AppCompatActivity {

    private MaterialToolbar tb_edit_task;
    private TextInputEditText tiet_edit_task_name, tiet_edit_task_notes;
    private MaterialButton btn_delete_task;

    private Database database;

    private int taskIdExtra;
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

        tiet_edit_task_name.setText(taskNameExtra);
        tiet_edit_task_notes.setText(taskNotesExtra);

        btn_delete_task.setOnClickListener(v -> deleteTask());
    }

    private void initializeComponents() {
        database = new Database(EditTaskActivity.this);

        tb_edit_task = findViewById(R.id.tb_edit_task);
        tiet_edit_task_name = findViewById(R.id.tiet_edit_task_name);
        tiet_edit_task_notes = findViewById(R.id.tiet_edit_task_notes);
        btn_delete_task = findViewById(R.id.btn_delete_task);
    }

    private void initializeExtras() {
        taskIdExtra = getIntent().getIntExtra("task_id", -1);
        taskNameExtra = getIntent().getStringExtra("task_name");
        taskNotesExtra = getIntent().getStringExtra("task_notes");
    }

    private void initializeToolbar() {
        setSupportActionBar(tb_edit_task);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void deleteTask() {
        database.deleteTask(taskIdExtra);
        Intent backToMainIntent = new Intent(EditTaskActivity.this, MainActivity.class);
        startActivity(backToMainIntent);
        finish();
    }
}