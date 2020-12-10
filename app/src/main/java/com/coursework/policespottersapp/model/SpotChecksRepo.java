package com.coursework.policespottersapp.model;

import android.app.Application;
import android.os.AsyncTask;
import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

public class SpotChecksRepo {

    private SpotDao spotDao;
    private LiveData<List<Spot>> allSpotChecks;

    public  SpotChecksRepo(Application application){
        SpotChecksRoomDatabase sd = SpotChecksRoomDatabase.getDatabase(application);
        spotDao = sd.spotDao();
        allSpotChecks = spotDao.getAllSpotChecks();
    }

    public LiveData<List<Spot>> getAllSpotChecks() {return allSpotChecks;}

    public void insert(Spot spot){
        new InsertAsyncTask(spotDao).execute(spot);
    }
    public void delete(Spot spot){new DeleteAsyncTask(spotDao).execute(spot);}



    public LiveData<List<Spot>> getSpotChecksById(String id){
        return spotDao.getSpotChecksById(id);
    }


    public static class InsertAsyncTask extends AsyncTask<Spot, Void, Void>{

        private SpotDao asyncTaskDao;


        InsertAsyncTask(SpotDao dao) {asyncTaskDao = dao;}

        @Override
        protected Void doInBackground(Spot... spots) {

            asyncTaskDao.insert(spots[0]);

            return null;
        }

    }

    public static class DeleteAsyncTask extends AsyncTask<Spot, Void, Void>{

        private SpotDao aSyncTaskDao;

        DeleteAsyncTask(SpotDao dao){
            aSyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Spot... spots) {
            aSyncTaskDao.delete(spots[0]);
            return null;
        }
    }

}
