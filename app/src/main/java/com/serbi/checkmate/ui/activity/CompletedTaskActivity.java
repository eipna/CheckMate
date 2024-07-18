package com.serbi.checkmate.ui.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.serbi.checkmate.Constants;
import com.serbi.checkmate.R;
import com.serbi.checkmate.data.local.AppDatabase;
import com.serbi.checkmate.data.model.TaskModel;
import com.serbi.checkmate.ui.adapter.TaskAdapter;

import java.util.List;

public class CompletedTaskActivity extends AppCompatActivity {

    private List<TaskModel> taskModels;

    private TaskAdapter adapter;
    private AppDatabase appDatabase;

    private MaterialToolbar toolbar;
    private RecyclerView rv_completed_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_completed_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeComponents();
        initializeToolbar();
        initializeDatasets();
        displayTaskItems();
    }

    // Displays the task items in the recyclerview
    private void displayTaskItems() {
        adapter = new TaskAdapter(this, taskModels);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        // Makes the task items to generate at the top of the list instead at the bottom
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        rv_completed_task.setLayoutManager(linearLayoutManager);
        rv_completed_task.setAdapter(adapter);
    }

    private void initializeComponents() {
        appDatabase = new AppDatabase(CompletedTaskActivity.this);

        toolbar = findViewById(R.id.tb_completed_task);
        rv_completed_task = findViewById(R.id.rv_completed_task);
    }

    private void initializeToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void initializeDatasets() {
        // Gets all task items from database ( 1 means true)
        taskModels = appDatabase.getTaskItems(Constants.TASK_COMPLETED);
    }
}