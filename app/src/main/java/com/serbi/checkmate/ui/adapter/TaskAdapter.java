package com.serbi.checkmate.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.textview.MaterialTextView;
import com.serbi.checkmate.R;
import com.serbi.checkmate.data.interfaces.TaskListener;
import com.serbi.checkmate.data.model.TaskModel;
import com.serbi.checkmate.util.DateUtil;
import com.serbi.checkmate.util.ParserUtil;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private final TaskListener taskListener;
    private final PrettyTime prettyTime;
    private final Context context;
    private final ArrayList<TaskModel> taskModels;

    public TaskAdapter(Context context, TaskListener taskListener, ArrayList<TaskModel> taskModels) {
        this.taskModels = taskModels;
        this.context = context;
        this.taskListener = taskListener;
        this.prettyTime = new PrettyTime();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new ViewHolder(view, taskListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Sets the data and state of each component of the task item card on load
        holder.itemTaskCard.setOnClickListener(v -> holder.itemTaskCard.setSelected(true));
        holder.itemTaskName.setText(taskModels.get(position).getName());
        holder.itemTaskPriority.setText(ParserUtil.parsePriorityLevel(taskModels.get(position).getPriority()));
        holder.itemTaskDateCreated.setText(DateUtil.getDetailedDate(taskModels.get(position).getDateCreated()));
        holder.itemTaskLastEdited.setText(prettyTime.format(new Date(taskModels.get(position).getLastEdited())));

        // Sets the task item card appearance based on its is_completed value on load
        if (taskModels.get(position).getIsCompleted() == 1) {
            completeTaskAppearance(holder, taskModels.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return taskModels.size();
    }

    // Handles the appearance of the task item card
    private void completeTaskAppearance(ViewHolder holder, TaskModel task) {
        // Transforms both the task item name and notes text to strikethrough
        holder.itemTaskName.setText(task.getName(), TextView.BufferType.SPANNABLE);
        Spannable taskNameSpannable = (Spannable) holder.itemTaskName.getText();
        taskNameSpannable.setSpan(new StrikethroughSpan(), 0, holder.itemTaskName.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Sets the alpha for all components in the task item to a lower value
        holder.itemTaskCard.setAlpha(0.8f);
        holder.itemTaskName.setAlpha(0.3f);
        holder.itemTaskPriority.setAlpha(0.3f);
        holder.itemTaskDateCreated.setAlpha(0.3f);
        holder.itemTaskLastEdited.setElevation(0.3f);
        holder.itemTaskDateCreatedIMG.setAlpha(0.3f);
        holder.itemTaskPriorityIMG.setAlpha(0.3f);
        holder.itemTaskDivider.setAlpha(0.3f);
        holder.itemTaskMoreOptions.setAlpha(0.3f);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialDivider itemTaskDivider;
        MaterialCardView itemTaskCard;
        MaterialTextView itemTaskName, itemTaskDateCreated, itemTaskLastEdited, itemTaskPriority;
        ImageView itemTaskMoreOptions, itemTaskDateCreatedIMG, itemTaskPriorityIMG;

        public ViewHolder(@NonNull View itemView, TaskListener taskListener) {
            super(itemView);
            itemTaskCard = itemView.findViewById(R.id.itemTaskCard);
            itemTaskDivider = itemView.findViewById(R.id.itemTaskDivider);

            itemTaskName = itemView.findViewById(R.id.itemTaskName);
            itemTaskDateCreated = itemView.findViewById(R.id.itemTaskDateCreated);
            itemTaskPriority = itemView.findViewById(R.id.itemTaskPriority);
            itemTaskLastEdited = itemView.findViewById(R.id.itemTaskLastEdited);

            itemTaskMoreOptions = itemView.findViewById(R.id.itemTaskMoreOptions);
            itemTaskDateCreatedIMG = itemView.findViewById(R.id.itemTaskDateCreatedIMG);
            itemTaskPriorityIMG = itemView.findViewById(R.id.itemTaskPriorityIMG);

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