package com.example.alexchar.studyorganizer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.alexchar.studyorganizer.R;
import com.example.alexchar.studyorganizer.database.SubjectDatabase;

public class MainActivity extends AppCompatActivity {

    private CardView subject_button, marks_button, task_button, exam_button;
    private SubjectDatabase sDatabase;
    private ProgressBar passedProgress, failedProgress;
    private TextView total,passed,failed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sDatabase = SubjectDatabase.getSubjectDatabase(getApplicationContext());
        setDashboardStatistics();
        //Open activities
        openActivities();
    }

    private void setDashboardStatistics() {
        total = findViewById(R.id.total_number);
        int totalSubjects = sDatabase.subjectDao().countSubjects();
        total.setText(String.valueOf(totalSubjects));
        passedProgress = findViewById(R.id.passed_progress);
        passedProgress.setMax(totalSubjects);
        int passedSubjects = sDatabase.subjectDao().getPassedSubjects();
        passedProgress.setProgress(passedSubjects);
        passed = findViewById(R.id.passed_number);
        passed.setText(String.valueOf(passedSubjects));
        failedProgress = findViewById(R.id.failed_progress);
        failedProgress.setMax(totalSubjects);
        int failedSubjects = totalSubjects-passedSubjects;
        failedProgress.setProgress(failedSubjects);
        failed=findViewById(R.id.failed_number);
        failed.setText(String.valueOf(failedSubjects));

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
        exam_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ExamActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPostResume() {
        setDashboardStatistics();
        super.onPostResume();
    }
}
