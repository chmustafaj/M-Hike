package com.example.m_hike.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.m_hike.objects.Hike;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface HikeDao {
    @Insert
    void insertSingleHike(Hike hike);

    @Delete
    void deleteSingleHike(Hike hike);
    @Update
    void update(Hike entity);

    @Query("SELECT * FROM hikes")
    List<Hike> getAllHikes();
}
