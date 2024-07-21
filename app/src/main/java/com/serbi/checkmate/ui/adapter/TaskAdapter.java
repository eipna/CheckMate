package com.serbi.checkmate.ui.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.textview.MaterialTextView;
import com.serbi.checkmate.R;
import com.serbi.checkmate.data.interfaces.TaskListener;
import com.serbi.checkmate.data.model.TaskModel;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private final TaskListener taskListener;

    private final Context context;
    private final ArrayList<TaskModel> taskModels;

    public TaskAdapter(Context context, TaskListener taskListener, ArrayList<TaskModel> taskModels) {
        this.taskModels = taskModels;
        this.context = context;
        this.taskListener = taskListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, null);
        return new ViewHolder(view, taskListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Sets the data and state of each component of the task item card on load
        holder.item_task_name.setText(taskModels.get(position).getName());
        holder.item_task_notes.setText(taskModels.get(position).getNotes());
        holder.item_task_check_box.setChecked(taskModels.get(position).getIsCompleted() == 1);

        holder.item_task_check_box.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (taskListener != null) {
                int taskItemPosition = holder.getAdapterPosition();
                if (taskItemPosition != RecyclerView.NO_POSITION) {
                    taskListener.onTaskCheck(taskItemPosition, isChecked);
                }
            }
        });

        // Sets the task item card appearance based on its is_completed value on load
        if (taskModels.get(position).getIsCompleted() == 1) {
            setTaskAppearance(holder, taskModels.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return taskModels.size();
    }

    // Handles the appearance of the task item card
    private void setTaskAppearance(ViewHolder holder, TaskModel task) {
        // Transforms both the task item name and notes text to strikethrough
        holder.item_task_name.setText(task.getName(), TextView.BufferType.SPANNABLE);
        Spannable taskNameSpannable = (Spannable) holder.item_task_name.getText();
        taskNameSpannable.setSpan(new StrikethroughSpan(), 0, holder.item_task_name.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.item_task_notes.setText(task.getNotes(), TextView.BufferType.SPANNABLE);
        Spannable taskNotesSpannable = (Spannable) holder.item_task_notes.getText();
        taskNotesSpannable.setSpan(new StrikethroughSpan(), 0, holder.item_task_notes.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Sets the alpha for all components in the task item to a lower value
        holder.item_task_name.setAlpha(0.3f);
        holder.item_task_notes.setAlpha(0.3f);
        holder.item_task_divider.setAlpha(0.3f);
        holder.item_task.setAlpha(0.8f);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView item_task_name, item_task_notes;
        MaterialCheckBox item_task_check_box;
        MaterialCardView item_task;
        MaterialDivider item_task_divider;

        public ViewHolder(@NonNull View itemView, TaskListener taskListener) {
            super(itemView);
            item_task = itemView.findViewById(R.id.item_task);
            item_task_name = itemView.findViewById(R.id.item_task_name);
            item_task_notes = itemView.findViewById(R.id.item_task_notes);
            item_task_check_box = itemView.findViewById(R.id.item_task_check_box);
            item_task_divider = itemView.findViewById(R.id.item_task_divider);

            itemView.setOnClickListener(v -> {
                if (taskListener != null) {
                    int taskItemPosition = getAdapterPosition();
                    if (taskItemPosition != RecyclerView.NO_POSITION) {
                        taskListener.onTaskClick(taskItemPosition);
                    }
                }
            });
        }
    }
}