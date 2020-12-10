package com.coursework.policespottersapp.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SpotDao {

    @Insert
    void insert(Spot spot);

    @Delete
    void delete(Spot spot);

    @Query("SELECT * FROM Spot ORDER BY id ASC")
    LiveData<List<Spot>> getAllSpotChecks();

    @Query("SELECT * FROM Spot WHERE id LIKE '%' || :id || '%' ORDER BY id ASC")
    LiveData<List<Spot>> getSpotChecksById(String id);




}
