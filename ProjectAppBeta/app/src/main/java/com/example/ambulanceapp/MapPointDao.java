package com.example.ambulanceapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MapPointDao {
    @Insert
    void insert(MapPoints mapPoint);

    @Delete
    void delete(MapPoints mapPoint);

    @Query("SELECT * FROM map_points")
    List<MapPoints> getAllMapPoints();
}

