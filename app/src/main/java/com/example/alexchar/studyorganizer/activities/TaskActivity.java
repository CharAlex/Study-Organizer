package com.example.alexchar.studyorganizer.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.alexchar.studyorganizer.R;
import com.example.alexchar.studyorganizer.fragments.SetMarkFragment;
import com.example.alexchar.studyorganizer.fragments.setTaskFragment;

public class TaskActivity extends AppCompatActivity {
    private FloatingActionButton addTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        setAddTaskButton();
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
