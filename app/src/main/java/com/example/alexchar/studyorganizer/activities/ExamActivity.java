package com.example.alexchar.studyorganizer.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.alexchar.studyorganizer.R;
import com.example.alexchar.studyorganizer.adapters.ExamAdapter;

import java.util.List;

public class ExamActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ExamAdapter examAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        setRecyclerView();
    }

    private void setRecyclerView() {
        recyclerView = findViewById(R.id.exam_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        examAdapter = new ExamAdapter();
        recyclerView.setAdapter(examAdapter);
    }
}
