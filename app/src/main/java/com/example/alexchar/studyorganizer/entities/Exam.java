package com.example.alexchar.studyorganizer.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "exam")
public class Exam {
    @PrimaryKey(autoGenerate = true)
    private int eid;
    @ColumnInfo(name = "subject_id")
    private int subjectId;
    @ColumnInfo(name = "exam_day")
    private int examDay;
    @ColumnInfo(name = "exam_month")
    private int examMonth;
    @ColumnInfo(name = "exam_year")
    private int examYear;
    @ColumnInfo(name = "exam_room")
    private int examRoom;
    @ColumnInfo(name = "exam_difficulty")
    private int examDifficulty;

    public Exam(int subjectId, int examDay, int examMonth, int examYear, int examRoom, int examDifficulty) {
        this.subjectId = subjectId;
        this.examDay = examDay;
        this.examMonth = examMonth;
        this.examYear = examYear;
        this.examRoom = examRoom;
        this.examDifficulty = examDifficulty;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getExamDay() {
        return examDay;
    }

    public void setExamDay(int examDay) {
        this.examDay = examDay;
    }

    public int getExamMonth() {
        return examMonth;
    }

    public void setExamMonth(int examMonth) {
        this.examMonth = examMonth;
    }

    public int getExamYear() {
        return examYear;
    }

    public void setExamYear(int examYear) {
        this.examYear = examYear;
    }

    public int getExamRoom() {
        return examRoom;
    }

    public void setExamRoom(int examRoom) {
        this.examRoom = examRoom;
    }

    public int getExamDifficulty() {
        return examDifficulty;
    }

    public void setExamDifficulty(int examDifficulty) {
        this.examDifficulty = examDifficulty;
    }
}
