package com.example.m_hike.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.m_hike.Models.Observation;

import java.util.List;

@Dao
public interface ObservationDao {
    @Insert
    void insertSingleObservation(Observation observation);

    @Delete
    void deleteSingleObservation(Observation observation);
    @Update
    void update(Observation entity);

    @Query("SELECT * FROM observations")
    List<Observation> getAllObservations();
}
