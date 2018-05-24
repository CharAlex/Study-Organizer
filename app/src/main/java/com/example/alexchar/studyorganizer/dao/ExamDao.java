package com.example.alexchar.studyorganizer.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.alexchar.studyorganizer.entities.Exam;
import com.example.alexchar.studyorganizer.entities.Task;

import java.util.List;

@Dao
public interface ExamDao {
    @Query("SELECT * FROM exam")
    List<Exam> getAll();

    @Query("SELECT * FROM exam WHERE eid LIKE :eid")
    Exam findById(int eid);

    @Query("SELECT COUNT(*) FROM exam")
    int countExams();

    @Insert
    long insert(Exam exam);

    @Delete
    void delete(Exam exam);

    @Query("DELETE FROM exam")
    void deleteAll();

    @Query("UPDATE exam SET exam_day = :examDay, exam_month = :examMonth, exam_year = :examYear, exam_difficulty = :examDifficulty, subject_id = :subjectId  WHERE eid = :eid")
    void  update(int examDay, int examMonth, int examYear, int examDifficulty, int subjectId, int eid);


}
