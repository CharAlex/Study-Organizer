package com.example.alexchar.studyorganizer.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "subject")
public class Subject {
    @PrimaryKey(autoGenerate = true)
    private int sid;
    @ColumnInfo(name = "subject_name")
    private String subjectName;
    @ColumnInfo(name = "subject_type")
    private String subjectType;
    @ColumnInfo(name = "subject_semester")
    private String subjectSemester;
    @ColumnInfo(name = "subject_teacher")
    private String subjectTeacher;
    @ColumnInfo(name = "subject_points")
    private String subjectPoints;
    @ColumnInfo(name = "subject_hours")
    private String subjectHours;
    @ColumnInfo(name = "subject_room")
    private String subjectRoom;
    @ColumnInfo(name = "subject_grade")
    private int subjectGrade;

    @Ignore
    public Subject() {
    }

    public Subject(String subjectName, String subjectType, String subjectSemester, String subjectTeacher, String subjectPoints, String subjectHours, String subjectRoom, int subjectGrade) {
        this.subjectName = subjectName;
        this.subjectType = subjectType;
        this.subjectSemester = subjectSemester;
        this.subjectTeacher = subjectTeacher;
        this.subjectPoints = subjectPoints;
        this.subjectHours = subjectHours;
        this.subjectRoom = subjectRoom;
        this.subjectGrade = subjectGrade;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getSubjectSemester() {
        return subjectSemester;
    }

    public void setSubjectSemester(String subjectSemester) {
        this.subjectSemester = subjectSemester;
    }

    public String getSubjectTeacher() {
        return subjectTeacher;
    }

    public void setSubjectTeacher(String subjectTeacher) {
        this.subjectTeacher = subjectTeacher;
    }

    public String getSubjectPoints() {
        return subjectPoints;
    }

    public void setSubjectPoints(String subjectPoints) {
        this.subjectPoints = subjectPoints;
    }

    public String getSubjectHours() {
        return subjectHours;
    }

    public void setSubjectHours(String subjectHours) {
        this.subjectHours = subjectHours;
    }

    public String getSubjectRoom() {
        return subjectRoom;
    }

    public void setSubjectRoom(String subjectRoom) {
        this.subjectRoom = subjectRoom;
    }

    public int getSubjectGrade() {
        return subjectGrade;
    }

    public void setSubjectGrade(int subjectGrade) {
        this.subjectGrade = subjectGrade;
    }
}