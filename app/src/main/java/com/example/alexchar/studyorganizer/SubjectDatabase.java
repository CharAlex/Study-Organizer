package com.example.alexchar.studyorganizer;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

@Database(entities = {Subject.class}, version = 3)
public abstract class SubjectDatabase extends RoomDatabase {
    private static SubjectDatabase INSTANCE;
    public abstract SubjectDao subjectDao();

    public static SubjectDatabase getSubjectDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), SubjectDatabase.class, "subject-database").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }

    public static void destroyInstance(){
        INSTANCE = null;
    }


}
