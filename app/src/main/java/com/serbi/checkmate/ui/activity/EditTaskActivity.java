package com.serbi.checkmate.ui.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.serbi.checkmate.R;

public class EditTaskActivity extends AppCompatActivity {

    private TextInputEditText tiet_edit_task_name, tiet_edit_task_notes;

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

        tiet_edit_task_name.setText(taskNameExtra);
        tiet_edit_task_notes.setText(taskNotesExtra);
    }

    private void initializeComponents() {
        tiet_edit_task_name = findViewById(R.id.tiet_edit_task_name);
        tiet_edit_task_notes = findViewById(R.id.tiet_edit_task_notes);
    }

    private void initializeExtras() {
        taskIdExtra = getIntent().getIntExtra("task_id", -1);
        taskNameExtra = getIntent().getStringExtra("task_name");
        taskNotesExtra = getIntent().getStringExtra("task_notes");
    }
}