package com.example.alexchar.studyorganizer;

import android.arch.persistence.room.Database;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.alexchar.studyorganizer.dao.TaskDao;
import com.example.alexchar.studyorganizer.entities.Task;

@Database(entities = {Task.class},version = 1)
public abstract class TaskDatabase extends RoomDatabase {
    private static TaskDatabase INSTANCE;
    public abstract TaskDao taskDao();

    public static TaskDatabase getTaskDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TaskDatabase.class, "task-database").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }
    public static void destroyInstance(){
        INSTANCE = null;
    }
}
