package com.example.alexchar.studyorganizer.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.alexchar.studyorganizer.dao.ExamDao;
import com.example.alexchar.studyorganizer.entities.Exam;

@Database(entities = {Exam.class}, version = 1)
public abstract class ExamDatabase extends RoomDatabase {
    private static ExamDatabase INSTANCE;
    public abstract ExamDao examtDao();

    public static ExamDatabase getExamDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ExamDatabase.class, "exam-database").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }

    public static void destroyInstance(){
        INSTANCE = null;
    }


}
