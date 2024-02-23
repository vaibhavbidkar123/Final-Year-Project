package com.example.ambulanceapp;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

// AppDatabase.java
@Database(entities = {Users.class, MapPoints.class}, version = 2 ,exportSchema = false)
public abstract class userDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract MapPointDao mapPointDao();

    private static userDatabase INSTANCE;

    public static userDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            userDatabase.class, "user-database").fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    // Define the migration from version 1 to 2
//    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            // Perform the necessary schema changes here
//            // For example, if you added a new column 'new_column' to 'your_table_name':
//            // database.execSQL("ALTER TABLE your_table_name ADD COLUMN new_column TEXT");
//            // Create the new table 'map_points' with the desired schema
//            database.execSQL("CREATE TABLE IF NOT EXISTS `map_points` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `latitude` REAL NOT NULL, `longitude` REAL NOT NULL, `title` TEXT)");
//
//            // Copy data from the old table to the new one (if needed)
//            database.execSQL("INSERT INTO `map_points` (`latitude`, `longitude`, `title`) SELECT `latitude`, `longitude`, `title` FROM `old_table_name`");
//
//            // Drop the old table (if needed)
//            database.execSQL("DROP TABLE IF EXISTS `old_table_name`");
//        }
//    };
}
