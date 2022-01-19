package com.peseca.safedata.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Password.class},version =1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PasswordDAO getDao();

    public static volatile AppDatabase instance = null;
    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class,"password.db")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}

