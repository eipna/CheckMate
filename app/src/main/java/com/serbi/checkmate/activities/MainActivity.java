package com.serbi.checkmate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.serbi.checkmate.R;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton btn_add_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeComponents();

        btn_add_task.setOnClickListener(v -> {
            Intent createTaskIntent = new Intent(MainActivity.this, CreateTaskActivity.class);
            startActivity(createTaskIntent);
        });
    }

    private void initializeComponents() {
        btn_add_task = findViewById(R.id.fba_add_task);
    }
}