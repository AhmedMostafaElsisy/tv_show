package com.example.tv_show.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tv_show.dao.TVShowDao;
import com.example.tv_show.models.TvShow;

@Database(entities = TvShow.class, version = 1, exportSchema = false)
public abstract class TVShowDataBase extends RoomDatabase {

    private static TVShowDataBase tvShowDataBase;

    public static synchronized TVShowDataBase getTvShowDataBase(Context context) {

        if (tvShowDataBase == null) {
            tvShowDataBase = Room.databaseBuilder(
                    context,
                    TVShowDataBase.class,
                    "tv_Shows_db"
            ).build();
        }
        return tvShowDataBase;

    }

    public abstract TVShowDao tvShowDao();
}
