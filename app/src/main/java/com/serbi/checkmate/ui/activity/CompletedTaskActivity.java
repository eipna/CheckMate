package com.serbi.checkmate.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
    private ConstraintLayout emptyCompletedTaskContainer;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Edit task operation which updates the task list by updating the edited task
        if (requestCode == Constants.EDIT_TASK_REQUEST_CODE && resultCode == RESULT_OK) {
            updateTaskItems();
        }
    }

    // Updates task list on new task creation or task update
    private void updateTaskItems() {
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
        emptyCompletedTaskContainer = findViewById(R.id.cl_empty_completed_tasks_container);
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

        if (taskModels.isEmpty()) {
            // Enables empty task list indicator if there are no completed task items
            emptyCompletedTaskContainer.setVisibility(View.VISIBLE);
        } else {
            // Disables empty task list indicator if completed task items is not empty
            emptyCompletedTaskContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.completed_task_options, menu);
        return true;
    }
}