package com.example.alexchar.studyorganizer.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.alexchar.studyorganizer.R;
import com.example.alexchar.studyorganizer.TaskDatabase;
import com.example.alexchar.studyorganizer.entities.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.ContentValues.TAG;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>   {

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_row,parent,false);
        context = parent.getContext();
        tDatabase = TaskDatabase.getTaskDatabase(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.taskTitle.setText(tasks.get(position).getTaskName());

//        Check if the checkbox is true then make text strike through
        holder.checkBox.setChecked(tasks.get(position).getTaskDone());
        if(holder.checkBox.isChecked()){
            holder.taskTitle.setPaintFlags(holder.taskTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.taskTitle.setTextColor(context.getResources().getColor(R.color.darkGrey));
        }else{
            holder.taskTitle.setPaintFlags(0);
            holder.taskTitle.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }

//        OnClick add the corresponding value to variable taskDone to database
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.checkBox.isChecked()){
                    holder.taskTitle.setPaintFlags(holder.taskTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.taskTitle.setTextColor(context.getResources().getColor(R.color.darkGrey));
                    tDatabase.taskDao().updateCheck(tasks.get(position).getTid(),true);

                }else{
                    holder.taskTitle.setPaintFlags(0);
                    holder.taskTitle.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    tDatabase.taskDao().updateCheck(tasks.get(position).getTid(),false);
                }
//                NotifyDataSetChanged when the user presses any checkbox
                tasks.clear();
                tasks = tDatabase.taskDao().getAll();
                sortBooleanTypeList(tasks);
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle;
        CheckBox checkBox;

        public ViewHolder(final View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.task_title);
            checkBox = itemView.findViewById(R.id.check_box);
        }
    }

    public List<Task> sortBooleanTypeList(List<Task> tasksList){
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
