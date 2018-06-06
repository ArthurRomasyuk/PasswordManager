package com.example.arthur.passwordmanager.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.arthur.passwordmanager.model.Profile;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(Profile profile);

    @Delete
    void delete(Profile profile);

    @Delete
    void deleteSelectedProfiles(List<Profile> profiles);

    @Query("SELECT * FROM profile_table")
    LiveData<List<Profile>> getAllProfiles();
}
