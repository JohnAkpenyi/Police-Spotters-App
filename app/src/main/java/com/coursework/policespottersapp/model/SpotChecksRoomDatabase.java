package com.coursework.policespottersapp.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Spot.class}, version = 1)
public abstract class SpotChecksRoomDatabase extends RoomDatabase {

    public abstract SpotDao spotDao();

    private static SpotChecksRoomDatabase INSTANCE;

    public static SpotChecksRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SpotChecksRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SpotChecksRoomDatabase.class, "spotChecks_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
