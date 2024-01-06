package com.example.m_hike.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.m_hike.Models.Hike;

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
