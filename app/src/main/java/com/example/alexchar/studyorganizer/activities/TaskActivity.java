package com.example.alexchar.studyorganizer.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.LinearLayout;

import com.example.alexchar.studyorganizer.R;
import com.example.alexchar.studyorganizer.database.TaskDatabase;
import com.example.alexchar.studyorganizer.adapters.TaskAdapter;
import com.example.alexchar.studyorganizer.entities.Task;
import com.example.alexchar.studyorganizer.fragments.setTaskFragment;

import java.util.List;

public class TaskActivity extends AppCompatActivity {
    private FloatingActionButton addTaskButton;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private TaskDatabase tDatabase;
    private LinearLayout noTaskIcon;
    private String TAG = "TaskActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        setRecyclerView();
        setAddTaskButton();
        //Add swipe-delete feature
        swipeToDelete();
        setNoSubjectIcon();
    }


    private void setRecyclerView() {
        tDatabase = TaskDatabase.getTaskDatabase(getApplicationContext());
        List<Task> tasks = tDatabase.taskDao().getAll();
        recyclerView = findViewById(R.id.task_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(tasks);
        recyclerView.setAdapter(taskAdapter);
    }

    private void setAddTaskButton() {
        addTaskButton = findViewById(R.id.add_task_button);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment();
            }
        });
    }

    private void openFragment(){
        setTaskFragment fragment = new setTaskFragment();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction().add(R.id.fragment_container, fragment, null);
        transaction.addToBackStack(null);
        transaction.commit();
        addTaskButton.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        addTaskButton.setVisibility(View.VISIBLE);
    }

    private void swipeToDelete() {
        //Drag and Drop items feature
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(TaskActivity.this);
                builder.setMessage("Θέλετε σίγουρα να διαγράψετε το task;");
                builder.setPositiveButton("Διαγραφή", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        List<Task> tasks = taskAdapter.getTasks();
                        int taskId = tasks.get(position).getTid();
                        taskAdapter.notifyItemRemoved(position);
                        //Remove item from database too
                        Task task = tDatabase.taskDao().findById(taskId);
                        tDatabase.taskDao().delete(task);
                        //Delete subject from list
                        tasks.remove(position);
                        taskAdapter.notifyItemRangeChanged(position,tasks.size());
                        setNoSubjectIcon();
                    }
                }).setNegativeButton("ΟΧΙ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        taskAdapter.notifyItemRemoved(position+1);
                        taskAdapter.notifyItemRangeChanged(position,taskAdapter.getItemCount());
                        return;
                    }
                }).show();

            }
        }).attachToRecyclerView(recyclerView);
    }

    private void setNoSubjectIcon() {
        noTaskIcon = findViewById(R.id.no_tasks_icon);
        if(taskAdapter.getItemCount() > 0){
            noTaskIcon.setVisibility(View.GONE);
        }else{
            noTaskIcon.setVisibility(View.VISIBLE);
        }
    }

    public TaskAdapter getTaskAdapter(){
        return taskAdapter;
    }
}
