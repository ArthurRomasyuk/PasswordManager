package com.example.arthur.passwordmanager.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.arthur.passwordmanager.dao.ProfileDao;
import com.example.arthur.passwordmanager.model.Profile;

@Database(entities = {Profile.class}, version = 1)
public abstract class ProfileDB extends RoomDatabase {
    public abstract ProfileDao profileDao();

    private static ProfileDB INSTANCE;


    public static ProfileDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ProfileDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ProfileDB.class, "profile_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
