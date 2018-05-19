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
    @ColumnInfo(name = "task_due_year")
    private int taskDueYear;
    @ColumnInfo(name = "task_due_month")
    private int taskDueMonth;
    @ColumnInfo(name = "task_due_day")
    private int taskDueDay;
    @ColumnInfo(name = "task_due_hour")
    private int taskDueHour;
    @ColumnInfo(name = "task_due_minute")
    private int taskDueMinute;
    @ColumnInfo(name = "task_notes")
    private String taskNotes;
    @ColumnInfo(name = "task_done")
    private boolean taskDone;

    public Task() {
    }

    public Task(String taskName, int subjectId, int taskDueYear, int taskDueMonth, int taskDueDay, int taskDueHour, int taskDueMinute, String taskNotes, boolean taskDone) {
        this.taskName = taskName;
        this.subjectId = subjectId;
        this.taskDueYear = taskDueYear;
        this.taskDueMonth = taskDueMonth;
        this.taskDueDay = taskDueDay;
        this.taskDueHour = taskDueHour;
        this.taskDueMinute = taskDueMinute;
        this.taskNotes = taskNotes;
        this.taskDone = taskDone;
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

    public int getTaskDueYear() {
        return taskDueYear;
    }

    public void setTaskDueYear(int taskDueYear) {
        this.taskDueYear = taskDueYear;
    }

    public int getTaskDueMonth() {
        return taskDueMonth;
    }

    public void setTaskDueMonth(int taskDueMonth) {
        this.taskDueMonth = taskDueMonth;
    }

    public int getTaskDueDay() {
        return taskDueDay;
    }

    public void setTaskDueDay(int taskDueDay) {
        this.taskDueDay = taskDueDay;
    }

    public int getTaskDueHour() {
        return taskDueHour;
    }

    public void setTaskDueHour(int taskDueHour) {
        this.taskDueHour = taskDueHour;
    }

    public int getTaskDueMinute() {
        return taskDueMinute;
    }

    public void setTaskDueMinute(int taskDueMinute) {
        this.taskDueMinute = taskDueMinute;
    }

    public String getTaskNotes() {
        return taskNotes;
    }

    public void setTaskNotes(String taskNotes) {
        this.taskNotes = taskNotes;
    }

    public boolean getTaskDone() {
        return taskDone;
    }

    public void setTaskDone(boolean taskDone) {
        this.taskDone = taskDone;
    }
}
