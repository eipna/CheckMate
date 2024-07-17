package com.serbi.checkmate.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.textview.MaterialTextView;
import com.serbi.checkmate.R;
import com.serbi.checkmate.data.local.Database;
import com.serbi.checkmate.data.model.TaskModel;
import com.serbi.checkmate.ui.activity.EditTaskActivity;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private Context context;
    private ArrayList<TaskModel> taskModels;
    private Database database;

    public TaskAdapter(Context context, ArrayList<TaskModel> taskModels) {
        this.taskModels = taskModels;
        this.context = context;
        this.database = new Database(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Sets the data and state of each component of the task item card on load
        holder.item_task_name.setText(taskModels.get(position).getName());
        holder.item_task_notes.setText(taskModels.get(position).getNotes());
        holder.item_task_check_box.setChecked(taskModels.get(position).getIsCompleted() == 1);
        holder.item_task.setOnClickListener(v -> holder.item_task.setSelected(true));

        // Handles the state of the check box of the task item card
        holder.item_task_check_box.setOnCheckedChangeListener((buttonView, isChecked) -> toggleTask(holder, taskModels.get(position), isChecked));

        // Transitions to edit task activity to edit the specific task clicked
        holder.item_task.setOnClickListener(v -> {
            // Passes through the name and notes of the task to the edit task activity
            Intent editTaskIntent = new Intent(context, EditTaskActivity.class);
            editTaskIntent.putExtra("task_name", taskModels.get(position).getName());
            editTaskIntent.putExtra("task_notes", taskModels.get(position).getNotes());
            context.startActivity(editTaskIntent);
        });

        // Sets the task item card appearance based on its is_completed value on load
        if (taskModels.get(position).getIsCompleted() == 1) {
            setTaskAppearance(holder, taskModels.get(position), true);
        }
    }

    @Override
    public int getItemCount() {
        return taskModels.size();
    }

    // Handles the appearance of the task item card
    private void setTaskAppearance(ViewHolder holder, TaskModel task, boolean isChecked) {
        if (isChecked) {
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
        } else {
            /* Turns all components back to default, removing strikethrough
            from text views and setting alpha value back to 1.0 */
            Spannable taskNameSpannable = (Spannable) holder.item_task_name.getText();
            StrikethroughSpan[] taskNameSpans = taskNameSpannable.getSpans(0, taskNameSpannable.length(), StrikethroughSpan.class);
            for (StrikethroughSpan span : taskNameSpans) {
                taskNameSpannable.removeSpan(span);
            }

            Spannable taskNotesSpannable = (Spannable) holder.item_task_notes.getText();
            StrikethroughSpan[] taskNotesSpans = taskNotesSpannable.getSpans(0, taskNotesSpannable.length(), StrikethroughSpan.class);
            for (StrikethroughSpan span : taskNotesSpans) {
                taskNotesSpannable.removeSpan(span);
            }

            holder.item_task_name.setAlpha(1.0f);
            holder.item_task_notes.setAlpha(1.0f);
            holder.item_task_divider.setAlpha(1.0f);
            holder.item_task.setAlpha(1.0f);
        }
    }

    // Handles the task item card is_completed value in the database
    private void toggleTask(ViewHolder holder, TaskModel task, boolean isChecked) {
        // Change task item card appearance to not completed
        setTaskAppearance(holder, task, isChecked);

        // Toggle's the task's is_completed value to false (0)
        database.toggleTask(task.getId(), isChecked);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView item_task_name, item_task_notes;
        MaterialCheckBox item_task_check_box;
        MaterialCardView item_task;
        MaterialDivider item_task_divider;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_task = itemView.findViewById(R.id.item_task);
            item_task_name = itemView.findViewById(R.id.item_task_name);
            item_task_notes = itemView.findViewById(R.id.item_task_notes);
            item_task_check_box = itemView.findViewById(R.id.item_task_check_box);
            item_task_divider = itemView.findViewById(R.id.item_task_divider);
        }
    }
}