package com.example.m_hike.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.m_hike.objects.Hike;

@Database(entities = {Hike.class}, version = 1)
@TypeConverters(Converters.class )
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {  //Making it synchronized makes the function thread safe
        if (null == instance) {
            instance = Room.databaseBuilder(context, AppDatabase.class, "hike_db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
        return instance;
    }
    public abstract HikeDao hikeDao();
}