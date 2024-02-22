package com.example.ambulanceapp;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// AppDatabase.java
@Database(entities = {Users.class}, version = 1)
public abstract class userDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    private static userDatabase INSTANCE;

    public static userDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            userDatabase.class, "user-database")
                    .build();
        }
        return INSTANCE;
    }
}
