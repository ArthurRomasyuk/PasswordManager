package com.example.arthur.passwordmanager.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.example.arthur.passwordmanager.model.Profile;
import com.example.arthur.passwordmanager.repository.ProfileRepository;

import java.util.List;

public class ProfileViewModel extends AndroidViewModel {

    private ProfileRepository repository;
    private LiveData<List<Profile>> profiles;

    public ProfileViewModel(Application application) {
        super(application);
        repository = new ProfileRepository(application);
        profiles = repository.getAllProfiles();
    }

    public LiveData<List<Profile>> getAllProfiles() {
        return profiles;
    }

    public void save(Profile profile) {
        repository.save(profile);
    }

    public void delete(Profile profile) {
        repository.delete(profile);
    }

    public void deleteSelectedProfiles(List<Profile> profiles){
        repository.deleteSelectedProfiles(profiles);
    }

}