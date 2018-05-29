package com.example.alexchar.studyorganizer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.alexchar.studyorganizer.R;
import com.example.alexchar.studyorganizer.database.SubjectDatabase;
import com.example.alexchar.studyorganizer.database.TaskDatabase;
import com.example.alexchar.studyorganizer.entities.Subject;
import com.example.alexchar.studyorganizer.entities.Task;

import static android.content.ContentValues.TAG;

public class TaskInfoFragment extends Fragment {
    private EditText title, subject, notes, date, time;
    private TextView dueTitle;
    private TaskDatabase tDatabase;
    private SubjectDatabase sDatabase;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_task_info, container, false);
        int taskId = getTaskId();
        setFields(view, taskId);
        Log.d(TAG, "onCreateView: " + taskId);
        // Inflate the layout for this fragment
        return view;
    }

    private void setFields(View view, int taskId) {
        title = view.findViewById(R.id.title);
        subject = view.findViewById(R.id.subject);
        notes = view.findViewById(R.id.notes);
        date = view.findViewById(R.id.date);
        time = view.findViewById(R.id.time);
        dueTitle = view.findViewById(R.id.due_date_title);
        tDatabase = TaskDatabase.getTaskDatabase(getContext());
        sDatabase = SubjectDatabase.getSubjectDatabase(getContext());
        Task task = tDatabase.taskDao().findById(taskId);
        title.setText(task.getTaskName());
        Subject taskSubject = sDatabase.subjectDao().findById(task.getSubjectId());
        if(taskSubject!=null){
            subject.setVisibility(View.VISIBLE);
            subject.setText(taskSubject.getSubjectName());
        }
        else{
            subject.setVisibility(View.GONE);
        }
        if(!task.getTaskNotes().equals("")){
            notes.setText(task.getTaskNotes());
        }
        else{
            notes.setVisibility(View.GONE);
        }

        String day = (task.getTaskDueDay() < 10 ? "0" : "") + task.getTaskDueDay();
        String month = (task.getTaskDueMonth() < 10 ? "0" : "") + task.getTaskDueMonth();
        String year = (task.getTaskDueYear() < 10 ? "0" : "") + task.getTaskDueYear();
        String hour = (task.getTaskDueHour() < 10 ? "0" : "") + task.getTaskDueHour();
        String minute = (task.getTaskDueMinute() < 10 ? "0" : "") + task.getTaskDueMinute();
        if(task.getTaskDueDay() == -1){
            date.setVisibility(View.GONE);
            time.setVisibility(View.GONE);
            dueTitle.setVisibility(View.GONE);
        }{
            date.setText(day + "/" + month + "/" + year);
            time.setText(hour + ":" + minute);
        }

    }

    private int getTaskId() {
        Bundle bundle = this.getArguments();
        if (bundle != null){
            int i = bundle.getInt("taskId", 1);
            return i;
        }
        return 1;
    }


}
