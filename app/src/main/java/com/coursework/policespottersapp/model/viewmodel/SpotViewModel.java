package com.coursework.policespottersapp.model.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.coursework.policespottersapp.model.Spot;
import com.coursework.policespottersapp.model.SpotChecksRepo;

import java.util.List;

public class SpotViewModel extends AndroidViewModel {


    private SpotChecksRepo repo;
    private LiveData<List<Spot>> allSpotChecks;
    public LiveData<List<Spot>> searchBySpotId;
    private final MutableLiveData<String> filterLiveData = new MutableLiveData<>();


    public SpotViewModel(@NonNull Application application) {
        super(application);
        repo = new SpotChecksRepo(application);
        allSpotChecks = repo.getAllSpotChecks();

        searchBySpotId = Transformations.switchMap(filterLiveData, id-> repo.getSpotChecksById(id));

    }

    public LiveData<List<Spot>> getAllSpotChecks() {return allSpotChecks;}

    public void insert(Spot spot) { repo.insert(spot);}
    public void delete(Spot spot) { repo.delete(spot);}

}
