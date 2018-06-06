package com.example.arthur.passwordmanager.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.arthur.passwordmanager.dao.ProfileDao;
import com.example.arthur.passwordmanager.db.ProfileDB;
import com.example.arthur.passwordmanager.model.Profile;

import java.util.List;

public class ProfileRepository {

    static final String SAVE = "SAVE";
    static final String DELETE = "DELETE";

    private ProfileDao profileDao;
    private LiveData<List<Profile>> profiles;

    public ProfileRepository(Application application) {
        ProfileDB db = ProfileDB.getDatabase(application);
        profileDao = db.profileDao();
        profiles = profileDao.getAllProfiles();
    }

    public LiveData<List<Profile>> getAllProfiles() {
        return profiles;
    }

    public void save(Profile profile) {
        new daoAsyncTask(profileDao, SAVE).execute(profile);
    }

    public void delete(Profile profile) {
        new daoAsyncTask(profileDao, DELETE).execute(profile);
    }

    public void deleteSelectedProfiles(List<Profile> profiles) {
        new deleteProfilesAsyncTask(profileDao).execute(profiles);
    }

    private static class daoAsyncTask extends AsyncTask<Profile, Void, Void> {

        private ProfileDao dao;
        private String task;

        daoAsyncTask(ProfileDao dao, String task) {
            this.dao = dao;
            this.task = task;
        }

        @Override
        protected Void doInBackground(final Profile... params) {
            switch (task) {
                case SAVE:
                    dao.save(params[0]);
                    break;
                case DELETE:
                    dao.delete(params[0]);
                    break;
            }
            return null;
        }
    }

    private static class deleteProfilesAsyncTask extends AsyncTask<List<Profile>, Void, Void> {

        private ProfileDao dao;

        deleteProfilesAsyncTask(ProfileDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(List<Profile>... params) {
            dao.deleteSelectedProfiles(params[0]);
            return null;
        }
    }

}
