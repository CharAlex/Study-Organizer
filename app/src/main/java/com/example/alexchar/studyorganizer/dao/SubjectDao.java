package com.example.alexchar.studyorganizer.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.alexchar.studyorganizer.entities.Subject;

import java.util.List;

@Dao
public interface SubjectDao {
    @Query("SELECT * FROM subject")
    List<Subject> getAll();

    @Query("SELECT * FROM subject WHERE subject_name LIKE :subjectName")
    Subject findByName(String subjectName);

    @Query("SELECT * FROM subject WHERE sid LIKE :sid")
    Subject findById(int sid);

    @Query("SELECT COUNT(*) FROM subject")
    int countSubjects();

    @Insert
    void insertAll(Subject... subject);

    @Delete
    void delete(Subject subject);

    @Query("DELETE FROM subject")
    void deleteAll();

    @Query("UPDATE subject SET subject_name = :subjectName, subject_type = :subjectType, subject_semester = :subjectSemester, " +
            "subject_teacher = :subjectTeacher, subject_points = :subjectPoints, subject_hours = :subjectHours, subject_room = :subjectRoom" +
            " WHERE sid = :id")
    void  update(String subjectName, String subjectType, String subjectSemester, String subjectTeacher, String subjectPoints, String subjectHours, String subjectRoom, int id);

    @Query("UPDATE subject SET subject_grade = :subjectGrade" +
            " WHERE sid = :id")
    void  updateGrade(int subjectGrade, int id);

    @Query("SELECT COUNT(*) FROM subject WHERE subject_grade >= 5")
    int  getPassedSubjects();
}
