package com.example.alexchar.studyorganizer.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.widget.DatePicker;
import android.widget.TimePicker;

@Entity(tableName = "task")
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int tid;
    @ColumnInfo(name = "task_name")
    private String taskName;
    @ColumnInfo(name = "subject_id")
    private int subjectId;
    @ColumnInfo(name = "task_due_date")
    private String  dueDate;
    @ColumnInfo(name = "task_due_time")
    private String dueTime;
    @ColumnInfo(name = "task_notes")
    private String taskNotes;

    public Task(String taskName, int subjectId, String dueDate, String dueTime, String taskNotes) {
        this.taskName = taskName;
        this.subjectId = subjectId;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.taskNotes = taskNotes;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public String getTaskNotes() {
        return taskNotes;
    }

    public void setTaskNotes(String taskNotes) {
        this.taskNotes = taskNotes;
    }
}
