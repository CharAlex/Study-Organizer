package com.example.alexchar.studyorganizer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.example.alexchar.studyorganizer.R;

public class MainActivity extends AppCompatActivity {

    private CardView subject_button, marks_button, task_button, exam_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Open activities
        openActivities();
    }

    public void openActivities(){
        subject_button = findViewById(R.id.subject_button);
        marks_button = findViewById(R.id.marks_button);
        task_button = findViewById(R.id.todo_button);
        exam_button = findViewById(R.id.exam_button);

        subject_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SubjectActivity.class);
                startActivity(intent);
            }
        });
        marks_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MarkActivity.class);
                startActivity(intent);
            }
        });
        task_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TaskActivity.class);
                startActivity(intent);
            }
        });
//        exam_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, activity);
//                startActivity(intent);
//            }
//        });
    }
}
