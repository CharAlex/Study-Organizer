package com.example.alexchar.studyorganizer.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alexchar.studyorganizer.R;
import com.example.alexchar.studyorganizer.TaskDatabase;
import com.example.alexchar.studyorganizer.activities.TaskActivity;
import com.example.alexchar.studyorganizer.entities.Task;
import com.example.alexchar.studyorganizer.fragments.TaskInfoFragment;
import com.example.alexchar.studyorganizer.fragments.setTaskFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;

import static android.content.ContentValues.TAG;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> tasks;
    private Context context;
    private TaskDatabase tDatabase;

    public TaskAdapter(List<Task> tasks) {
        this.tasks = tasks;
//        Sort the tasks list so the checked tasks would appear on the bottom
        sortBooleanTypeList(tasks);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_row, parent, false);
        context = parent.getContext();
        tDatabase = TaskDatabase.getTaskDatabase(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //Change background color for every 2nd item
        if (position % 2 == 0) {
            holder.rootView.setBackgroundResource(R.color.rowBlueLight);
        }

        holder.taskTitle.setText(tasks.get(position).getTaskName());
        if (tasks.get(holder.getAdapterPosition()).getTaskDueDay() != -1) {
//            Call method to conver 1 to 01 etc
            List<String> formatedDateAndTime = setDateAndTimeFormat(position);
            holder.taskDate.setText(formatedDateAndTime.get(0) + "/" + formatedDateAndTime.get(1) + "/" + tasks.get(position).getTaskDueYear());
            holder.taskTime.setText(formatedDateAndTime.get(3) + ":" + formatedDateAndTime.get(2));
            holder.taskDate.setVisibility(View.VISIBLE);
            holder.taskTime.setVisibility(View.VISIBLE);
            holder.divider.setVisibility(View.VISIBLE);
        } else {
            holder.taskDate.setVisibility(View.GONE);
            holder.taskTime.setVisibility(View.GONE);
            holder.divider.setVisibility(View.GONE);
        }


//        Check if the checkbox is true then make text strike through
        holder.checkBox.setChecked(tasks.get(position).getTaskDone());
        if (holder.checkBox.isChecked()) {
            holder.taskTitle.setPaintFlags(holder.taskTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.taskTitle.setTextColor(context.getResources().getColor(R.color.darkGrey));
        } else {
            holder.taskTitle.setPaintFlags(0);
            holder.taskTitle.setTextColor(context.getResources().getColor(R.color.colorPrimary));

        }
        sortBooleanTypeList(tasks);

//        OnClick add the corresponding value to variable taskDone to database
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.checkBox.isChecked()) {
                    holder.taskTitle.setPaintFlags(holder.taskTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.taskTitle.setTextColor(context.getResources().getColor(R.color.darkGrey));
                    tDatabase.taskDao().updateCheck(tasks.get(position).getTid(), true);

                } else {
                    holder.taskTitle.setPaintFlags(0);
                    holder.taskTitle.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    tDatabase.taskDao().updateCheck(tasks.get(position).getTid(), false);
                }
//                NotifyDataSetChanged when the user presses any checkbox
                tasks.clear();
                tasks = tDatabase.taskDao().getAll();
                sortBooleanTypeList(tasks);
                notifyDataSetChanged();

            }
        });

//        Start edit task activity
        holder.edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int taskId = tasks.get(position).getTid();
                Bundle bundle = new Bundle();
                bundle.putInt("taskId", taskId);
                TaskActivity taskActivity = (TaskActivity) context;
                setTaskFragment fragment = new setTaskFragment();
                fragment.setArguments(bundle);
                taskActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        //Make click listener to each row item
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && motionEvent.getAction() != MotionEvent.ACTION_CANCEL) {
                    //Start new TaskInfo Fragment
                    parseTaskId(holder.getAdapterPosition());
                    return true;
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    TaskActivity taskActivity = (TaskActivity) context;
                    taskActivity.onBackPressed();
                    return true;
                }
                return false;
            }
        });

    }

    private void parseTaskId(int adapterPosition) {
        int taskId = tasks.get(adapterPosition).getTid();
        Bundle bundle = new Bundle();
        bundle.putInt("taskId", taskId);
        TaskActivity taskActivity = (TaskActivity) context;
        TaskInfoFragment fragment = new TaskInfoFragment();
        fragment.setArguments(bundle);
        taskActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private List<String> setDateAndTimeFormat(int position) {
        String day = (tasks.get(position).getTaskDueDay() < 10 ? "0" : "") + tasks.get(position).getTaskDueDay();
        String month = (tasks.get(position).getTaskDueMonth() < 10 ? "0" : "") + tasks.get(position).getTaskDueMonth();
        String minute = (tasks.get(position).getTaskDueMinute() < 10 ? "0" : "") + tasks.get(position).getTaskDueMinute();
        String hour = (tasks.get(position).getTaskDueHour() < 10 ? "0" : "") + tasks.get(position).getTaskDueHour();

        List<String> list = Arrays.asList(day, month, minute, hour);
        return list;
    }


    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle, taskDate, taskTime;
        CheckBox checkBox;
        LinearLayout rootView;
        View divider;
        ImageButton edit_button;

        public ViewHolder(final View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.task_title);
            checkBox = itemView.findViewById(R.id.check_box);
            rootView = itemView.findViewById(R.id.rootView);
            taskDate = itemView.findViewById(R.id.task_date);
            taskTime = itemView.findViewById(R.id.task_time);
            divider = itemView.findViewById(R.id.divider);
            edit_button = itemView.findViewById(R.id.edit_button);
        }

    }

    public List<Task> sortBooleanTypeList(List<Task> tasksList) {
        Collections.sort(tasksList, new Comparator<Task>() {
            @Override
            public int compare(Task task, Task t1) {
                int b1 = task.getTaskDone() ? 1 : 0;
                int b2 = t1.getTaskDone() ? 1 : 0;
                return b1 - b2;
            }
        });
        return tasksList;
    }

    public List<Task> getTasks() {
        return tasks;
    }

}
