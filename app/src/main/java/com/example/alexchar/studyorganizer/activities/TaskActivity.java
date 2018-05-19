package com.example.alexchar.studyorganizer.activities;

import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.alexchar.studyorganizer.R;
import com.example.alexchar.studyorganizer.TaskDatabase;
import com.example.alexchar.studyorganizer.adapters.TaskAdapter;
import com.example.alexchar.studyorganizer.entities.Task;
import com.example.alexchar.studyorganizer.fragments.SetMarkFragment;
import com.example.alexchar.studyorganizer.fragments.setTaskFragment;

import java.util.List;

public class TaskActivity extends AppCompatActivity {
    private FloatingActionButton addTaskButton;
    private RecyclerView recyclerView;
    private ArrayAdapter arrayAdapter;
    private TaskDatabase tDatabase;
    private String TAG = "TaskActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        setRecyclerView();
        setAddTaskButton();
    }


    private void setRecyclerView() {
        tDatabase = TaskDatabase.getTaskDatabase(getApplicationContext());
        List<Task> tasks = tDatabase.taskDao().getAll();
        recyclerView = findViewById(R.id.task_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final TaskAdapter taskAdapter = new TaskAdapter(tasks);
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


}
