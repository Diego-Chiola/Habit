package com.app.habit.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.app.habit.data.dao.UsagesDao;
import com.app.habit.data.model.Usage;

@Database(entities = {Usage.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabaseManager extends RoomDatabase {

    public abstract UsagesDao UsagesDao();

    private volatile static AppDatabaseManager _instance = null;

    public static synchronized AppDatabaseManager getDatabase(Context context) {
        if (_instance != null)
            return _instance;

        _instance = Room.databaseBuilder(context, AppDatabaseManager.class, "habit")
                .fallbackToDestructiveMigration()
                .build();
        return _instance;
    }
}
