package com.serbi.checkmate.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textview.MaterialTextView;
import com.serbi.checkmate.R;
import com.serbi.checkmate.data.model.TaskModel;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private Context context;
    private ArrayList<TaskModel> taskModels;

    public TaskAdapter(Context context, ArrayList<TaskModel> taskModels) {
        this.taskModels = taskModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaskModel task = taskModels.get(position);
        boolean isTaskCompleted;
        if (task.getIsCompleted() == 1) {
            isTaskCompleted = true;
        } else {
            isTaskCompleted = false;
        }

        holder.item_task_name.setText(task.getName());
        holder.item_task_notes.setText(task.getNotes());
        holder.item_task_check_box.setChecked(isTaskCompleted);

        holder.item_task.setOnClickListener(v -> holder.item_task.setSelected(true));
    }

    @Override
    public int getItemCount() {
        return taskModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView item_task_name, item_task_notes;
        MaterialCheckBox item_task_check_box;
        MaterialCardView item_task;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_task = itemView.findViewById(R.id.item_task);
            item_task_name = itemView.findViewById(R.id.item_task_name);
            item_task_notes = itemView.findViewById(R.id.item_task_notes);
            item_task_check_box = itemView.findViewById(R.id.item_task_check_box);
        }
    }
}