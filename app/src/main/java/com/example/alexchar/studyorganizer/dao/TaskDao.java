package com.example.alexchar.studyorganizer.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.alexchar.studyorganizer.entities.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task")
    List<Task> getAll();

    @Query("SELECT * FROM task WHERE task_name LIKE :taskName")
    Task findByName(String taskName);

    @Query("SELECT * FROM task WHERE tid LIKE :tid")
    Task findById(int tid);

    @Query("SELECT COUNT(*) FROM task")
    int countTasks();

    @Insert
    void insertAll(Task... task);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM task")
    void deleteAll();

    @Query("UPDATE task SET task_name = :taskName, subject_id = :subjectId, task_due_year = :taskDueYear, " +
            "task_due_month = :taskDueMonth, task_due_day = :taskDueDay, task_due_hour = :taskDueHour, task_due_minute = :taskDueMinute, task_notes = :taskNotes" +
            " WHERE tid = :id")
    void  update(String taskName, int subjectId, int taskDueYear, int taskDueMonth, int taskDueDay, int taskDueHour, int taskDueMinute, String taskNotes, int id);

    @Query("UPDATE task SET task_done = :taskDone WHERE tid = :id")
    void  updateCheck(int id, boolean taskDone);

}
