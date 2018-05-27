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
    @ColumnInfo(name = "exam_minute")
    private int examMinute;
    @ColumnInfo(name = "exam_hour")
    private int examHour;
    @ColumnInfo(name = "exam_room")
    private String examRoom;
    @ColumnInfo(name = "exam_difficulty")
    private int examDifficulty;

    public Exam() {
    }

    public Exam(int subjectId, int examDay, int examMonth, int examYear, int examMinute, int examHour, String examRoom, int examDifficulty) {
        this.subjectId = subjectId;
        this.examDay = examDay;
        this.examMonth = examMonth;
        this.examYear = examYear;
        this.examMinute = examMinute;
        this.examHour = examHour;
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

    public int getExamMinute() {
        return examMinute;
    }

    public void setExamMinute(int examMinute) {
        this.examMinute = examMinute;
    }

    public int getExamHour() {
        return examHour;
    }

    public void setExamHour(int examHour) {
        this.examHour = examHour;
    }

    public String getExamRoom() {
        return examRoom;
    }

    public void setExamRoom(String examRoom) {
        this.examRoom = examRoom;
    }

    public int getExamDifficulty() {
        return examDifficulty;
    }

    public void setExamDifficulty(int examDifficulty) {
        this.examDifficulty = examDifficulty;
    }
}
